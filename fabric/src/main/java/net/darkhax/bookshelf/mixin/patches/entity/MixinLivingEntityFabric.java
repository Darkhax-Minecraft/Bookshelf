package net.darkhax.bookshelf.mixin.patches.entity;

import net.darkhax.bookshelf.impl.event.FabricBookshelfEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityFabric {

    @Shadow
    protected ItemStack useItem;

    @Shadow
    protected int useItemRemaining;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateUsingItem(Lnet/minecraft/world/item/ItemStack;)V"), method = "updatingUsingItem")
    private void updatingUsingItem(CallbackInfo ci) {

        final LivingEntity living = (LivingEntity) (Object) this;
        final AtomicInteger duration = new AtomicInteger(this.useItemRemaining);
        FabricBookshelfEvents.ITEM_USE_TICK_EVENT.invoker().onUseTick(living, useItem, duration);
        this.useItemRemaining = duration.get();
    }
}
