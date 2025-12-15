package net.darkhax.bookshelf.common.mixin.patch.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.darkhax.bookshelf.common.api.block.IBlockHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class MixinLightningBolt {

    @Inject(method = "powerLightningRod", at = @At("RETURN"))
    private void onLightningStrike(CallbackInfo ci, @Local BlockPos strikePos, @Local BlockState strikeState) {
        final LightningBolt self = (LightningBolt) (Object) this;
        final Block strikeBlock = strikeState.getBlock();
        Direction[] redirections = strikeBlock == Blocks.LIGHTNING_ROD ? IBlockHooks.LIGHTNING_REDIRECTION_FACES : IBlockHooks.NO_LIGHTNING_REDIRECTION_FACES;
        if (strikeBlock instanceof IBlockHooks extended) {
            extended.onLightningStrike(strikeState, self.level(), strikePos, self);
            redirections = extended.redirectLightningStrike(strikeState, self.level(), strikePos);
        }
        for (Direction direction : redirections) {
            final BlockPos indirectPos = strikePos.relative(direction);
            final BlockState indirectState = self.level().getBlockState(indirectPos);
            if (indirectState.getBlock() instanceof IBlockHooks extended) {
                extended.onLightningStrikeIndirect(indirectState, self.level(), indirectPos, self, strikePos);
            }
        }
    }
}
