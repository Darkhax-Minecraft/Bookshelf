package net.darkhax.bookshelf.common.api.registry;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Holds context that is shared between different registry adapters.
 */
public final class RegistrationContext {

    private final String namespace;
    private final Map<RegistryReference<ResourceKey<Block>, Block>, Function<Block, Item>> placeableBlocks = new HashMap<>();

    private static final Map<Item, ResourceKey<DecoratedPotPattern>> INTERNAL_POT_PATTERN_ITEMS = new HashMap<>();
    public static final Map<Item, ResourceKey<DecoratedPotPattern>> POT_PATTERN_ITEMS = Collections.unmodifiableMap(INTERNAL_POT_PATTERN_ITEMS);

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
    public void addPlaceableBlock(RegistryReference<ResourceKey<Block>, Block> block, Function<Block, Item> itemBlock) {
        this.placeableBlocks.put(block, itemBlock);
    }

    /**
     * Provides an unmodifiable view of placeable blocks and their associated item factories.
     *
     * @return An unmodifiable map of placeable blocks to their placer item factories.
     */
    public Map<RegistryReference<ResourceKey<Block>, Block>, Function<Block, Item>> getPlaceableBlocks() {
        return Collections.unmodifiableMap(this.placeableBlocks);
    }

    /**
     * Associates an item with a decorated pot pattern. Replacing existing associations is not a supported use case.
     *
     * @param item    The item to associate with the pattern.
     * @param pattern The pattern displayed by the item.
     */
    public void addPotPatternItem(Item item, ResourceKey<DecoratedPotPattern> pattern) {
        if (INTERNAL_POT_PATTERN_ITEMS.containsKey(item)) {
            BookshelfMod.LOG.warn("Mod {} has changed the pot pattern of {} to {} from {}.", this.namespace(), BuiltInRegistries.ITEM.getKey(item), pattern.identifier(), INTERNAL_POT_PATTERN_ITEMS.get(item).identifier());
        }
        INTERNAL_POT_PATTERN_ITEMS.put(item, pattern);
    }
}