package net.darkhax.bookshelf.util;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;

public class VirtualMethods {
    
    /**
     * Allows for the retrieval of a custom armor ItemStack from an instance of EntityHorse.
     * This method does not make use of the new method injected into EntityHorse as that is for
     * internal use only. Results will be the same however.
     * 
     * @param horse: An instance of the EntityHorse you are grabbing the armor item of.
     * @return ItemStack: The ItemStack in the horses custom armor data. This ItemStack may be
     *         null, and won't always be an instance of our implementation of ItemHorseArmor.
     */
    public static ItemStack getCustomHorseArmor (EntityHorse horse) {
    
        return horse.getDataWatcher().getWatchableObjectItemStack(23);
    }
    
    /**
     * Sets the custom armor ItemStack on an instance of EntityHorse. The method does not make
     * use of the new method injected into EntityHorse as that is for internal use only,
     * however this method functions the same way.
     * 
     * @param horse: An instance of the EntityHorse to set the new item to.
     * @param stack: An ItemStack to set to the EntityHorse's custom armor data.
     */
    public static void setCustomHorseArmor (EntityHorse horse, ItemStack stack) {
    
        horse.getDataWatcher().updateObject(23, stack);
    }
}
