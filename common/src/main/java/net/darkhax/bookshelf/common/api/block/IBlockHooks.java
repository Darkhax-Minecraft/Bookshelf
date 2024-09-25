package net.darkhax.bookshelf.common.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public interface IBlockHooks {

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
}