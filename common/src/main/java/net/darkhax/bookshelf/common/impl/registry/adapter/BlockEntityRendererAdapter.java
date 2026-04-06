package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
public record BlockEntityRendererAdapter(BiConsumer<BlockEntityType, BlockEntityRendererProvider> bindFunc) {

    public <T extends BlockEntity, S extends BlockEntityRenderState> void bind(BlockEntityType<T> type, BlockEntityRendererProvider<T, S> rendererProvider) {
        this.bindFunc.accept(type, rendererProvider);
    }
}