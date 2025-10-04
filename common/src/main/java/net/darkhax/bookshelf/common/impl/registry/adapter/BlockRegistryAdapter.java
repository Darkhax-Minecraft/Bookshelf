package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A registry adapter for the block registry.
 */
public class BlockRegistryAdapter extends GameRegistryAdapter<Block> {

    public BlockRegistryAdapter(RegistrationContext context, ResourceKey<Registry<Block>> regKey, BiConsumer<ResourceKey<Block>, Supplier<Block>> registryFunc) {
        super(context, regKey, registryFunc);
    }

    /**
     * Adds a new block to the block registry and queues up a BlockItem to be registered automatically during item
     * registration. The item will be registered using the same ID as the block.
     *
     * @param key   The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param value A supplier that will produce the block to register.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceKey<Block>, Block> addPlaceable(String key, Supplier<Block> value) {
        return this.addPlaceable(key, value, block -> new BlockItem(block, new Item.Properties()));
    }

    /**
     * Adds a new block to the block registry and queues up a custom placer item to be registered automatically during
     * item registry. The item will be registered using the same ID as the block.
     *
     * @param key    The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param value  A supplier that will produce the block to register.
     * @param placer A factory that produces the placer item. The input block is the block that was registered.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceKey<Block>, Block> addPlaceable(String key, Supplier<Block> value, Function<Block, Item> placer) {
        final RegistryReference<ResourceKey<Block>, Block> reference = this.add(key, value);
        this.context.addPlaceableBlock(reference, placer);
        return reference;
    }
}