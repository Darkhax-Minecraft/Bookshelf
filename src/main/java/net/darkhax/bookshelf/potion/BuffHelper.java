package net.darkhax.bookshelf.potion;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.packet.PacketBuffUpdate;
import net.darkhax.bookshelf.common.network.packet.PacketSyncPlayerProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BuffHelper {

    private static BiMap<String, Buff> buffMap = HashBiMap.create();

    public static void registerBuff (Buff buff) {

        buffMap.put(buff.getPotionName(), buff);
    }

    public static Buff getBuffFromString (String name) {

        try {
            return buffMap.get(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<BuffEffect> getEntityEffects (EntityLivingBase entity) {

        List<BuffEffect> effects = new ArrayList<BuffEffect>();
        NBTTagList list = EntityProperties.getProperties(entity).getBuffs();
        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                BuffEffect buff = BuffEffect.readFromNBT(tag);
                effects.add(buff);
            }
        }
        return effects;
    }

    public static BuffEffect getEntitybuff (EntityLivingBase entity, Buff buff) {

        if (hasBuff(entity, buff))
            for (BuffEffect effect : getEntityEffects(entity)) {
                if (effect.getBuff().equals(buff)) {
                    return effect;
                }
            }

        return null;
    }

    public static boolean applyToEntity (EntityLivingBase entity, BuffEffect effect) {

        if (entity != null) {
            EntityProperties entityProperties = EntityProperties.getProperties(entity);
            NBTTagList list = entityProperties.getBuffs();
            if (hasBuff(entity, effect.getBuff())) {
                int toRemove = -1;
                for (int i = 0; i < getEntityEffects(entity).size(); i++) {
                    BuffEffect current = getEntityEffects(entity).get(i);
                    if (current.getBuff().equals(effect.getBuff())) {
                        toRemove = i;
                    }
                }
                list.removeTag(toRemove);
            }
            NBTTagCompound buff = new NBTTagCompound();
            effect.writeToNBT(buff);
            list.appendTag(buff);
            if (!entity.worldObj.isRemote) {
                Bookshelf.network.sendToAllAround(new PacketSyncPlayerProperties(entityProperties), new TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 128D));
            }
            EntityProperties.getProperties(entity).setBuffs(list);
            return true;
        }
        return false;
    }

    public static boolean updateBuff (World world, EntityLivingBase entity, BuffEffect effect) {

        if (entity != null && hasBuff(entity, effect.getBuff())) {
            NBTTagList list = EntityProperties.getProperties(entity).getBuffs();
            int count = 0;
            boolean foundBuff = false;
            for (BuffEffect buffE : getEntityEffects(entity)) {
                if (!foundBuff) {
                    if (!buffE.getBuff().equals(effect.getBuff())) {
                        count++;
                    }
                    else {
                        foundBuff = true;
                    }

                }

            }
            NBTTagCompound buff = new NBTTagCompound();
            effect.writeToNBT(buff);
            if (!(effect.getDuration() <= 0)) {
                list.func_150304_a(count, buff);
            }
            else {
                list.removeTag(count);
            }
            if (!world.isRemote) {
                Bookshelf.network.sendToAllAround(new PacketBuffUpdate(entity, effect), new TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 128D));
            }
            EntityProperties.getProperties(entity).setBuffs(list);
        }
        return false;
    }

    public static BiMap<String, Buff> getBuffMap () {

        return buffMap;
    }

    public static boolean hasBuff (EntityLivingBase entity, Buff buff) {

        for (BuffEffect effect : getEntityEffects(entity)) {
            if (effect.getBuff().equals(buff)) {
                return true;
            }
        }

        return false;
    }

}
