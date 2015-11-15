package net.darkhax.bookshelf.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;

import net.minecraftforge.event.entity.living.LivingEvent;

import cpw.mods.fml.common.eventhandler.Event.HasResult;

@HasResult
public class LootingEvent extends LivingEvent {
    
    /**
     * The amount of looting to be returned.
     */
    public int lootingModifier;
    
    /**
     * An event that is fired every time the game attempts to calculate the looting modifier.
     * This event can not be canceled, instead it makes use of results. By default, the result
     * will be default, which will only return the level of looting on the held ItemStack. If
     * the result is set to deny, the modifier will be 0. If the result is set to allow, it
     * will return the lootingModifier instead.
     * 
     * @param entity: The entity to calculate the looting modifier for.
     */
    public LootingEvent(EntityLivingBase entity) {
        
        super(entity);
        this.lootingModifier = EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, entity.getHeldItem());
    }
}