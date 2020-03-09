package net.darkhax.bookshelf.internal;

import javax.annotation.Nullable;

import net.minecraft.item.crafting.RecipeManager;

public interface ISidedProxy {
    
    void setClipboard (String text);
    
    @Nullable
    RecipeManager getActiveRecipeManager ();
}