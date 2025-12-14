package net.darkhax.bookshelf.common.mixin.patch.entity;

import net.darkhax.bookshelf.common.api.data.BookshelfTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
        if (!this.level().isClientSide && !this.isInvulnerableTo(source) && source.is(BookshelfTags.FAKE_PLAYER_DAMAGE)) {
            this.lastHurtByPlayerTime = this.tickCount;
            this.lastHurtByMobTimestamp = this.tickCount;
        }
    }

    private MixinLivingEntity() {
        super(null, null);
    }
}