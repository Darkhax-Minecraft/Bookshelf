package net.darkhax.bookshelf.internal;

import javax.annotation.Nullable;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;

public interface ISidedProxy {
    
    void setClipboard (String text);
    
    @Nullable
    @Deprecated
    RecipeManager getActiveRecipeManager ();
    
    @Nullable
    @Deprecated
    MinecraftServer getCurrentServer ();
}