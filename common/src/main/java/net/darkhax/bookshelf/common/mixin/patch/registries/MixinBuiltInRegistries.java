package net.darkhax.bookshelf.common.mixin.patch.registries;

import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterPacket;
import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleTypes;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.mixin.access.particles.AccessSimpleParticleType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class MixinBuiltInRegistries {

    @Inject(method = "bootStrap()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/registries/BuiltInRegistries;freeze()V", ordinal = 0))
    private static void bootstrap(CallbackInfo callback) {
        // TODO consider moving this to a different location later
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            provider.registerLoadConditions(new Register<>(provider.contentNamespace(), LoadConditions::register));
            provider.registerPackets(new RegisterPacket(provider.contentNamespace(), Services.NETWORK::register));
            provider.registerParticleTypes(new RegisterParticleTypes(provider.contentNamespace(), (id, overrideLimit) -> {
                final SimpleParticleType particleType = AccessSimpleParticleType.init(overrideLimit);
                Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particleType);
                return () -> particleType;
            }));
        });
    }
}