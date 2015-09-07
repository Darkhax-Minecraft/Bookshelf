package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.util.Constants;
import net.darkhax.bookshelf.util.Utilities;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    
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
}
