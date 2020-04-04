/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class RecipeUtils {
    
    /**
     * Gets the active recipe manager. One the client this is updated from the recipe updated
     * event. On the server this is updated when the server first launches and when it is
     * stopped.
     * 
     * @return The current recipe manager in use.
     */
    @Nullable
    @Deprecated
    public static RecipeManager getActiveRecipeManager () {
        
        return Bookshelf.SIDED.getActiveRecipeManager();
    }
    
    /**
     * Looks up the map of all known recipes for a given recipe type.
     * 
     * @param <T> The type of the IRecipe object.
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @return A map of recipes for the provided recipe type. Key is ResourceLocation, value is
     *         the recipe object.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends IRecipe<?>> Map<ResourceLocation, T> getRecipes (IRecipeType<T> recipeType, RecipeManager manager) {
        
        return (Map) manager.recipes.getOrDefault(recipeType, Collections.emptyMap());
    }
    
    /**
     * Gets a list of all recipes for a given recipe type. This list will be sorted using the
     * translation key of the output item.
     * 
     * @param <T> The type of the IRecipe object.
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @return A list of recipes for the given recipe type.
     */
    public static <T extends IRecipe<?>> List<T> getRecipeList (IRecipeType<T> recipeType, RecipeManager manager) {
        
        return getRecipeList(recipeType, manager, Comparator.comparing(recipe -> recipe.getRecipeOutput().getTranslationKey()));
    }
    
    /**
     * Gets a list of all recipes for a given recipe type. This list will be sorted using the
     * provided comparator.
     * 
     * @param <T> The type of the IRecipe object.
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @param comparator A comparator that will be used to sort the map.
     * @return A list of recipes for the given recipe type.
     */
    public static <T extends IRecipe<?>> List<T> getRecipeList (IRecipeType<T> recipeType, RecipeManager manager, Comparator<T> comparator) {
        
        return getRecipes(recipeType, manager).values().stream().sorted(comparator).collect(Collectors.toList());
    }
    
    /**
     * Gets all recipes for a given recipe type.
     * 
     * @param <C> The inventory type of the recipe.
     * @param <T> The type of the recipe.
     * @param world The world to read data from.
     * @param recipeType The recipe type you want to get.
     * @return A map of recipes keyed to their IDs.
     */
    public static <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> getRecipes (World world, IRecipeType<T> recipeType) {
        
        return getRecipes(world.getRecipeManager(), recipeType);
    }
    
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
    
    /**
     * Creates an ingredient using an array of item tags.
     * 
     * @param tags The tags to create an ingredient for.
     * @return An ingredient for these tags.
     */
    @SafeVarargs
    public static Ingredient ingredientFromTags (Tag<Item>... tags) {
        
        return Ingredient.fromItems(Arrays.stream(tags).flatMap(t -> t.getAllElements().stream()).toArray(Item[]::new));
    }
}