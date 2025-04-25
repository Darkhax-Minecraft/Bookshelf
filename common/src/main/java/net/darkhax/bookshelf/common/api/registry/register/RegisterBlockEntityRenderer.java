package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.mixin.access.block.AccessorBlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
public record RegisterBlockEntityRenderer(BiConsumer<BlockEntityType, BlockEntityRendererProvider> bindFunc) {

    public <T extends BlockEntity> void bind(BlockEntityType<T> type, BlockEntityRendererProvider<T> rendererProvider) {
        this.bindFunc.accept(type, rendererProvider);
    }

    public static void bindBlockEntityRenderers() {
        final RegisterBlockEntityRenderer binder = new RegisterBlockEntityRenderer(AccessorBlockEntityRenderers::bookshelf$register);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.bindBlockEntityRenderer(binder));
    }
}