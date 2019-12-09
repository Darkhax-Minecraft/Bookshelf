package net.darkhax.bookshelf.util;

import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

public final class RecipeUtils {
    
    /**
     * Gets all recipes for a given recipe type.
     * 
     * @param <C> The inventory type of the recipe.
     * @param <T> The type of the recipe.
     * @param manager An instance of the recipe manager.
     * @param recipeType The recipe type you want to get.
     * @return A map of recipes keyed to their IDs.
     */
    public static <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> getRecipes (RecipeManager manager, IRecipeType<T> recipeType) {
        
        return manager.getRecipes(recipeType);
    }
}