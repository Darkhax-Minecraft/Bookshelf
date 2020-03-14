package net.darkhax.bookshelf.internal;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

public class BookshelfServer implements ISidedProxy {
    
    protected RecipeManager recipeManager;
    protected MinecraftServer server;
    
    public BookshelfServer() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStop);
    }
    
    @Override
    public void setClipboard (String text) {
        
        throw new IllegalStateException("Can not set the clipboard contents on a server!");
    }
    
    private void onServerStart (FMLServerAboutToStartEvent event) {
        
        this.server = event.getServer();
        this.recipeManager = event.getServer().getRecipeManager();
    }
    
    private void onServerStop (FMLServerStoppedEvent event) {
        
        this.server = null;
        this.recipeManager = null;
    }
    
    @Override
    public RecipeManager getActiveRecipeManager () {
        
        return this.recipeManager;
    }
    
    @Override
    public MinecraftServer getCurrentServer () {
        
        return this.server;
    }
}