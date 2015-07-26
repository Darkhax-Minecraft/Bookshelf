package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.util.Utilities;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderingHandler {
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Pre event) {
    
        triggerRenderHook(event, 0);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Post event) {
    
        triggerRenderHook(event, 1);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Specials.Pre event) {
    
        triggerRenderHook(event, 2);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Specials.Post event) {
    
        triggerRenderHook(event, 3);
    }
    
    /**
     * Attempts to trigger the rendering hook for a horses custom armor item. Will only work if
     * the entity is a horse, and is wearing ItemHorseArmor
     * 
     * @param event: The event containing all the data we need to check if we should call the
     *            hook, and provide the hook with.
     * @param flag: An integer which represents the rendering stage. 0:pre 1:post 2:special-pre
     *            3:special-post
     */
    public void triggerRenderHook (RenderLivingEvent event, int flag) {
    
        if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                armor.onArmorRendering(horse, customArmor, event.renderer, event.x, event.y, event.z, flag);
            }
        }
    }
}
