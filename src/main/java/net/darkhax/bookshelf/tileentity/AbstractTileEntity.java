package net.darkhax.bookshelf.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntity extends TileEntity {
    
    @Override
    public Packet getDescriptionPacket () {
        
        NBTTagCompound nbtTag = new NBTTagCompound();
        saveClientDataToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(pos, 0, nbtTag);
    }
    
    @Override
    public void onDataPacket (NetworkManager manager, S35PacketUpdateTileEntity packet) {
        
        loadClientDataFromNBT(packet.getNbtCompound());
    }
    
    @Override
    public void readFromNBT (NBTTagCompound nbtTag) {
        
        super.readFromNBT(nbtTag);
        loadFromNBT(nbtTag);
    }
    
    @Override
    public void writeToNBT (NBTTagCompound nbtTag) {
        
        super.writeToNBT(nbtTag);
        saveToNBT(nbtTag);
    }
    
    /**
     * Load NBT data from a tag. This can be called on a dedicated server, or an integrated
     * server.
     *
     * @param nbtTag: The NBT tag to read the data from.
     */
    public abstract void loadFromNBT (NBTTagCompound nbtTag);
    
    /**
     * Write NBT data to a tag. This can be called on a dedicated server, or an integrated
     * server.
     *
     * @param nbtTag: The NBT tag to write data to.
     */
    public abstract void saveToNBT (NBTTagCompound nbtTag);
    
    /**
     * Load NBT data from a tag. This can be called on a client.
     *
     * @param nbtTag: The NBT tag to read the data from.
     */
    public abstract void loadClientDataFromNBT (NBTTagCompound nbtTag);
    
    /**
     * Write NBT data to a tag. This can be called on a client.
     *
     * @param nbtTag: The NBT tag to write data to.
     */
    public abstract void saveClientDataToNBT (NBTTagCompound nbtTag);
}
