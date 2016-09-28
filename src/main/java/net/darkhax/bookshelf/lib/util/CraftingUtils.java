package net.darkhax.bookshelf.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingUtils {
    
    /**
     * Generates a list of all shaped recipes that have a result similar to the passed stack.
     * 
     * @param stack The ItemStack to get recipes for.
     * @return A list of recipes that can craft the passed stack.
     */
    public static List<ShapedRecipes> getShapedRecipes (ItemStack stack) {
        
        return getRecipesForStack(stack, recipe -> recipe instanceof ShapedRecipes);
    }
    
    /**
     * Generates a list of all shaped ore recipes that have a result similar to the passed
     * stack.
     * 
     * @param stack The ItemStack to get recipes for.
     * @return A list of recipes that can craft the passed stack.
     */
    public static List<ShapedOreRecipe> getShapedOreRecipe (ItemStack stack) {
        
        return getRecipesForStack(stack, recipe -> recipe instanceof ShapedOreRecipe);
    }
    
    /**
     * Generates a list of all shapeless recipes that have a result similar to the passed
     * stack.
     * 
     * @param stack The ItemStack to get recipes for.
     * @return A list of recipes that can craft the passed stack.
     */
    public static List<ShapelessRecipes> getShapelessRecipes (ItemStack stack) {
        
        return getRecipesForStack(stack, recipe -> recipe instanceof ShapelessRecipes);
    }
    
    /**
     * Generates a list of all shapeless ore recipes that have a result similar to the passed
     * stack.
     * 
     * @param stack The ItemStack to get recipes for.
     * @return A list of recipes that can craft the passed stack.
     */
    public static List<ShapelessOreRecipe> getShapelessOreRecipe (ItemStack stack) {
        
        return getRecipesForStack(stack, recipe -> recipe instanceof ShapelessOreRecipe);
    }
    
    /**
     * Generates a list of all recipes that have a result similar to the passed stack.
     * 
     * @param stack The ItemStack to get recipes for.
     * @return A list of recipes that can craft the passed stack.
     */
    public static List<IRecipe> getAnyRecipe (ItemStack stack) {
        
        return getRecipesForStack(stack, recipe -> true);
    }
    
    /**
     * Generates a list of all recipes that have a result similar to the passed stack and pass
     * the predicate test.
     * 
     * @param stack The ItemStack to get recipes for.
     * @param condition A predicate to do additional checks on the recipe.
     * @return A list of recipes that can craft the passed stack and pass the predicate test.
     */
    @SuppressWarnings("unchecked")
    public static <T extends IRecipe> List<T> getRecipesForStack (ItemStack stack, Predicate<IRecipe> condition) {
        
        final List<T> foundRecipes = new ArrayList<T>();
        
        for (final IRecipe recipe : CraftingManager.getInstance().getRecipeList())
            if (condition.test(recipe)) {
                
                final ItemStack result = recipe.getRecipeOutput();
                
                if (ItemStackUtils.areStacksEqual(result, stack, result.hasTagCompound()))
                    foundRecipes.add((T) recipe);
            }
        
        return foundRecipes;
    }
    
    /**
     * Creates 9 recipes which allow an ItemStack to be converted into a different one. 9
     * recipes to allow up to 9 at a time.
     * 
     * @param input The initial input item.
     * @param output The resulting item.
     */
    public static void createConversionRecipes (ItemStack input, ItemStack output) {
        
        for (int amount = 1; amount < 10; amount++) {
            
            final ItemStack[] inputs = new ItemStack[amount];
            Arrays.fill(inputs, input);
            GameRegistry.addShapelessRecipe(ItemStackUtils.copyStackWithSize(output, amount), (Object[]) inputs);
        }
    }
}