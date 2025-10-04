package net.darkhax.bookshelf.common.api.loot;

import com.mojang.datafixers.util.Either;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.registry.adapter.LootDescriptionAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.data.loot.entries.LootItemStack;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorCompositeEntryBase;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootItem;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootPool;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootTable;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorNestedLootTable;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorTagEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides a system for describing what items can be dropped by a loot table.
 */
public class LootPoolEntryDescriptions {

    private static final CachedSupplier<ItemStack> UNKNOWN_ITEM_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.STRUCTURE_VOID);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.unknown"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.unknown.desc").withStyle(ChatFormatting.GRAY))));
        return stack;
    });

    private static final CachedSupplier<ItemStack> EMPTY_ITEM_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.BARRIER);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.empty"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.empty.desc").withStyle(ChatFormatting.GRAY))));
        return stack;
    });

    private static final CachedSupplier<ItemStack> DYNAMIC_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.JIGSAW);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.dynamic"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.dynamic.desc").withStyle(ChatFormatting.GRAY))));
        return stack;
    });

    private static final Map<LootPoolEntryType, LootPoolEntryDescriber> DESCRIBERS = new HashMap<>();
    private static boolean hasInitialized = false;

    public static final LootPoolEntryDescriber EMPTY = (server, entry, collector) -> {
        if (entry instanceof EmptyLootItem) {
            collector.accept(EMPTY_ITEM_DISPLAY.get());
        }
    };

    public static final LootPoolEntryDescriber ITEM = (server, entry, collector) -> {
        if (entry instanceof AccessorLootItem accessor) {
            collector.accept(new ItemStack(accessor.bookshelf$item()));
        }
    };

    public static final LootPoolEntryDescriber LOOT_TABLE = (server, entry, collector) -> {
        if (entry instanceof AccessorNestedLootTable accessor) {
            getPotentialItems(server, accessor.bookshelf$contents(), collector);
        }
    };
    public static final LootPoolEntryDescriber DYNAMIC = (server, entry, collector) -> {
        if (entry instanceof DynamicLoot) {
            collector.accept(DYNAMIC_DISPLAY.get());
        }
    };
    public static final LootPoolEntryDescriber TAG = (server, entry, collector) -> {
        if (entry instanceof AccessorTagEntry tagEntry) {
            getTagItems(tagEntry.bookshelf$tag(), collector);
        }
    };
    public static final LootPoolEntryDescriber COMPOSITE = (server, entry, collector) -> {
        if (entry instanceof AccessorCompositeEntryBase accessor) {
            getPotentialItems(server, accessor.bookshelf$children(), collector);
        }
    };
    public static final LootPoolEntryDescriber ITEM_STACK = (server, entry, collector) -> {
        if (entry instanceof LootItemStack loot) {
            collector.accept(loot.getBaseStack());
        }
    };

    private static void bootstrap() {
        if (!hasInitialized) {
            final LootDescriptionAdapter register = new LootDescriptionAdapter(DESCRIBERS::put);
            Services.CONTENT.get().forEach(provider -> provider.defineLootDescriptions(register));
            hasInitialized = true;
        }
    }

    /**
     * Generates a list of unique items that can generate from a loot table.
     *
     * @param registries The current registries.
     * @param table      The loot table to examine.
     * @return A list containing the entries.
     */
    public static List<ItemStack> getUniqueItems(@NotNull RegistryAccess registries, LootTable table) {
        final List<ItemStack> items = new ArrayList<>();
        getPotentialItems(registries, table, stack -> addStacking(items, stack));
        return items;
    }

    /**
     * Gets potential drops for a loot table.
     *
     * @param registries The current registries.
     * @param table      The loot table to examine.
     * @param consumer   Collects entries in your desired format.
     */
    public static void getPotentialItems(@NotNull RegistryAccess registries, Either<ResourceKey<LootTable>, LootTable> table, Consumer<ItemStack> consumer) {
        final LootTable resolved = table.map(rl -> registries.registryOrThrow(Registries.LOOT_TABLE).get(rl), Function.identity());
        if (resolved != null) {
            getPotentialItems(registries, resolved, consumer);
        }
    }

    /**
     * Gets potential drops for a loot table.
     *
     * @param registries The current registries.
     * @param table      The loot table to examine.
     * @param consumer   Collects entries in your desired format.
     */
    public static void getPotentialItems(@NotNull RegistryAccess registries, LootTable table, Consumer<ItemStack> consumer) {
        if (table instanceof AccessorLootTable tableAccess) {
            for (LootPool pool : tableAccess.bookshelf$pools()) {
                if (pool instanceof AccessorLootPool poolAccess) {
                    getPotentialItems(registries, poolAccess.bookshelf$entries(), consumer);
                }
            }
        }
    }

    /**
     * Gets potential drops for a list of loot pool entries.
     *
     * @param registries The current registries.
     * @param entries    A list of entries to examine.
     * @param collector  Collects entries in your desired format.
     */
    public static void getPotentialItems(@NotNull RegistryAccess registries, List<LootPoolEntryContainer> entries, Consumer<ItemStack> collector) {
        for (LootPoolEntryContainer entry : entries) {
            getPotentialItems(registries, entry, collector);
        }
    }

    /**
     * Gets potential drops from a loot pool entry.
     *
     * @param registries The current registries.
     * @param entry      The pool entry to examine.
     * @param collector  Collects entries in your desired format.
     */
    public static void getPotentialItems(@NotNull RegistryAccess registries, LootPoolEntryContainer entry, Consumer<ItemStack> collector) {
        bootstrap();
        final LootPoolEntryDescriber describer = DESCRIBERS.get(entry.getType());
        if (describer != null) {
            describer.getPotentialDrops(registries, entry, collector);
        }
        else {
            collector.accept(UNKNOWN_ITEM_DISPLAY.get());
        }
    }

    /**
     * Adds an ItemStack to a list, only if the item does not stack with any of the items already in the list.
     *
     * @param items The list to add to.
     * @param toAdd The entry to add.
     */
    private static void addStacking(List<ItemStack> items, ItemStack toAdd) {
        for (ItemStack existing : items) {
            if (Objects.equals(existing, toAdd) || ItemStack.isSameItemSameComponents(existing, toAdd)) {
                return;
            }
        }
        items.add(toAdd);
    }

    private static void getTagItems(TagKey<Item> tag, Consumer<ItemStack> collector) {
        for (Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            collector.accept(new ItemStack(item));
        }
    }
}