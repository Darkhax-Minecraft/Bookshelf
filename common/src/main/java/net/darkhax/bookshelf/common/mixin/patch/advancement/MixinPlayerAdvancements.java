package net.darkhax.bookshelf.common.mixin.patch.advancement;

import net.darkhax.bookshelf.common.impl.data.criterion.trigger.AdvancementTrigger;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class MixinPlayerAdvancements {

    @Shadow
    private ServerPlayer player;

    @Shadow
    public abstract AdvancementProgress getOrStartProgress(AdvancementHolder advHolder);

    @Inject(method = "award(Lnet/minecraft/advancements/AdvancementHolder;Ljava/lang/String;)Z", at = @At("RETURN"))
    private void onAward(AdvancementHolder advancement, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (this.getOrStartProgress(advancement).isDone()) {
            AdvancementTrigger.TRIGGER.trigger(this.player, advancement);
        }
    }
}