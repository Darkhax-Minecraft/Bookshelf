package net.darkhax.bookshelf.internal;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

public class BookshelfServer implements ISidedProxy {
    
    protected RecipeManager recipeManager;
    
    public BookshelfServer() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStop);
    }
    
    @Override
    public void setClipboard (String text) {
        
        throw new IllegalStateException("Can not set the clipboard contents on a server!");
    }
    
    private void onServerStart (FMLServerAboutToStartEvent event) {
        
        this.recipeManager = event.getServer().getRecipeManager();
    }
    
    private void onServerStop (FMLServerStoppedEvent event) {
        
        this.recipeManager = null;
    }
    
    @Override
    public RecipeManager getActiveRecipeManager () {
        
        return this.recipeManager;
    }
}