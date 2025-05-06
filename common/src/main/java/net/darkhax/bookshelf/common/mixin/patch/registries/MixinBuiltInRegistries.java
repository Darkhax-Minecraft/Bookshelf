package net.darkhax.bookshelf.common.mixin.patch.registries;

import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterPacket;
import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleTypes;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.registry.RegistryHandlers;
import net.darkhax.bookshelf.common.mixin.access.particles.AccessSimpleParticleType;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BuiltInRegistries.class)
public class MixinBuiltInRegistries {

    @Inject(method = "internalRegister(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/core/registries/BuiltInRegistries$RegistryBootstrap;)Lnet/minecraft/core/WritableRegistry;", at = @At("RETURN"))
    private static <T, R extends WritableRegistry<T>> void onRegistry(ResourceKey<? extends Registry<T>> key, R registry, @Coerce Object bootstrap, CallbackInfoReturnable<R> cir) {
        RegistryHandlers.loadContent(key, registry);
    }

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