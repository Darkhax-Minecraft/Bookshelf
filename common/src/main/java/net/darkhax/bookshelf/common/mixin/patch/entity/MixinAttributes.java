package net.darkhax.bookshelf.common.mixin.patch.entity;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Attributes.class)
public class MixinAttributes {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerAttributes(new Register<>(provider.contentNamespace(), (id, attribute) -> Registry.register(BuiltInRegistries.ATTRIBUTE, id, attribute))));
    }
}