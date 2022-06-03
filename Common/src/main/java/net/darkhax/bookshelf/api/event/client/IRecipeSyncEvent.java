package net.darkhax.bookshelf.api.event.client;

import net.minecraft.world.item.crafting.RecipeManager;

public interface IRecipeSyncEvent {

    void apply(RecipeManager manager);
}