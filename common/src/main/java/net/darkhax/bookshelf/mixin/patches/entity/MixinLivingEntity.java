package net.darkhax.bookshelf.mixin.patches.entity;

import net.darkhax.bookshelf.api.data.BookshelfTags;
import net.darkhax.bookshelf.api.item.IEquippable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    /**
     * A non-serialized timer that is used to allow entities to drop EXP within a certain time frame regardless of the
     * damage source that kills them. This mirrors LivingEntity#lastHurtByPlayerTime field which vanilla uses to track
     * if a player hit the entity recently enough for it to drop exp points. We use a separate timer because our sources
     * often lack a corresponding player which could lead to unexpected side effects.
     */
    @Unique
    private int bookshelf$expDamageTimer = 0;

    /**
     * Reduces the exp damage timer.
     */
    @Inject(method = "baseTick", at = @At("RETURN"))
    private void onBaseTick(CallbackInfo cbi) {
        if (!this.level().isClientSide && this.bookshelf$expDamageTimer > 0) {
            this.bookshelf$expDamageTimer--;
        }
    }

    /**
     * When the entity is damaged by a damage source that matches the bookshelf:cause_exp_drops tag set the
     * expDamageTimer to 100 ticks (5s). This will lead to the mob dropping EXP if it is killed within that time frame.
     */
    @Inject(method = "hurt", at = @At("HEAD"))
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        if (!this.level().isClientSide && !this.isInvulnerableTo(source) && source.is(BookshelfTags.DamageTypes.CAUSE_EXP_DROPS)) {
            this.bookshelf$expDamageTimer = 100;
        }
    }

    /**
     * Determines if the entity should drop EXP. If the expDamageTimer is active this will be true even if it otherwise
     * would not.
     */
    @Inject(method = "isAlwaysExperienceDropper", at = @At("RETURN"), cancellable = true)
    private void onExpDropTest(CallbackInfoReturnable<Boolean> callback) {
        if (!this.level().isClientSide && this.bookshelf$expDamageTimer > 0) {
            callback.setReturnValue(true);
        }
    }

    /**
     * Allows items to define a custom equipment slot.
     */
    @Inject(method = "getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;", at = @At("HEAD"), cancellable = true)
    private static void getEquipmentSlotForItem(ItemStack itemStack, CallbackInfoReturnable<EquipmentSlot> callback) {

        final IEquippable equippable = IEquippable.get(itemStack);

        if (equippable != null) {
            callback.setReturnValue(equippable.getEquipmentSlot(itemStack));
        }
    }

    private MixinLivingEntity() {
        super(null, null);
    }
}
