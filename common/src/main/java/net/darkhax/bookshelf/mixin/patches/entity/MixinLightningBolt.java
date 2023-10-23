package net.darkhax.bookshelf.mixin.patches.entity;

import net.darkhax.bookshelf.api.block.ILightningConductive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightningBolt.class)
public class MixinLightningBolt {

    /**
     * Patches lightning to allow blocks to react to being struck by lightning. Also introduces a system for blocks
     * redirecting lightning to adjacent blocks similar to the vanilla lightning rod block.
     */
    @Inject(method = "powerLightningRod()V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onStrikeBlock(CallbackInfo callback, BlockPos strikePos, BlockState strikeState) {

        final LightningBolt self = (LightningBolt) (Object) this;
        final Block strikeBlock = strikeState.getBlock();

        // Checks if the block being struck reacts to lightning.
        if (strikeBlock instanceof ILightningConductive extended) {

            extended.onDirectLightningStrike(self.level(), strikePos, strikeState, self);
        }

        // Checks if the block redirects lightning to adjacent blocks. This is
        // a hardcoded behaviour of the lightning rod that we expose to other
        // blocks as well.
        if (canRedirect(self.level(), strikePos, strikeState)) {

            for (Direction direction : getRedirectionSides(self.level(), strikePos, strikeState)) {

                final BlockPos indirectPos = strikePos.relative(direction);
                final BlockState indirectState = self.level().getBlockState(indirectPos);

                if (indirectState.getBlock() instanceof ILightningConductive extended) {

                    extended.onIndirectLightingStrike(self.level(), strikePos, strikeState, indirectPos, indirectState, self);
                }
            }
        }
    }

    @Unique
    private static boolean canRedirect(Level world, BlockPos pos, BlockState state) {

        return state.is(Blocks.LIGHTNING_ROD) || (state.getBlock() instanceof ILightningConductive extended && extended.canRedirectLightning(world, pos, state));
    }

    @Unique
    private static Direction[] getRedirectionSides(Level world, BlockPos pos, BlockState state) {

        if (state.is(Blocks.LIGHTNING_ROD)) {

            return ILightningConductive.LIGHTNING_REDIRECTION_FACES;
        }

        if (state.getBlock() instanceof ILightningConductive extended) {

            return extended.getLightningRedirectionFaces(world, pos, state);
        }

        return ILightningConductive.NO_REDIRECTION_FACES;
    }
}
