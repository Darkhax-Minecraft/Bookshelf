package net.darkhax.bookshelf.mixin.patches.block;

import net.darkhax.bookshelf.impl.event.FabricBookshelfEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class MixinFarmBlock {

    /**
     * Implements a farmland trample event equivalent on Fabric.
     */
    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f, CallbackInfo ci) {
        if (FabricBookshelfEvents.FARMLAND_TRAMPLE_EVENT.invoker().apply(entity, blockPos, blockState)) {
            ci.cancel();
        }
    }
}