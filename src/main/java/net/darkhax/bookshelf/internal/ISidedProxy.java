package net.darkhax.bookshelf.internal;

import javax.annotation.Nullable;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;

public interface ISidedProxy {
    
    void setClipboard (String text);
    
    @Nullable
    RecipeManager getActiveRecipeManager ();
    
    @Nullable
    MinecraftServer getCurrentServer ();
}