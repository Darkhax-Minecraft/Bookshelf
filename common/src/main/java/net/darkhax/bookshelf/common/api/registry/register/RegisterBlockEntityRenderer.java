package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
public record RegisterBlockEntityRenderer(BiConsumer<BlockEntityType, BlockEntityRendererProvider> bindFunc) {

    public <T extends BlockEntity> void bind(BlockEntityType<T> type, BlockEntityRendererProvider<T> rendererProvider) {
        this.bindFunc.accept(type, rendererProvider);
    }
}