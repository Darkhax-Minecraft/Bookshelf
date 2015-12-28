package net.darkhax.bookshelf.common.network.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.network.ByteBufUtils;

import net.darkhax.bookshelf.buff.BuffEffect;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.AbstractMessage;

import io.netty.buffer.ByteBuf;

public class PacketRemovePlayerProperties extends AbstractMessage<PacketRemovePlayerProperties> {

    private BuffEffect buffs = null;
    private int entityId;

    public PacketRemovePlayerProperties() {

    }

    public PacketRemovePlayerProperties(EntityLivingBase entity, BuffEffect buff) {

        this.buffs = buff;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void fromBytes (ByteBuf buf) {

        this.entityId = buf.readInt();

        this.buffs = BuffEffect.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes (ByteBuf buf) {

        buf.writeInt(entityId);

        NBTTagCompound compound = new NBTTagCompound();
        buffs.writeToNBT(compound);
        ByteBufUtils.writeTag(buf, compound);
    }

    @Override
    public void handleClientMessage (PacketRemovePlayerProperties message, EntityPlayer player) {

        if (player != null && player.worldObj != null) {

            Entity entity = player.worldObj.getEntityByID(message.entityId);

            if (entity != null && entity instanceof EntityLivingBase)
                EntityProperties.getProperties((EntityLivingBase) entity).remove(message.buffs);
        }
    }

    @Override
    public void handleServerMessage (PacketRemovePlayerProperties message, EntityPlayer player) {

    }
}