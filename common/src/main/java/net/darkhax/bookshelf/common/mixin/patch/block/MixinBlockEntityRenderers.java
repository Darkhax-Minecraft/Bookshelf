package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.registry.register.RegisterBlockEntityRenderer;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderers.class)
public class MixinBlockEntityRenderers {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        final RegisterBlockEntityRenderer binder = new RegisterBlockEntityRenderer(MixinBlockEntityRenderers::register);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.bindBlockEntityRenderer(binder));
    }

    @Shadow
    @SuppressWarnings("rawtypes")
    private static void register(BlockEntityType type, BlockEntityRendererProvider renderProvider) {
    }
}