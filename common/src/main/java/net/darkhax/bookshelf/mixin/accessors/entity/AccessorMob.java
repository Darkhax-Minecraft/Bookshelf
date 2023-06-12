package net.darkhax.bookshelf.mixin.accessors.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface AccessorMob {

    @Invoker("getAmbientSound")
    SoundEvent bookshelf$getAmbientSound();
}