package net.darkhax.bookshelf.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingEvent;

import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class PotionCuredEvent extends LivingEvent {
    
    /**
     * The ItemStack being used to cure the effects.
     */
    public ItemStack stack;
    
    /**
     * Constructs a new PotionCuredEvent. This event is triggered whenever a cure item has been
     * used. If the event is cancelled, the cure process will fail.
     * 
     * @param living: The entity being cured.
     * @param stack: The cure being used.
     */
    public PotionCuredEvent(EntityLivingBase living, ItemStack stack) {
        
        super(living);
        this.stack = stack;
    }
}