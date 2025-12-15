package net.darkhax.bookshelf.common.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public interface IBlockHooks {

    Direction[] LIGHTNING_REDIRECTION_FACES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN};
    Direction[] NO_LIGHTNING_REDIRECTION_FACES = new Direction[]{};

    /**
     * Allows the block to determine its own pathfinding type.
     *
     * @param state   The current state of the block.
     * @param context Additional context from the world the block is in.
     * @param pos     The position of the block.
     * @return The pathfinding type for the block. If null is returned the vanilla behavior for determining pathfinding
     * will be used instead.
     */
    @Nullable
    default PathType getPathfindingType(BlockState state, BlockGetter context, BlockPos pos) {
        return null;
    }

    /**
     * Called when the block is directly struck by lightning.
     *
     * @param state     The state of this block.
     * @param level     The level.
     * @param pos       The position of this block.
     * @param lightning The lightning bolt that hit the block.
     */
    default void onLightningStrike(BlockState state, Level level, BlockPos pos, LightningBolt lightning) {
    }

    /**
     * Called when a neighbor is struck by lightning and the block is not insulated from the strike.
     *
     * @param state        The state of this block.
     * @param level        The level.
     * @param pos          The position of this block.
     * @param lightning    The lightning bolt that hit the block.
     * @param strikeOrigin The original strike position of the lightning bolt.
     */
    default void onLightningStrikeIndirect(BlockState state, Level level, BlockPos pos, LightningBolt lightning, BlockPos strikeOrigin) {
    }

    /**
     * Provides an array of directions lightning can travel and indirectly hit when this block is hit by lightning.
     *
     * @param state The state of this block.
     * @param level The level.
     * @param pos   The position of this block.
     * @return An array of directions that should be indirectly hit by the lightning.
     */
    default Direction[] redirectLightningStrike(BlockState state, Level level, BlockPos pos) {
        return NO_LIGHTNING_REDIRECTION_FACES;
    }
}