package net.darkhax.bookshelf.mixin.accessors.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface AccessorLivingEntity {

    @Invoker("getHurtSound")
    SoundEvent bookshelf$getHurtSound(DamageSource source);

    @Invoker("getDeathSound")
    SoundEvent bookshelf$getDeathSound();

    @Invoker("getFallDamageSound")
    SoundEvent bookshelf$getFallDamageSound(int distance);

    @Invoker("getDrinkingSound")
    SoundEvent bookshelf$getDrinkingSound(ItemStack beverage);

    @Invoker("makePoofParticles")
    void bookshelf$makePoofParticles();
}
