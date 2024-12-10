package net.darkhax.bookshelf.common.mixin.patch;

import net.darkhax.bookshelf.common.api.registry.register.RegisterCatVariant;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.animal.CatVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatVariant.class)
public class MixinCatVariant {

    @Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/world/entity/animal/CatVariant;", at = @At("RETURN"))
    private static void onBootstrap(Registry<CatVariant> registry, CallbackInfoReturnable<CatVariant> cir) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerCatVariants(new RegisterCatVariant(owner, (key, texture) -> Registry.register(registry, key, new CatVariant(texture))));
        });
    }
}
