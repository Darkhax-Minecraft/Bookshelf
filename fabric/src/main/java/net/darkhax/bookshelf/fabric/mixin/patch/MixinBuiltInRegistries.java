package net.darkhax.bookshelf.fabric.mixin.patch;

import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleProviders;
import net.darkhax.bookshelf.common.api.service.Services;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(BuiltInRegistries.class)
public class MixinBuiltInRegistries {

    @Inject(method = "bootStrap()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/registries/BuiltInRegistries;freeze()V", ordinal = 0), order = 9999)
    private static void bootstrap(CallbackInfo callback) {
        final ParticleFactoryRegistry factoryRegistry = ParticleFactoryRegistry.getInstance();
        final BiConsumer<SimpleParticleType, RegisterParticleProviders.SimpleParticleProviderBuilder> registerSimple = (type, builder) -> factoryRegistry.register(type, builder::build);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerParticleFactories(new RegisterParticleProviders(provider.contentNamespace(), registerSimple)));
    }
}