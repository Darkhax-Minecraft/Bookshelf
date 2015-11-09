package net.darkhax.bookshelf.potion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.packet.PacketBuffUpdate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class BuffHelper {
    
    /**
     * A BiMap which stores every single Buff effect that has been registered.
     */
    private static BiMap<String, Buff> buffMap = HashBiMap.create();
    
    /**
     * Provides access to the buff registry.
     *
     * @return BiMap<String, Buff>: The Buff map.
     */
    public static BiMap<String, Buff> getBuffMap () {
        
        return buffMap;
    }
    
    /**
     * Registers a Buff with the buffMap.
     *
     * @param buff: The Buff to register.
     */
    public static void registerBuff (Buff buff) {
        
        if (buffMap.containsKey(buff.getPotionName()))
            throw new RuntimeException("An attempt was made to register a Potion with the name of " + buff.getPotionName() + " however it is already in use. " + buffMap.get(buff.getPotionName()).getClass().getName() + " " + buff.getClass().getName());
            
        buffMap.put(buff.getPotionName(), buff);
    }
    
    /**
     * Attempts to retrieve a Buff by its name.
     *
     * @param name: The name of the buff you are looking for.
     * @return Buff: The Buff, if its name was found. If not, null.
     */
    public static Buff getBuffFromString (String name) {
        
        return (buffMap.containsKey(name)) ? buffMap.get(name) : null;
    }
    
    /**
     * Retrieves a List of BuffEffects from a living Entity.
     *
     * @param entity: The entity to retrieve the effects from.
     * @return List<BuffEffect>: A list of all BuffEffects applied to the entity passed.
     */
    public static List<BuffEffect> getEntityEffects (EntityLivingBase entity) {
        
        return EntityProperties.getProperties(entity).getBuffs();
    }
    
    /**
     * Retrieves a BuffEffect from the passed entity, that represents the passed Buff.
     *
     * @param entity: The entity to find the BuffEffect on.
     * @param buff: The Buff you are looking for.
     * @return BuffEffect: The BuffEffect you are looking for. Null if not found.
     */
    public static BuffEffect getEntitybuff (EntityLivingBase entity, Buff buff) {
        
        if (hasBuff(entity, buff))
            for (BuffEffect effect : getEntityEffects(entity))
                if (effect.getBuff().equals(buff))
                    return effect;
                    
        return null;
    }
    
    /**
     * Applies a Buff to an Entity.
     *
     * @param entity: The entity to give the buff to.
     * @param effect: The effect to give to the entity.
     * @return boolean: Whether or not it was applied successfully.
     */
    public static boolean applyToEntity (EntityLivingBase entity, BuffEffect effect) {
        
        if (entity != null) {
            
            List<BuffEffect> list = getEntityEffects(entity);
            
            if (hasBuff(entity, effect.getBuff())) {
                
                for (Iterator<BuffEffect> iterator = list.iterator(); iterator.hasNext();) {
                    BuffEffect current = iterator.next();
                    
                    if (current.getBuff().equals(effect.getBuff()))
                        iterator.remove();
                }
                
            }
            list.add(effect);
            EntityProperties.getProperties(entity).setBuffs(list);
            
            if (!entity.worldObj.isRemote)
                EntityProperties.getProperties(entity).sync();
                
            return true;
        }
        
        return false;
    }
    
    /**
     * Updates a BuffEffect on an entity.
     *
     * @param world: The world the entity is in.
     * @param entity: The Entity to update on.
     * @param effect: The effect to update.
     */
    public static void updateBuff (World world, EntityLivingBase entity, BuffEffect effect) {
        
        if (entity != null && hasBuff(entity, effect.getBuff())) {
            
            List<BuffEffect> list = getEntityEffects(entity);
            
            for (Iterator<BuffEffect> iterator = list.iterator(); iterator.hasNext();) {
                BuffEffect buffE = iterator.next();
                
                if (buffE != null) {
                    if (buffE.getBuff().equals(effect.getBuff())) {
                        iterator.remove();
                        
                    }
                }
            }
            if (!(effect.duration <= 0)) {
                list.add(effect);
            }
            
            EntityProperties.getProperties(entity).setBuffs(list);
            
            if (!world.isRemote)
                Bookshelf.network.sendToAllAround(new PacketBuffUpdate(entity, effect), new TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 128D));
                
        }
    }
    
    /**
     * A check to see if an Entity has a certain buff.
     *
     * @param entity: The entity to check.
     * @param buff: The Buff to check for.
     * @return boolean: Whether or not the entity has the Buff.
     */
    public static boolean hasBuff (EntityLivingBase entity, Buff buff) {
        
        for (BuffEffect effect : getEntityEffects(entity))
            if (effect.getBuff().equals(buff))
                return true;
                
        return false;
    }
    
    /**
     * Attempts to cure all buff effects using the ItemStack passed.
     *
     * @param entity: The entity trying to be cured.
     * @param stack: The cure ItemStack being used.
     */
    public static void cureBuffs (EntityLivingBase entity, ItemStack stack) {
        
        List<BuffEffect> buffs = getEntityEffects(entity);
        
        for (Iterator<BuffEffect> iterator = buffs.iterator(); iterator.hasNext();) {
            BuffEffect effect = iterator.next();
            
            if (effect != null)
                if (effect.getBuff().shouldBeCured(entity, stack))
                    iterator.remove();
                    
        }
        
        EntityProperties properties = EntityProperties.getProperties(entity);
        properties.setBuffs(buffs);
        properties.sync();
    }
    
    public static NBTTagList writeNBT (List<BuffEffect> buffs) {
        
        NBTTagList nbtList = new NBTTagList();
        
        for (BuffEffect effect : buffs) {
            NBTTagCompound buffNBT = new NBTTagCompound();
            effect.writeToNBT(buffNBT);
            nbtList.appendTag(buffNBT);
        }
        return nbtList;
    }
    
    public static List<BuffEffect> readNBT (NBTTagList list) {
        
        List<BuffEffect> effects = new ArrayList<BuffEffect>();
        
        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                BuffEffect buff = BuffEffect.readFromNBT(tag);
                effects.add(buff);
            }
        }
        
        return effects;
    }
}