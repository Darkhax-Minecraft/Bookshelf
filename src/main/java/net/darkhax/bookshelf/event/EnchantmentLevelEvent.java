package net.darkhax.bookshelf.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;;;

/**
 * This event is fired whenever a specific enchantment modifier is calculated. This can be used
 * to make the player seem to have more levels of a specific enchantment than they actually do.
 * This event uses results. The result must be set to 'ALLOW' for the new value to be set. If
 * the result is 'Deny' it will give 0 levels.
 */
@HasResult
public class EnchantmentLevelEvent extends LivingEvent {
    
    /**
     * The amount of levels to be returned
     */
    public int levels;
    
    public EnchantmentLevelEvent(EntityLivingBase entity, int enchant, ItemStack stack) {
        
        super(entity);
        this.levels = EnchantmentHelper.getEnchantmentLevel(enchant, stack);
    }
    
    public EnchantmentLevelEvent(EntityLivingBase entity, int enchant, ItemStack[] armor) {
        
        super(entity);
        this.levels = EnchantmentHelper.getMaxEnchantmentLevel(enchant, armor);
    }
    
    public static class KnockbackEvent extends EnchantmentLevelEvent {
        
        public KnockbackEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.knockback.effectId, entity.getHeldItem());
        }
    }
    
    public static class FireAspectEvent extends EnchantmentLevelEvent {
        
        public FireAspectEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.fireAspect.effectId, entity.getHeldItem());
        }
    }
    
    public static class RespirationEvent extends EnchantmentLevelEvent {
        
        public RespirationEvent(Entity entity) {
            
            super((EntityLivingBase) entity, Enchantment.respiration.effectId, entity.getInventory());
        }
    }
    
    public static class DepthStriderEvent extends EnchantmentLevelEvent {
        
        public DepthStriderEvent(Entity entity) {
            
            super((EntityLivingBase) entity, Enchantment.depthStrider.effectId, entity.getInventory());
        }
    }
    
    public static class EfficiencyEvent extends EnchantmentLevelEvent {
        
        public EfficiencyEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.efficiency.effectId, entity.getHeldItem());
        }
    }
    
    // TODO: Silk Touch
    
    public static class FortuneEvent extends EnchantmentLevelEvent {
        
        public FortuneEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.fortune.effectId, entity.getHeldItem());
        }
    }
    
    public static class LuckOfSeaEvent extends EnchantmentLevelEvent {
        
        public LuckOfSeaEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.luckOfTheSea.effectId, entity.getHeldItem());
        }
    }
    
    public static class LureEvent extends EnchantmentLevelEvent {
        
        public LureEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.lure.effectId, entity.getHeldItem());
        }
    }
    
    public static class LootingEvent extends EnchantmentLevelEvent {
        
        public LootingEvent(EntityLivingBase entity) {
            
            super(entity, Enchantment.looting.effectId, entity.getHeldItem());
        }
    }
}