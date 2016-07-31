package net.darkhax.bookshelf.lib.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingUtils {
    public static ShapedRecipes[] findShapedRecipe (ItemStack result) {
        
        return findShapedRecipe(result, Integer.MAX_VALUE);
    }
    
    public static ShapedRecipes[] findShapedRecipe (ItemStack result, int depth) {
        
        final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final List<ShapedRecipes> found = new ArrayList<>();
        for (final IRecipe recipe : recipes)
            if (recipe instanceof ShapedRecipes) {
                final ShapedRecipes sRecipe = (ShapedRecipes) recipe;
                final ItemStack recipeResult = sRecipe.getRecipeOutput();
                if (ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound())) {
                    found.add(sRecipe);
                    if (depth >= found.size())
                        break;
                }
            }
            
        return found.size() > 0 ? found.toArray(new ShapedRecipes[found.size()]) : null;
    }
    
    public static ShapelessRecipes[] findShapelessRecipe (ItemStack result) {
        
        return findShapelessRecipe(result, Integer.MAX_VALUE);
    }
    
    public static ShapelessRecipes[] findShapelessRecipe (ItemStack result, int depth) {
        
        final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final List<ShapelessRecipes> found = new ArrayList<>();
        for (final IRecipe recipe : recipes)
            if (recipe instanceof ShapelessRecipes) {
                final ShapelessRecipes sRecipe = (ShapelessRecipes) recipe;
                final ItemStack recipeResult = sRecipe.getRecipeOutput();
                if (ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound())) {
                    found.add(sRecipe);
                    if (depth >= found.size())
                        break;
                }
            }
            
        return found.size() > 0 ? found.toArray(new ShapelessRecipes[found.size()]) : null;
    }
    
    public static ShapedOreRecipe[] findShapedOreRecipe (ItemStack result) {
        
        return findShapedOreRecipe(result, Integer.MAX_VALUE);
    }
    
    public static ShapedOreRecipe[] findShapedOreRecipe (ItemStack result, int depth) {
        
        final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final List<ShapedOreRecipe> found = new ArrayList<>();
        for (final IRecipe recipe : recipes)
            if (recipe instanceof ShapedOreRecipe) {
                final ShapedOreRecipe sRecipe = (ShapedOreRecipe) recipe;
                final ItemStack recipeResult = sRecipe.getRecipeOutput();
                if (ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound())) {
                    found.add(sRecipe);
                    if (depth >= found.size())
                        break;
                }
            }
            
        return found.size() > 0 ? found.toArray(new ShapedOreRecipe[found.size()]) : null;
    }
    
    public static ShapelessOreRecipe[] findShapelessOreRecipe (ItemStack result) {
        
        return findShapelessOreRecipe(result, Integer.MAX_VALUE);
    }
    
    public static ShapelessOreRecipe[] findShapelessOreRecipe (ItemStack result, int depth) {
        
        final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final List<ShapelessOreRecipe> found = new ArrayList<>();
        for (final IRecipe recipe : recipes)
            if (recipe instanceof ShapelessOreRecipe) {
                final ShapelessOreRecipe sRecipe = (ShapelessOreRecipe) recipe;
                final ItemStack recipeResult = sRecipe.getRecipeOutput();
                if (ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound())) {
                    found.add(sRecipe);
                    if (depth >= found.size())
                        break;
                }
            }
            
        return found.size() > 0 ? found.toArray(new ShapelessOreRecipe[found.size()]) : null;
    }
}
