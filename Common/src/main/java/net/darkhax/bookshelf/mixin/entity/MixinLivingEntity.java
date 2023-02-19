package net.darkhax.bookshelf.mixin.entity;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.item.IEquippable;
import net.darkhax.bookshelf.api.data.BookshelfTags;
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

    @Unique
    private int bookshelf$timeSinceLastExpDamage = 0;

    @Inject(method = "baseTick", at = @At("RETURN"))
    private void onBaseTick(CallbackInfo cbi) {

        if (!this.level.isClientSide && this.bookshelf$timeSinceLastExpDamage > 0) {
            this.bookshelf$timeSinceLastExpDamage--;
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {

        if (!this.level.isClientSide && !this.isInvulnerableTo(source) && source.is(BookshelfTags.CAUSE_EXP_DROPS)) {
            this.bookshelf$timeSinceLastExpDamage = 100;
            Constants.LOG.info("Set: {}", this.bookshelf$timeSinceLastExpDamage);
        }
    }

    @Inject(method = "isAlwaysExperienceDropper", at = @At("RETURN"), cancellable = true)
    private void onExpDropTest(CallbackInfoReturnable<Boolean> callback) {

        Constants.LOG.info("Val: {}", this.bookshelf$timeSinceLastExpDamage);
        if (!this.level.isClientSide && this.bookshelf$timeSinceLastExpDamage > 0) {
            callback.setReturnValue(true);
        }
    }

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
