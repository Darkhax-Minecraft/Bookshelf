package net.darkhax.bookshelf.common.network.packet;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.AbstractMessage;
import net.darkhax.bookshelf.potion.BuffEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketSyncPlayerProperties extends AbstractMessage<PacketSyncPlayerProperties> {
    
    private List<BuffEffect> buffs = new ArrayList<BuffEffect>();
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
        int size = buf.readInt();
        
        this.buffs = new ArrayList<BuffEffect>();
        
        for (int i = 0; i < size; i++)
            buffs.add(BuffEffect.readFromNBT(ByteBufUtils.readTag(buf)));
    }
    
    @Override
    public void toBytes (ByteBuf buf) {
        
        buf.writeInt(entityId);
        buf.writeInt(buffs.size());
        
        for (BuffEffect buff : buffs) {
            NBTTagCompound compound = new NBTTagCompound();
            buff.writeToNBT(compound);
            ByteBufUtils.writeTag(buf, compound);
        }
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