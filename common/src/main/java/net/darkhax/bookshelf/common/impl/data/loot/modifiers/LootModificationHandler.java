package net.darkhax.bookshelf.common.impl.data.loot.modifiers;

import net.darkhax.bookshelf.common.api.data.loot.modifiers.LootPoolAddition;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.registry.adapter.LootPoolAdditionAdapter;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootPool;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootTable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Handles collecting loot pool modifications from various mods and then applying those modifications to loot tables.
 */
public class LootModificationHandler {

    public static final Supplier<LootModificationHandler> HANDLER = CachedSupplier.cache(() -> {
        final LootModificationHandler handler = new LootModificationHandler();
        Services.CONTENT.get().forEach(provider -> provider.defineLootPoolAdditions(new LootPoolAdditionAdapter(provider.namespace(), handler::addPoolEntry)));
        return handler;
    });

    private final Map<Identifier, Map<Integer, Map<Integer, List<LootPoolAddition>>>> newPoolEntries = new HashMap<>();

    private void addPoolEntry(Identifier tableId, int poolIndex, int poolHash, LootPoolAddition entry) {
        // We consolidate entries together here to avoid additional iterations later on.
        // This also helps us avoid making the list mutable and immutable many times.
        final Map<Integer, Map<Integer, List<LootPoolAddition>>> tableEntries = newPoolEntries.computeIfAbsent(tableId, k -> new LinkedHashMap<>());
        final Map<Integer, List<LootPoolAddition>> indexEntries = tableEntries.computeIfAbsent(poolIndex, k -> new LinkedHashMap<>());
        final List<LootPoolAddition> hashEntries = indexEntries.computeIfAbsent(poolHash, k -> new LinkedList<>());
        hashEntries.add(entry);
    }

    public void processLootTable(Identifier tableId, LootTable table) {
        if (newPoolEntries.containsKey(tableId) && table instanceof AccessorLootTable accessor) {
            final List<LootPool> pools = accessor.bookshelf$pools();
            for (Map.Entry<Integer, Map<Integer, List<LootPoolAddition>>> indexEntry : newPoolEntries.get(tableId).entrySet()) {
                for (Map.Entry<Integer, List<LootPoolAddition>> hashEntry : indexEntry.getValue().entrySet()) {
                    final LootPool targetPool = findPool(indexEntry.getKey(), hashEntry.getKey(), pools);
                    if (targetPool == null) {
                        BookshelfMod.LOG.warn("Could not locate pool {} in table '{}'. The following loot additions will not be applied. {}", hashEntry.getKey(), tableId, hashEntry.getValue().stream().map(a -> a.id().toString()).collect(Collectors.joining(", ")));
                    }
                    else if (targetPool instanceof AccessorLootPool pool) {
                        final List<LootPoolEntryContainer> entries = new LinkedList<>(pool.bookshelf$entries());
                        for (LootPoolAddition addition : hashEntry.getValue()) {
                            entries.add(addition.entry());
                            BookshelfMod.LOG.debug("Added entry `{}` to pool `{}` in table `{}`.", addition.id(), indexEntry.getKey(), tableId);
                        }
                        pool.bookshelf$setEntries(Collections.unmodifiableList(entries));
                    }
                }
            }
        }
    }

    @Nullable
    private static LootPool findPool(int targetIndex, int targetHash, List<LootPool> pools) {
        // Check the target first, which will usually be correct.
        if (targetIndex > -1 && targetIndex < pools.size() + 1) {
            final LootPool pool = pools.get(targetIndex);
            if (pool instanceof ILootPoolHooks hooks && hooks.bookshelf$matches(targetHash)) {
                return pool;
            }
        }
        // If the pool order has been changed we can not rely on the target index, however
        // the hash can still be used as long as there is only one match.
        final List<LootPool> matchingHashes = new ArrayList<>();
        for (final LootPool pool : pools) {
            if (pool instanceof ILootPoolHooks hooks && hooks.bookshelf$matches(targetHash)) {
                matchingHashes.add(pool);
            }
        }
        return matchingHashes.size() == 1 ? matchingHashes.getFirst() : null;
    }
}