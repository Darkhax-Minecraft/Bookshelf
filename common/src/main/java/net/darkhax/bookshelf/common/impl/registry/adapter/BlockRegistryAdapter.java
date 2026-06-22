package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A registry adapter for the block registry.
 */
public class BlockRegistryAdapter extends GameRegistryAdapter<Block> {

    public BlockRegistryAdapter(RegistrationContext context, ResourceKey<Registry<Block>> regKey, BiConsumer<ResourceKey<Block>, Supplier<Block>> registryFunc) {
        super(context, regKey, registryFunc);
    }

    public RegistryReference<ResourceKey<Block>, Block> addSimplePlaceable(String key, UnaryOperator<BlockBehaviour.Properties> propertiesFunc) {
        return this.addPlaceable(key, Block::new, propertiesFunc);
    }

    public RegistryReference<ResourceKey<Block>, Block> addPlaceable(String key, Function<BlockBehaviour.Properties, Block> func) {
        return this.addPlaceable(key, func, UnaryOperator.identity());
    }

    public RegistryReference<ResourceKey<Block>, Block> addPlaceable(String key, Function<BlockBehaviour.Properties, Block> func, UnaryOperator<BlockBehaviour.Properties> propertiesFunc) {
        return this.add(key, func, propertiesFunc, (registry, id, block) ->  registry.add(key, itemProps -> new BlockItem(block, itemProps)));
    }

    public RegistryReference<ResourceKey<Block>, Block> addSimple(String key, UnaryOperator<BlockBehaviour.Properties> propertiesFunc, RegistrationContext.BlockItemGenerator placer) {
        return this.add(key, Block::new, propertiesFunc, placer);
    }

    public RegistryReference<ResourceKey<Block>, Block> addSimple(String key, UnaryOperator<BlockBehaviour.Properties> propertiesFunc) {
        return this.addSimple(key, propertiesFunc, null);
    }

    public RegistryReference<ResourceKey<Block>, Block> add(String key, Function<BlockBehaviour.Properties, Block> func) {
        return this.add(key, func, null);
    }

    public RegistryReference<ResourceKey<Block>, Block> add(String key, Function<BlockBehaviour.Properties, Block> func, RegistrationContext.BlockItemGenerator placer) {
        return this.add(key, func, UnaryOperator.identity(), placer);
    }

    public RegistryReference<ResourceKey<Block>, Block> add(String key, Function<BlockBehaviour.Properties, Block> func, UnaryOperator<BlockBehaviour.Properties> propertiesFunc, RegistrationContext.BlockItemGenerator placer) {
        final RegistryReference<ResourceKey<Block>, Block> blockRef = this.add(key, () -> {
            final Identifier blockId = this.id(key);
            final BlockBehaviour.Properties properties = propertiesFunc.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, blockId)));
            return func.apply(properties);
        });
        if (placer != null) {
            this.context.addPlaceableBlock(blockRef, placer);
        }
        return blockRef;
    }
}