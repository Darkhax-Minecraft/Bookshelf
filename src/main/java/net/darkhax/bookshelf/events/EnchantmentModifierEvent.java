/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.events;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EnchantmentModifierEvent extends Event {
    
    /**
     * The enchantment to calculate levels for.
     */
    private final Enchantment enchantment;
    
    /**
     * The entity using the enchantment.
     */
    private final EntityLivingBase entity;
    
    /**
     * The amount of levels to say the user has of the given enchantment.
     */
    private int levels;
    
    /**
     * This event is fired every time
     * {@link EnchantmentHelper#getMaxEnchantmentLevel(Enchantment, EntityLivingBase)} would be
     * called. This allows for enchantment specific effects to be applied, as well as
     * increasing the effect of an enchantment level, without actually giving the player an
     * item with that enchantment. For example, a ring item could be put in the inventory,
     * which would give the player +1 levels to the frost walker enchantment. For the modified
     * level to be applied, this event must be canceled. Due to how decentralised the
     * enchantment code is, not all enchantments will trigger this event. Below is a list of
     * enchantments that are known to work.
     *
     * Power, Punch, Flame, Frost Walker, Aqua Affinity, Fire Aspect, Respiration, Depth
     * Strider, Efficiency, Luck of The Sea, Lure, Looting, Knockback, Resistance, Fire
     * Protection.
     *
     * @param enchantment The enchantment that the level is being calculated for.
     * @param entity The entity that is using the enchantment.
     */
    public EnchantmentModifierEvent(Enchantment enchantment, EntityLivingBase entity) {
        
        final Iterable<ItemStack> iterable = enchantment.getEntityEquipment(entity);
        
        if (iterable != null) {
            for (final ItemStack stack : iterable) {
                this.levels += EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
            }
        }
        
        this.enchantment = enchantment;
        this.entity = entity;
    }
    
    /**
     * Gets the enchantment being calculated.
     *
     * @return The enchantment that is being calculated.
     */
    public Enchantment getEnchantment () {
        
        return this.enchantment;
    }
    
    /**
     * Gets the entity using the enchantment.
     *
     * @return The entity using the enchantment.
     */
    public EntityLivingBase getEntity () {
        
        return this.entity;
    }
    
    /**
     * Gets the amount of levels to return for the event. Can be no less than 0.
     *
     * @return The amount of levels to return.
     */
    public int getLevels () {
        
        return Math.max(this.levels, 0);
    }
    
    /**
     * Sets the amount of levels to return for the event.
     *
     * @param amount The amount of levels the event should return.
     */
    public void setLevels (int amount) {
        
        this.levels = amount;
    }
}