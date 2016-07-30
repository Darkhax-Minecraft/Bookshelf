package net.darkhax.bookshelf.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TileEntityBasic extends TileEntity {
    
    @Override
    public void readFromNBT (NBTTagCompound dataTag) {
        
        this.readNBT(dataTag);
        super.readFromNBT(dataTag);
    }
    
    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound dataTag) {
        
        this.writeNBT(dataTag);
        return super.writeToNBT(dataTag);
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
        
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        
        return oldState.getBlock() != newState.getBlock();
    }
    
    /**
     * Handles the ability to write custom NBT values to a TileEntity.
     * 
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public void writeNBT (NBTTagCompound dataTag) {
    
    }
    
    /**
     * Handles the ability to read custom NBT values from the TileEntity's NBTTagCompound.
     * 
     * @param dataTag: The NBTTagCompound for the TileEntity.
     */
    public void readNBT (NBTTagCompound dataTag) {
    
    }
}