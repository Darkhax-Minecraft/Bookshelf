/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.tileentity;

import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.BlockFlags;

public abstract class TileEntityBasic extends TileEntity {

    private final LazyValue<ChunkPos> chunkPos;

    public TileEntityBasic (TileEntityType<?> tileEntityType) {

        super(tileEntityType);
        this.chunkPos = new LazyValue<>( () -> new ChunkPos(this.worldPosition));
    }

    @Override
    public void load (BlockState state, CompoundNBT dataTag) {

        this.deserialize(dataTag);
        super.load(state, dataTag);
    }

    @Override
    public CompoundNBT save (CompoundNBT dataTag) {

        this.serialize(dataTag);
        return super.save(dataTag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket () {

        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket (NetworkManager net, SUpdateTileEntityPacket packet) {

        super.onDataPacket(net, packet);
        this.deserialize(packet.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag () {

        return this.save(super.getUpdateTag());
    }

    /**
     * Synchronizes the server state of the tile with all clients tracking it.
     *
     * @param renderUpdate Whether or not a render update should happen as well. Only use this
     *        if you need to change the block model.
     */
    public void sync (boolean renderUpdate) {

        if (renderUpdate) {

            this.setChanged();
            final BlockState state = this.getState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, BlockFlags.DEFAULT_AND_RERENDER);
        }

        else if (this.level instanceof ServerWorld) {

            final IPacket<?> packet = this.getUpdatePacket();
            WorldUtils.sendToTracking((ServerWorld) this.level, this.getChunkPos(), packet, false);
        }
    }

    /**
     * Checks if the tile entity has a valid position.
     *
     * @return Whether or not the tile entity has a valid position.
     */
    public boolean hasPosition () {

        return this.worldPosition != null && this.worldPosition != BlockPos.ZERO;
    }

    /**
     * Checks if the tile entity has initialized, and is in a loaded chunk.
     *
     * @return Whether or not the tile entity is initialized and in a loaded chunk.
     */
    public boolean isLoaded () {

        return this.hasLevel() && this.hasPosition() && this.getLevel().hasChunkAt(this.getBlockPos());
    }

    /**
     * Gets the block state of the tile entity. Can be null if the tile is not loaded.
     *
     * @return The block state of the tile.
     */
    public BlockState getState () {

        return this.isLoaded() ? this.getLevel().getBlockState(this.worldPosition) : null;
    }

    /**
     * Gets the current chunk position of the tile. This is used to optimize chunk related
     * calculations. The chunk position is not serialized with the tile entity and is
     * initialized on demand.
     *
     * @return The ChunkPos that this tile entity is within.
     */
    public ChunkPos getChunkPos () {

        return this.chunkPos.get();
    }

    /**
     * Handles the ability to write custom NBT values to a TileEntity.
     *
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public abstract void serialize (CompoundNBT dataTag);

    /**
     * Handles the ability to read custom NBT values from the TileEntity's NBTTagCompound.
     *
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public abstract void deserialize (CompoundNBT dataTag);
}