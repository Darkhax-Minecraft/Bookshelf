package net.darkhax.bookshelf.handler;

import java.util.Iterator;
import java.util.List;

import javax.swing.text.Utilities;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    
    @SubscribeEvent
    public void onAnvilUsed (AnvilUpdateEvent event) {
        
        for (BookshelfRegistry.AnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes()) {
            
            if (recipe != null && ItemStackUtils.isValidStack(recipe.output) && ItemStackUtils.areStacksSimilarWithSize(event.left, recipe.inputLeft) && ItemStackUtils.areStacksSimilarWithSize(event.right, recipe.inputRight)) {
                
                event.cost = recipe.getExperienceCost(event.left, event.right, event.name);
                event.materialCost = recipe.getMaterialCost(event.left, event.right, event.name);
                
                if (recipe.nameTaxt != null && !recipe.nameTaxt.isEmpty()) {
                    
                    if (recipe.nameTaxt.equalsIgnoreCase(event.name))
                        event.output = recipe.getOutput(event.left, event.right, event.name);
                        
                    return;
                }
                
                event.output = recipe.getOutput(event.left, event.right, event.name);
                return;
            }
        }
    }
}
