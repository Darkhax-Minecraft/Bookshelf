/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public final class PotionUtils {
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private PotionUtils() {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Checks if a potion if beneficial. This is a wrapper for the access transformer. The
     * vanilla method is client side only.
     *
     * @param potion The potion to check beneficialness of.
     * @return Whether or not the potion was beneficial.
     */
    public static boolean isBeneficial (Potion potion) {
        
        return potion.beneficial;
    }
    
    /**
     * Deincrements the duration of a PotionEffect. This is a wrapper for the access
     * transformer.
     *
     * @param potionEffect The effect to deincrement.
     */
    public static void deincrementDuration (PotionEffect potionEffect) {
        
        potionEffect.deincrementDuration();
    }
}
