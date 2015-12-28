package net.darkhax.bookshelf.buff;

import java.util.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import net.darkhax.bookshelf.common.EntityProperties;

public class BuffHelper {
    
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
    public static BuffEffect getEntityBuffEffect (EntityLivingBase entity, Buff buff) {
        
        for (BuffEffect effect : getEntityEffects(entity))
            if (effect.getBuff().equals(buff))
                return effect;
                
        return null;
    }
    
    /**
     * Updates a BuffEffect on an entity.
     *
     * @param world: The world the entity is in.
     * @param entity: The Entity to update on.
     * @param effect: The effect to update.
     */
    public static boolean addBuff (World world, EntityLivingBase entity, BuffEffect effect) {
        
        if (entity != null && !world.isRemote) {
            
            if (hasBuff(entity, effect.getBuff())) {
                
                EntityProperties.getProperties(entity).remove(effect, false);
            }
            EntityProperties.getProperties(entity).add(effect, false);
            
            return true;
        }
        
        return false;
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
        
        if (!entity.worldObj.isRemote) {
            List<BuffEffect> list = getEntityEffects(entity);
            
            for (Iterator<BuffEffect> iterator = list.iterator(); iterator.hasNext();) {
                
                BuffEffect effect = iterator.next();
                
                if (effect.getBuff().shouldBeCured(entity, stack))
                    iterator.remove();
            }
            
            EntityProperties.getProperties(entity).setBuffs(list).sync(true);
        }
    }
    
    /**
     * Writes a list of BuffEffect to an NBTTagCompound.
     *
     * @param buffs: A List of buffs to write to a tag.
     * @return NBTTagList: An NBTTagList containing a list of tags, each contains buff data.
     */
    public static NBTTagList writeNBT (List<BuffEffect> buffs) {
        
        NBTTagList nbtList = new NBTTagList();
        
        for (BuffEffect effect : buffs) {
            
            NBTTagCompound buffNBT = new NBTTagCompound();
            effect.writeToNBT(buffNBT);
            nbtList.appendTag(buffNBT);
        }
        
        return nbtList;
    }
    
    /**
     * Reads a list of BuffEffects from an NBTTagList.
     *
     * @param list: The NBTTagList to read from.
     * @return List<BuffEffect>: A list of BuffEffect that were stored in the NBTTagList.
     */
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