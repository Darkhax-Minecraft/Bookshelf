/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

public abstract class BlockEntityBase extends BlockEntity {

    /**
     * Lazily gets the chunk position of the block entity. Used by {@link #getChunkPos()}.
     */
    private final Lazy<ChunkPos> chunkPos = Lazy.of( () -> new ChunkPos(this.getBlockPos()));

    public BlockEntityBase (BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }
    
    /**
     * Checks if the block entity has a valid world position.
     * @return Whether or not the block has a valid position.
     */
    public boolean hasPosition () {

        return this.worldPosition != null && this.worldPosition != BlockPos.ZERO;
    }
    
    /**
     * Checks if the block entity is initialized and loaded. Makes sure it has a world, position, and is in a loaded chunk.
     * @return Whether or not the block entity is loaded.
     */
    public boolean isLoaded () {

        return this.hasLevel() && this.hasPosition() && this.getLevel().isLoaded(this.getBlockPos());
    }
    
    /**
     * Gets the current block state at the position of the block entity. This will lookup the state rather than using the cached value.
     * @return The block state that is currently at the position of the block entity.
     */
    public BlockState getCurrentState () {

        return this.isLoaded() ? this.getLevel().getBlockState(this.worldPosition) : null;
    }
    
    /**
     * Gets the chunk position of the block entity. This will get the cached chunk position and is not serialized.
     * @return The block entities chunk position.
     */
    public ChunkPos getChunkPos () {

        return this.chunkPos.get();
    }
}