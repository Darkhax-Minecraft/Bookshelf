package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public record RegisterParticleTypes(String owner, BiFunction<ResourceLocation, Boolean, Supplier<SimpleParticleType>> typeFactory) {

    public Supplier<SimpleParticleType> addSimple(String name) {
        return this.addSimple(name, false);
    }

    public Supplier<SimpleParticleType> addSimple(ResourceLocation id) {
        return this.addSimple(id, false);
    }

    public Supplier<SimpleParticleType> addSimple(String id, boolean ignoreLimit) {
        return this.addSimple(ResourceLocation.tryBuild(this.owner, id));
    }

    public Supplier<SimpleParticleType> addSimple(ResourceLocation id, boolean ignoreLimit) {
        return this.typeFactory.apply(id, ignoreLimit);
    }
}