package net.darkhax.bookshelf.common.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.darkhax.bookshelf.common.network.AbstractMessage;
import net.darkhax.bookshelf.potion.BuffEffect;
import net.darkhax.bookshelf.potion.BuffHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PacketBuffUpdate extends AbstractMessage<PacketBuffUpdate> {
    public int entityID;
    public BuffEffect effect;

    public PacketBuffUpdate () {

    }

    public PacketBuffUpdate (Entity entity, BuffEffect effect) {

        this.entityID = entity.getEntityId();
        this.effect = effect;
    }

    @Override
    public void handleClientMessage (PacketBuffUpdate message, EntityPlayer player) {

        EntityLivingBase entity = (EntityLivingBase) player.worldObj.getEntityByID(message.entityID);
        BuffHelper.updateBuff(entity.worldObj, entity, message.effect);
    }

    @Override
    public void handleServerMessage (PacketBuffUpdate message, EntityPlayer player) {

    }

    @Override
    public void fromBytes (ByteBuf buf) {

        entityID = buf.readInt();
        effect = new BuffEffect(BuffHelper.getBuffFromString(ByteBufUtils.readUTF8String(buf)), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes (ByteBuf buf) {

        buf.writeInt(entityID);
        ByteBufUtils.writeUTF8String(buf, effect.getBuff().getPotionName());
        buf.writeInt(effect.getDuration());
        buf.writeInt(effect.getPower());
    }
}
