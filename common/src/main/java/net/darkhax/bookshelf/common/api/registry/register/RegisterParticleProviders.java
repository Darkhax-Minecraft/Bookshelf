package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record RegisterParticleProviders(String owner, BiConsumer<SimpleParticleType, SimpleParticleProviderBuilder> registerSimple) {

    public void add(Supplier<SimpleParticleType> type, SimpleParticleProviderBuilder provider) {
        this.add(type.get(), provider);
    }

    public void add(String id, SimpleParticleProviderBuilder provider) {
        this.add(ResourceLocation.tryBuild(this.owner, id), provider);
    }

    public void add(ResourceLocation id, SimpleParticleProviderBuilder provider) {
        if (BuiltInRegistries.PARTICLE_TYPE.containsKey(id)) {
            final ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(id);
            if (type instanceof SimpleParticleType simpleType) {
                this.add(simpleType, provider);
            }
            else {
                throw new IllegalStateException("Particle type '" + id + "' is not a SimpleParticleType! type=" + type);
            }
        }
        else {
            throw new IllegalStateException("Particle type '" + id + "' has not been registered.");
        }
    }

    public void add(SimpleParticleType type, SimpleParticleProviderBuilder provider) {
        this.registerSimple.accept(type, provider);
    }

    public interface SimpleParticleProviderBuilder {
        <T extends SpriteSet> ParticleProvider<SimpleParticleType> build(T sprite);
    }
}
