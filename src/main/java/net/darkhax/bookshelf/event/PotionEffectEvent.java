package net.darkhax.bookshelf.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionEffectEvent extends Event {
    
    /**
     * The potion effect getting used.
     */
    public final PotionEffect potion;
    
    /**
     * The entity with the potion effect.
     */
    public final EntityLivingBase entity;
    
    /**
     * Constructs a new PotionEffectEvent. This event itself is never called, only the 3 child
     * events.
     *
     * @param potion: The potion effect getting used.
     * @param entity: The entity with the potion effect.
     */
    public PotionEffectEvent(PotionEffect potion, EntityLivingBase entity) {
        
        this.potion = potion;
        this.entity = entity;
    }
    
    public static class PotionEffectStartEvent extends PotionEffectEvent {
        
        /**
         * Constructs a new PotionEffectStartEvent. This event is triggered when a potion
         * effect gets used on a living entity.
         *
         * @param potion: The potion effect getting used.
         * @param entity: The entity with the potion effect.
         */
        public PotionEffectStartEvent(PotionEffect potion, EntityLivingBase entity) {
            
            super(potion, entity);
        }
    }
    
    public static class PotionEffectChangeEvent extends PotionEffectEvent {
        
        /**
         * Constructs a new PotionEffectChangeEvent. This event is triggered when a potion
         * effect gets updated.
         *
         * @param potion: The potion effect getting used.
         * @param entity: The entity with the potion effect.
         */
        public PotionEffectChangeEvent(PotionEffect potion, EntityLivingBase entity) {
            
            super(potion, entity);
        }
    }
    
    public static class PotionEffectFinishEvent extends PotionEffectEvent {
        
        /**
         * Constructs a new PotionEffectFinishEvent. This event is triggered when a potion
         * effect gets worn out.
         *
         * @param potion: The potion effect getting used.
         * @param entity: The entity with the potion effect.
         */
        public PotionEffectFinishEvent(PotionEffect potion, EntityLivingBase entity) {
            
            super(potion, entity);
        }
    }
}
