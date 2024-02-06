package net.darkhax.bookshelf.mixin.patches.entity;

import net.darkhax.bookshelf.api.data.BookshelfTags;
import net.darkhax.bookshelf.api.item.IEquippable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    @Shadow
    protected int lastHurtByPlayerTime;

    @Shadow
    private int lastHurtByMobTimestamp;

    /**
     * This patch allows mobs killed by Bookshelf's fake player damage to drop EXP and player specific loot. Bookshelf's
     * fake player damage is not connected to a specific entity instance so the timers responsible for these checks are
     * not updated otherwise.
     */
    @Inject(method = "hurt", at = @At("HEAD"))
    private void updateFakePlayerDamageTimes(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        if (!this.level().isClientSide && !this.isInvulnerableTo(source) && source.is(BookshelfTags.DamageTypes.FAKE_PLAYER)) {
            this.lastHurtByPlayerTime = this.tickCount;
            this.lastHurtByMobTimestamp = this.tickCount;
        }
    }

    /**
     * This patch implements an alternative to the vanilla Equipable interface that has access to the ItemStack context
     * and can be implemented on blocks or items. This gives mods greater control over deciding the intended slot and
     * allows blocks to be equipped without defining a custom BlockItem.
     */
    @Inject(method = "getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;", at = @At("HEAD"), cancellable = true)
    private static void getSlotFromStack(ItemStack itemStack, CallbackInfoReturnable<EquipmentSlot> callback) {
        final IEquippable equippable = IEquippable.get(itemStack);
        if (equippable != null) {
            callback.setReturnValue(equippable.getEquipmentSlot(itemStack));
        }
    }

    private MixinLivingEntity() {
        super(null, null);
    }
}
