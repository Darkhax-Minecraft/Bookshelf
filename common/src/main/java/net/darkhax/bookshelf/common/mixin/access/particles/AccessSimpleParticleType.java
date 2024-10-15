package net.darkhax.bookshelf.common.mixin.access.particles;

import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleParticleType.class)
public interface AccessSimpleParticleType {

    @Invoker("<init>")
    static SimpleParticleType init(boolean overrideLimit) {
        throw new IllegalStateException("That didn't mix well...");
    }
}
