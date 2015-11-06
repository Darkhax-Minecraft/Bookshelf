package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.event.CreativeTabEvent;
import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.SkullUtils;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    
    @SubscribeEvent
    public void post (CreativeTabEvent.Post event) {
        
        if (event.tab == CreativeTabs.tabDecorations)
            for (ItemStack stack : SkullUtils.getMHFSkulls())
                event.itemList.add(stack);
    }
    
    @SubscribeEvent
    public void onEntityUpdate (LivingUpdateEvent event) {
        
        if (!ASMHelper.isASMEnabled)
            Constants.LOG.warn("The ASM has not been initialized, there is an error with your setup!");
            
        else if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                armor.onHorseUpdate(horse, customArmor);
            }
        }
    }
    
    @SubscribeEvent
    public void onEntityHurt (LivingHurtEvent event) {
        
        if (!ASMHelper.isASMEnabled)
            Constants.LOG.warn("The ASM has not been initialized, there is an error with your setup!");
            
        else if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                event.setCanceled(armor.onHorseDamaged(horse, customArmor, event.source, event.ammount));
            }
        }
    }
}
