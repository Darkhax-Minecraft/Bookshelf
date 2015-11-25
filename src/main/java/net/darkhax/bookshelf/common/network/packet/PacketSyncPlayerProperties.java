package net.darkhax.bookshelf.common.network.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;

import net.darkhax.bookshelf.buff.BuffEffect;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.AbstractMessage;

import io.netty.buffer.ByteBuf;

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
        
        if (player != null && player.worldObj != null) {
            
            Entity entity = player.worldObj.getEntityByID(message.entityId);
            
            if (entity != null && entity instanceof EntityLivingBase)
                EntityProperties.getProperties((EntityLivingBase) entity).setBuffs(message.buffs);
        }
    }
    
    @Override
    public void handleServerMessage (PacketSyncPlayerProperties message, EntityPlayer player) {
    
    }
}