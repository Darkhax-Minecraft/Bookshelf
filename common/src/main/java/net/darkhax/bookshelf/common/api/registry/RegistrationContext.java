package net.darkhax.bookshelf.common.api.registry;

import net.darkhax.bookshelf.common.impl.registry.adapter.ItemRegistryAdapter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds context that is shared between different registry adapters.
 */
public final class RegistrationContext {

    private final String namespace;
    private final Map<RegistryReference<ResourceKey<Block>, Block>, BlockItemGenerator> placeableBlocks = new HashMap<>();

    public RegistrationContext(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the namespace that all new content should be registered with.
     *
     * @return The namespace new content is registered with.
     */
    public String namespace() {
        return this.namespace;
    }

    /**
     * Associates a block with a factory that provides its corresponding item form. The produced item will be
     * automatically registered with the same ID as the block.
     *
     * @param block     The block to associate the item with.
     * @param itemBlock A factory that creates the placer item.
     */
    public void addPlaceableBlock(RegistryReference<ResourceKey<Block>, Block> block, BlockItemGenerator itemBlock) {
        this.placeableBlocks.put(block, itemBlock);
    }

    public void registerPlacableBlocks(ItemRegistryAdapter itemRegistry) {
        for (Map.Entry<RegistryReference<ResourceKey<Block>, Block>, BlockItemGenerator> placable : placeableBlocks.entrySet()) {
            placable.getValue().generateAndRegister(itemRegistry, placable.getKey().key(), placable.getKey().value().get());
        }
    }

    public interface BlockItemGenerator {
        void generateAndRegister(ItemRegistryAdapter registry, ResourceKey<Block> blockId, Block block);
    }
}