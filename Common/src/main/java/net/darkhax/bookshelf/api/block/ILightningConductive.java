package net.darkhax.bookshelf.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This interface allows implementing blocks to respond to lighting strikes and redirect lightning strikes to other
 * adjacent blocks.
 */
public interface ILightningConductive {

    /**
     * A default array of faces for conductive blocks to redirect lightning to.
     */
    public static final Direction[] LIGHTNING_REDIRECTION_FACES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN};

    /**
     * An empty array of lightning redirection faces. Can be used when the block should not redirect lightning.
     */
    public static final Direction[] NO_REDIRECTION_FACES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN};

    /**
     * This method is called when a lightning bolt directly strikes the blocks.
     *
     * @param world     The world the block is in.
     * @param pos       The position the lightning bolt struck.
     * @param state     The state of the block struck by the lighting bolt.
     * @param lightning The lightning bolt that struck the block.
     */
    default void onDirectLightningStrike(Level world, BlockPos pos, BlockState state, LightningBolt lightning) {

        // No-op
    }

    /**
     * This method is called when a lightning bolt strikes an adjacent conductive block such as a lightning rod.
     *
     * @param world         The world the block is in.
     * @param strikePos     The position the lightning bolt struck.
     * @param strikeState   The state of the block struck by the lightning bolt.
     * @param indirectPos   The position of this block in the world.
     * @param indirectState The state of this block.
     * @param lightning     The lighting bolt that indirectly struck our block.
     */
    default void onIndirectLightingStrike(Level world, BlockPos strikePos, BlockState strikeState, BlockPos indirectPos, BlockState indirectState, LightningBolt lightning) {

        // No-Op
    }

    /**
     * Checks if the block should trigger {@link ILightningConductive#onIndirectLightingStrike(Level, BlockPos,
     * BlockState, BlockPos, BlockState, LightningBolt)} of adjacent blocks when directly struck by lightning.
     *
     * @param world       The world the block is in.
     * @param strikePos   The position the lightning bolt struck.
     * @param strikeState The state of the block struck by the lightning bolt.
     * @return Will the lightning indirectly strike adjacent blocks.
     */
    default boolean canRedirectLightning(Level world, BlockPos strikePos, BlockState strikeState) {

        return strikeState.is(Blocks.LIGHTNING_ROD);
    }

    /**
     * Gets an array of block faces that should conduct lightning when struck. If {@link #canRedirectLightning(Level,
     * BlockPos, BlockState)} is true, {@link #onIndirectLightingStrike(Level, BlockPos, BlockState, BlockPos,
     * BlockState, LightningBolt)} will be invoked on blocks adjacent to the faces returned by this method.
     *
     * @param world       The world the block is in.
     * @param strikePos   The position the lightning bolt struck.
     * @param strikeState The state of the block struck by the lightning bolt.
     * @return An array of adjacent faces that will be indirectly struck by lightning.
     */
    default Direction[] getLightningRedirectionFaces(Level world, BlockPos strikePos, BlockState strikeState) {

        return canRedirectLightning(world, strikePos, strikeState) ? LIGHTNING_REDIRECTION_FACES : NO_REDIRECTION_FACES;
    }
}
