package net.darkhax.bookshelf.internal;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;

public class BookshelfClient implements ISidedProxy {
    
    private RecipeManager recipeManager;
    
    public BookshelfClient() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onRecipesUpdated);
    }
    
    @Override
    public void setClipboard (String text) {
        
        Minecraft.getInstance().keyboardListener.setClipboardString(text);
    }
    
    @Override
    public RecipeManager getActiveRecipeManager () {
        
        return this.recipeManager;
    }
    
    private void onRecipesUpdated (RecipesUpdatedEvent event) {
        
        this.recipeManager = event.getRecipeManager();
    }
}