package net.darkhax.bookshelf.internal;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class BookshelfServer implements ISidedProxy {
    
    @Override
    public void setClipboard (String text) {
        
        throw new IllegalStateException("Can not set the clipboard contents on a server!");
    }
    
    @Override
    public RecipeManager getActiveRecipeManager () {
        
        return ServerLifecycleHooks.getCurrentServer().getRecipeManager();
    }
    
    @Override
    public MinecraftServer getCurrentServer () {
        
        return ServerLifecycleHooks.getCurrentServer();
    }
}