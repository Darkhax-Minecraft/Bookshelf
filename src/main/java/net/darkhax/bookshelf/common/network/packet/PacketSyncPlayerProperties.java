package net.darkhax.bookshelf.common.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.AbstractMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;

public class PacketSyncPlayerProperties extends AbstractMessage<PacketSyncPlayerProperties> {
    
    private NBTTagList buffs = new NBTTagList();
    private int entityId;
    
    public PacketSyncPlayerProperties() {
    
    }
    
    public PacketSyncPlayerProperties(EntityProperties props) {
        
        this.buffs = props.getBuffs();
        this.entityId = props.entity.getEntityId();
    }
    
    @Override
    public void fromBytes (ByteBuf buf) {
        
        this.entityId = buf.readInt();
        this.buffs = new NBTTagList();
        int size = buf.readInt();
        
        for (int i = 0; i < size; i++)
            buffs.appendTag(ByteBufUtils.readTag(buf));
    }
    
    @Override
    public void toBytes (ByteBuf buf) {
        
        buf.writeInt(entityId);
        buf.writeInt(buffs.tagCount());
        
        for (int i = 0; i < buffs.tagCount(); i++)
            ByteBufUtils.writeTag(buf, buffs.getCompoundTagAt(i));
    }
    
    @Override
    public void handleClientMessage (PacketSyncPlayerProperties message, EntityPlayer player) {
        
        EntityLivingBase entity = (EntityLivingBase) player.worldObj.getEntityByID(message.entityId);
        
        if (entity != null) {
            
            EntityProperties props = EntityProperties.getProperties(entity);
            props.setBuffs(message.buffs);
        }
    }
    
    @Override
    public void handleServerMessage (PacketSyncPlayerProperties message, EntityPlayer player) {
    
    }
}