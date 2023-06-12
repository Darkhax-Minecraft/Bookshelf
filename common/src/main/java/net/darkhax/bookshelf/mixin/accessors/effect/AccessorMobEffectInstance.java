package net.darkhax.bookshelf.mixin.accessors.effect;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEffectInstance.class)
public interface AccessorMobEffectInstance {

    @Accessor("hiddenEffect")
    MobEffectInstance bookshelf$getHiddenEffect();

    @Accessor("hiddenEffect")
    void bookshelf$setHiddenEffect(MobEffectInstance hiddenEffect);

    @Invoker("tickDownDuration")
    int bookshelf$tickDownDuration();

    @Accessor("duration")
    void setDuration(int duration);
}