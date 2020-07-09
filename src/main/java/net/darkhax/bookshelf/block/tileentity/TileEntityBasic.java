/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.BlockFlags;

public abstract class TileEntityBasic extends TileEntity {
    
    public TileEntityBasic(TileEntityType<?> tileEntityType) {
        
        super(tileEntityType);
    }
    
    @Override
    public void read (BlockState state, CompoundNBT dataTag) {
        
        this.deserialize(dataTag);
        super.read(state, dataTag);
    }
    
    @Override
    public CompoundNBT write (CompoundNBT dataTag) {
        
        this.serialize(dataTag);
        return super.write(dataTag);
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket () {
        
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket (NetworkManager net, SUpdateTileEntityPacket packet) {
        
        super.onDataPacket(net, packet);
        this.deserialize(packet.getNbtCompound());
    }
    
    @Override
    public CompoundNBT getUpdateTag () {
        
        return this.write(super.getUpdateTag());
    }
    
    /**
     * Marks the tile entity for a block update. This will mark the block as dirty, sync the
     * NBT from server to client, and cause a block update. Keep in mind that this should be
     * used only when necessary to prevent lag!
     */
    public void sync () {
        
        this.markDirty();
        final BlockState state = this.getState();
        this.getWorld().notifyBlockUpdate(this.pos, state, state, BlockFlags.DEFAULT_AND_RERENDER);
    }
    
    /**
     * Checks if the tile entity has a valid position.
     *
     * @return Whether or not the tile entity has a valid position.
     */
    public boolean hasPosition () {
        
        return this.pos != null && this.pos != BlockPos.ZERO;
    }
    
    /**
     * Checks if the tile entity has initialized, and is in a loaded chunk.
     *
     * @return Whether or not the tile entity is initialized and in a loaded chunk.
     */
    public boolean isLoaded () {
        
        return this.hasWorld() && this.hasPosition() && this.getWorld().isBlockLoaded(this.getPos());
    }
    
    /**
     * Gets the block state of the tile entity. Can be null if the tile is not loaded.
     *
     * @return The block state of the tile.
     */
    public BlockState getState () {
        
        return this.isLoaded() ? this.getWorld().getBlockState(this.pos) : null;
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