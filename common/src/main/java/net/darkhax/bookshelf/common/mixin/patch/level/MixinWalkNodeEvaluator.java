package net.darkhax.bookshelf.common.mixin.patch.level;

import net.darkhax.bookshelf.common.api.block.IBlockHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WalkNodeEvaluator.class)
public class MixinWalkNodeEvaluator {

    /**
     * This patch allows modded blocks to control their own pathfinding type. This is done by implementing
     * {@link IBlockHooks#getPathfindingType(BlockState, BlockGetter, BlockPos)}.
     */
    @Inject(method = "getPathTypeFromState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/PathType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void getBlockPathTypeRaw(BlockGetter level, BlockPos pos, CallbackInfoReturnable<PathType> cbi, BlockState state) {
        if (state.getBlock() instanceof IBlockHooks hooks) {
            final PathType customType = hooks.getPathfindingType(state, level, pos);
            if (customType != null) {
                cbi.setReturnValue(customType);
            }
        }
    }
}