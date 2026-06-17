package net.darkhax.bookshelf.common.mixin.patch.entity;

import net.darkhax.bookshelf.common.api.data.BookshelfTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    protected int lastHurtByPlayerMemoryTime;

    @Shadow
    private int lastHurtByMobTimestamp;

    /**
     * This patch allows mobs killed by Bookshelf's fake player damage to drop EXP and player specific loot. Bookshelf's
     * fake player damage is not connected to a specific entity instance so the timers responsible for these checks are
     * not updated otherwise.
     */
    @Inject(method = "hurtServer", at = @At(value = "RETURN"))
    private void updateFakePlayerDamageTimes(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && damage > 0f && source.is(BookshelfTags.FAKE_PLAYER_DAMAGE)) {
            this.lastHurtByPlayerMemoryTime = Math.max(this.lastHurtByPlayerMemoryTime, 100);
            this.lastHurtByMobTimestamp = Math.max(this.lastHurtByMobTimestamp, 100);
        }
    }
}