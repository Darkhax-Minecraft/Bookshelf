package net.darkhax.bookshelf.forge.impl;

import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleProviders;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.BiConsumer;

public class ForgeModClient {

    public ForgeModClient(FMLJavaModLoadingContext loadingContext) {
        loadingContext.getModEventBus().addListener(this::registerParticles);
    }

    private void registerParticles(RegisterParticleProvidersEvent event) {
        final BiConsumer<SimpleParticleType, RegisterParticleProviders.SimpleParticleProviderBuilder> registerSimple = (type, builder) -> event.registerSpriteSet(type, builder::build);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerParticleFactories(new RegisterParticleProviders(provider.contentNamespace(), registerSimple)));
    }
}