package net.darkhax.bookshelf.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.darkhax.bookshelf.util.Constants;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentListExpansionHandler {
    
    Field enchantmentsList = null;
    Field modifiers = null;
    
    /**
     * When constructed, Bookshelf will try to expand the array of enchantments from 256 to
     * 4096. This will allow for another 3840 possible enchantments to be added to the game. If
     * the enchantment list is already at the size of 4096 it will fail. This should be safe,
     * however there may be some systems which are not prepared to handle 4096 enchantment
     * spaces.
     */
    public EnchantmentListExpansionHandler() {
        
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
}
