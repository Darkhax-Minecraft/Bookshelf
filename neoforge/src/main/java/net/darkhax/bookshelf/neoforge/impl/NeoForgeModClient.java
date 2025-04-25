package net.darkhax.bookshelf.neoforge.impl;

import net.darkhax.bookshelf.common.api.registry.register.RegisterBlockEntityRenderer;
import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleProviders;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.BiConsumer;

public class NeoForgeModClient {

    public NeoForgeModClient(IEventBus bus) {
        bus.addListener(this::registerParticles);
        bus.addListener(this::registerEntityRenderers);
    }

    private void registerParticles(RegisterParticleProvidersEvent event) {
        final BiConsumer<SimpleParticleType, RegisterParticleProviders.SimpleParticleProviderBuilder> registerSimple = (type, builder) -> event.registerSpriteSet(type, builder::build);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerParticleFactories(new RegisterParticleProviders(provider.contentNamespace(), registerSimple)));
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RegisterBlockEntityRenderer.bindBlockEntityRenderers();
    }
}