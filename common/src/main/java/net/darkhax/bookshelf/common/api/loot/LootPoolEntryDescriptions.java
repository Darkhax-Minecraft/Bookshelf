package net.darkhax.bookshelf.common.api.loot;

import com.mojang.datafixers.util.Either;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.register.RegisterLootDescription;
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
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Provides a system for describing what items can be dropped by a loot table.
 */
public class LootPoolEntryDescriptions {

    private static final CachedSupplier<List<ItemStack>> UNKNOWN_ITEM_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.STRUCTURE_VOID);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.unknown"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.unknown.desc").withStyle(ChatFormatting.GRAY))));
        return List.of(stack);
    });

    private static final CachedSupplier<List<ItemStack>> EMPTY_ITEM_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.BARRIER);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.empty"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.empty.desc").withStyle(ChatFormatting.GRAY))));
        return List.of(stack);
    });

    private static final CachedSupplier<List<ItemStack>> DYNAMIC_DISPLAY = CachedSupplier.cache(() -> {
        final ItemStack stack = new ItemStack(Items.JIGSAW);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("tooltips.bookshelf.loot.dynamic"));
        stack.set(DataComponents.LORE, new ItemLore(List.of(), List.of(Component.translatable("tooltips.bookshelf.loot.dynamic.desc").withStyle(ChatFormatting.GRAY))));
        return List.of(stack);
    });

    private static final Map<LootPoolEntryType, LootPoolEntryDescriber> DESCRIBERS = new HashMap<>();
    private static boolean hasInitialized = false;

    public static final LootPoolEntryDescriber EMPTY = (server, entry) -> Optional.ofNullable(entry instanceof EmptyLootItem ? EMPTY_ITEM_DISPLAY.get() : null);
    public static final LootPoolEntryDescriber ITEM = (server, entry) -> Optional.ofNullable(entry instanceof AccessorLootItem accessor ? List.of(new ItemStack(accessor.bookshelf$item())) : null);
    public static final LootPoolEntryDescriber LOOT_TABLE = (server, entry) -> Optional.ofNullable(entry instanceof AccessorNestedLootTable accessor ? getPotentialItems(server, accessor.bookshelf$contents()) : null);
    public static final LootPoolEntryDescriber DYNAMIC = (server, entry) -> Optional.ofNullable(entry instanceof DynamicLoot ? DYNAMIC_DISPLAY.get() : null);
    public static final LootPoolEntryDescriber TAG = (server, entry) -> Optional.ofNullable(entry instanceof AccessorTagEntry tagEntry ? getTagItems(tagEntry.bookshelf$tag()) : null);
    public static final LootPoolEntryDescriber COMPOSITE = (server, entry) -> Optional.ofNullable(entry instanceof AccessorCompositeEntryBase access ? getPotentialItems(server, access.bookshelf$children()) : null);
    public static final LootPoolEntryDescriber ITEM_STACK = (server, entry) -> Optional.ofNullable(entry instanceof LootItemStack loot ? List.of(loot.getBaseStack()) : null);

    private static void bootstrap() {
        if (!hasInitialized) {
            final RegisterLootDescription register = new RegisterLootDescription(DESCRIBERS::put);
            Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerLootDescriptions(register));
            hasInitialized = true;
        }
    }

    /**
     * Gets a list of items that can be produced by a loot table.
     *
     * @param server The current Minecraft server instance.
     * @param table  The loot table to analyze.
     * @return A list of items that can be produced by the entry.
     */
    public static List<ItemStack> getPotentialItems(MinecraftServer server, Either<ResourceKey<LootTable>, LootTable> table) {
        final LootTable resolved = table.map(key -> server.reloadableRegistries().getLootTable(key), Function.identity());
        return resolved == null ? List.of() : getPotentialItems(server, resolved);
    }

    /**
     * Gets a list of items that can be produced by a loot table.
     *
     * @param server The current Minecraft server instance.
     * @param table  The loot table to analyze.
     * @return A list of items that can be produced by the entry.
     */
    public static List<ItemStack> getPotentialItems(MinecraftServer server, LootTable table) {
        final List<ItemStack> items = NonNullList.create();
        if (table instanceof AccessorLootTable tableAccess) {
            for (LootPool pool : tableAccess.bookshelf$pools()) {
                if (pool instanceof AccessorLootPool poolAccess) {
                    getPotentialItems(server, poolAccess.bookshelf$entries()).forEach(stack -> addStacking(items, stack));
                }
            }
        }
        return items;
    }

    /**
     * Gets a list of items that can be produced by a list of loot pool entries.
     *
     * @param server  The current Minecraft server instance.
     * @param entries A list of loot pool entries to analyze.
     * @return A list of items that can be produced by the entry.
     */
    public static List<ItemStack> getPotentialItems(MinecraftServer server, List<LootPoolEntryContainer> entries) {
        final List<ItemStack> items = NonNullList.create();
        for (LootPoolEntryContainer entry : entries) {
            items.addAll(getPotentialItems(server, entry));
        }
        return items;
    }

    /**
     * Gets a list of items that can be produced by a loot pool entry.
     *
     * @param server The current Minecraft server instance.
     * @param entry  The loot pool entry to analyze.
     * @return A list of items that can be produced by the entry.
     */
    public static List<ItemStack> getPotentialItems(MinecraftServer server, LootPoolEntryContainer entry) {
        bootstrap();
        final LootPoolEntryDescriber describer = DESCRIBERS.get(entry.getType());
        return describer != null ? describer.getPotentialDrops(server, entry).orElse(UNKNOWN_ITEM_DISPLAY.get()) : UNKNOWN_ITEM_DISPLAY.get();
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

    private static List<ItemStack> getTagItems(TagKey<Item> tag) {
        final List<ItemStack> items = new ArrayList<>();
        for (Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            items.add(new ItemStack(item));
        }
        return items;
    }
}