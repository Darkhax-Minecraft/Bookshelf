package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    
    @SubscribeEvent
    public void onAnvilUsed (AnvilUpdateEvent event) {
        
        for (BookshelfRegistry.AnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes()) {
            
            if (recipe != null && ItemStackUtils.isValidStack(recipe.output) && ItemStackUtils.areStacksSimilarWithSize(event.getLeft(), recipe.inputLeft) && ItemStackUtils.areStacksSimilarWithSize(event.getRight(), recipe.inputRight)) {
                
                event.setCost(recipe.getExperienceCost(event.getLeft(), event.getRight(), event.getName()));
                event.setMaterialCost(recipe.getMaterialCost(event.getLeft(), event.getRight(), event.getName()));
                
                if (recipe.nameTaxt != null && !recipe.nameTaxt.isEmpty()) {
                    
                    if (recipe.nameTaxt.equalsIgnoreCase(event.getName()))
                        event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                        
                    return;
                }
                
                event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                return;
            }
        }
    }
}
