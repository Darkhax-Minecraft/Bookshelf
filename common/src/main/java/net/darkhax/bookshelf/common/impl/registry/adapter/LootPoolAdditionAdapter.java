package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.data.loot.PoolTarget;
import net.darkhax.bookshelf.common.api.data.loot.modifiers.LootPoolAddition;
import net.darkhax.bookshelf.common.impl.data.loot.entries.LootItemStack;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootItem;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;

/**
 * Registers new LootPoolAddition from various mods to be applied by Bookshelf.
 *
 * @param owner        The ID of the mod registering loot additions.
 * @param registerFunc The function used to register new additions.
 */
public record LootPoolAdditionAdapter(String owner, RegisterFunc registerFunc) {

    public void add(String id, PoolTarget pool, ItemStackTemplate item, int weight) {
        add(id, pool.table(), pool.index(), pool.hash(), item, weight);
    }

    public void add(String id, PoolTarget pool, Item item, int weight) {
        add(id, pool.table(), pool.index(), pool.hash(), item, weight);
    }

    public void add(String id, ResourceKey<LootTable> tableId, int poolIndex, int poolHash, ItemStackTemplate item, int weight) {
        add(id, tableId.identifier(), poolIndex, poolHash, item, weight);
    }

    public void add(String id, Identifier tableId, int poolIndex, int poolHash, ItemStackTemplate item, int weight) {
        add(id, tableId, poolIndex, poolHash, LootItemStack.of(item, weight));
    }

    public void add(String id, ResourceKey<LootTable> tableId, int poolIndex, int poolHash, Item item, int weight) {
        add(id, tableId.identifier(), poolIndex, poolHash, item, weight);
    }

    public void add(String id, Identifier tableId, int poolIndex, int poolHash, Item item, int weight) {
        add(id, tableId, poolIndex, poolHash, AccessorLootItem.bookshelf$create(item.builtInRegistryHolder(), weight, 0, List.of(), List.of()));
    }

    public void add(String id, PoolTarget pool, LootPoolEntryContainer addition) {
        add(id, pool.table(), pool.index(), pool.hash(), addition);
    }

    public void add(String id, ResourceKey<LootTable> tableId, int poolIndex, int poolHash, LootPoolEntryContainer addition) {
        add(id, tableId.identifier(), poolIndex, poolHash, addition);
    }

    public void add(String id, Identifier tableId, int poolIndex, int poolHash, LootPoolEntryContainer addition) {
        registerFunc.register(tableId, poolIndex, poolHash, new LootPoolAddition(id(id), addition));
    }

    private Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(this.owner, id);
    }

    @FunctionalInterface
    public interface RegisterFunc {
        void register(Identifier tableId, int poolIndex, int poolHash, LootPoolAddition addition);
    }
}
