package net.darkhax.bookshelf.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

import net.darkhax.bookshelf.lib.Constants;

public class ExpansionHandler {
    
    /**
     * Attempts to expand the array of Enchantments from 256 IDs to 4096. This allows for
     * another 3840 possible enchantments.
     */
    public static void expandEnchantmentList () {
        
        Field enchantmentsList = null;
        Field modifiers = null;
        
        if (Enchantment.enchantmentsList.length < 4096) {
            
            Constants.LOG.info("Attempting to expand Enchantment List to 4096 spaces.");
            
            try {
                
                enchantmentsList = ReflectionHelper.findField(Enchantment.class, "b", "field_77331_b", "enchantmentsList");
                modifiers = Field.class.getDeclaredField("modifiers");
                
                if (enchantmentsList != null) {
                    
                    modifiers.setAccessible(true);
                    modifiers.setInt(enchantmentsList, enchantmentsList.getModifiers() & ~Modifier.FINAL);
                    
                    Enchantment[] existing = (Enchantment[]) enchantmentsList.get(null);
                    Enchantment[] expanded = new Enchantment[4096];
                    System.arraycopy(existing, 0, expanded, 0, existing.length);
                    enchantmentsList.set(existing, expanded);
                    Constants.LOG.info("The Enchantment List has successfully been expanded to 4096 spaces.");
                    return;
                }
            }
            
            catch (Exception e) {
                
                Constants.LOG.info("An exception occured during the expansion process. Please report this issue to the Bookshelf team! Make sure to provide a copy of this log!");
                e.printStackTrace();
            }
            
            Constants.LOG.info("The Enchantment List was not expanded.");
        }
    }
    
    /**
     * Expands the potion ID range from 32 to 256, allowing for 224 more potion ids to be
     * registered. Please note, you should be using the Buff system over the Potion system.
     */
    public static void expandPotionArray () {
        
        Field potionTypes = null;
        Field modifiers = null;
        
        if (Potion.potionTypes.length < 256) {
            
            Constants.LOG.info("Attempting to expand Potion Array to 256 spaces.");
            
            try {
                
                potionTypes = ReflectionHelper.findField(Potion.class, "a", "field_76425_a", "potionTypes");
                modifiers = Field.class.getDeclaredField("modifiers");
                
                if (potionTypes != null) {
                    
                    modifiers.setAccessible(true);
                    modifiers.setInt(potionTypes, potionTypes.getModifiers() & ~Modifier.FINAL);
                    
                    Potion[] existing = (Potion[]) potionTypes.get(null);
                    Potion[] expanded = new Potion[256];
                    System.arraycopy(existing, 0, expanded, 0, existing.length);
                    potionTypes.set(existing, expanded);
                    Constants.LOG.info("The Potion array has successfully been expanded to 256 spaces.");
                    return;
                }
            }
            
            catch (Exception e) {
                
                Constants.LOG.info("An exception occured during the expansion process. Please report this issue to the Bookshelf team! Make sure to provide a copy of this log!");
                e.printStackTrace();
            }
            
            Constants.LOG.info("The Potion array was not expanded.");
        }
    }
}