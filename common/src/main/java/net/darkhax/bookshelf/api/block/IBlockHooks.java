package net.darkhax.bookshelf.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;

public interface IBlockHooks {

    /**
     * Gets the pathfinding type that should be used for the block.
     *
     * @param state The current state of the block.
     * @param level The level that the block is within.
     * @param pos   The position of the block.
     * @return The pathfinding type that should be used for the block. When null is returned the vanilla behavior for
     * determining pathfinding types will be used.
     */
    @Nullable
    default BlockPathTypes getPathfindingType(BlockState state, BlockGetter level, BlockPos pos) {
        return null;
    }
}