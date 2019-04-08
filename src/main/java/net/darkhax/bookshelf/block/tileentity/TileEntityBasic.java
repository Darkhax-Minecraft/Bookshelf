/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityBasic extends TileEntity {
    
	public TileEntityBasic(TileEntityType<?> tileEntityType) {
		
		super(tileEntityType);
	}

	@Override
    public void read (NBTTagCompound dataTag) {
        
        this.readNBT(dataTag);
        super.read(dataTag);
    }
    
    @Override
    public NBTTagCompound write (NBTTagCompound dataTag) {
        
        this.writeNBT(dataTag);
        return super.write(dataTag);
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket () {
        
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity packet) {
        
        super.onDataPacket(net, packet);
        this.readNBT(packet.getNbtCompound());
    }
    
    @Override
    public NBTTagCompound getUpdateTag () {
        
        return this.write(new NBTTagCompound());
    }
    
    /**
     * Marks the tile entity for a block update. This will mark the block as dirty, sync the
     * NBT from server to client, and cause a block update. Keep in mind that this should be
     * used only when necessary to prevent lag!
     */
    public void sync () {
        
        this.markDirty();
        
        if (this.isLoaded()) {
            
            final IBlockState state = this.getState();
            this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
        }
    }
    
    /**
     * Checks if the tile entity has a valid position.
     *
     * @return Whether or not the tile entity has a valid position.
     */
    public boolean hasPosition () {
        
        return this.pos != null && this.pos != BlockPos.ORIGIN;
    }
    
    /**
     * Checks if the tile entity has initialized, and is in a loaded chunk.
     *
     * @return Whether or not the tile entity is initialized and in a loaded chunk.
     */
    public boolean isLoaded () {
        
        return this.hasWorld() && this.hasPosition() ? this.getWorld().isBlockLoaded(this.getPos()) : false;
    }
    
    /**
     * Gets the block state of the tile entity. Can be null if the tile is not loaded.
     *
     * @return The block state of the tile.
     */
    public IBlockState getState () {
        
        return this.isLoaded() ? this.getWorld().getBlockState(this.pos) : null;
    }
    
    /**
     * Handles the ability to write custom NBT values to a TileEntity.
     *
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public abstract void writeNBT (NBTTagCompound dataTag);
    
    /**
     * Handles the ability to read custom NBT values from the TileEntity's NBTTagCompound.
     *
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public abstract void readNBT (NBTTagCompound dataTag);
}