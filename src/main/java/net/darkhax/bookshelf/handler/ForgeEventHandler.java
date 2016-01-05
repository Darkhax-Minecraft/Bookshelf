package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraftforge.event.AnvilUpdateEvent;
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
