package net.darkhax.bookshelf.lib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public final class CraftingUtils
{
    public static ShapedRecipes[] findShapedRecipe(ItemStack result) {
        return findShapedRecipe(result, Integer.MAX_VALUE);
    }

    public static ShapedRecipes[] findShapedRecipe(ItemStack result, int depth) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<ShapedRecipes> found = new ArrayList<>();
        for( IRecipe recipe : recipes ) {
            if( recipe instanceof ShapedRecipes ) {
                ShapedRecipes sRecipe = ((ShapedRecipes) recipe);
                ItemStack recipeResult = sRecipe.getRecipeOutput();
                if( ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound()) ) {
                    found.add(sRecipe);
                    if( depth >= found.size() ) {
                        break;
                    }
                }
            }
        }

        return found.size() > 0 ? found.toArray(new ShapedRecipes[found.size()]) : null;
    }
    public static ShapelessRecipes[] findShapelessRecipe(ItemStack result) {
        return findShapelessRecipe(result, Integer.MAX_VALUE);
    }

    public static ShapelessRecipes[] findShapelessRecipe(ItemStack result, int depth) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<ShapelessRecipes> found = new ArrayList<>();
        for( IRecipe recipe : recipes ) {
            if( recipe instanceof ShapelessRecipes ) {
                ShapelessRecipes sRecipe = ((ShapelessRecipes) recipe);
                ItemStack recipeResult = sRecipe.getRecipeOutput();
                if( ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound()) ) {
                    found.add(sRecipe);
                    if( depth >= found.size() ) {
                        break;
                    }
                }
            }
        }

        return found.size() > 0 ? found.toArray(new ShapelessRecipes[found.size()]) : null;
    }

    public static ShapedOreRecipe[] findShapedOreRecipe(ItemStack result) {
        return findShapedOreRecipe(result, Integer.MAX_VALUE);
    }

    public static ShapedOreRecipe[] findShapedOreRecipe(ItemStack result, int depth) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<ShapedOreRecipe> found = new ArrayList<>();
        for( IRecipe recipe : recipes ) {
            if( recipe instanceof ShapedOreRecipe ) {
                ShapedOreRecipe sRecipe = ((ShapedOreRecipe) recipe);
                ItemStack recipeResult = sRecipe.getRecipeOutput();
                if( ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound()) ) {
                    found.add(sRecipe);
                    if( depth >= found.size() ) {
                        break;
                    }
                }
            }
        }

        return found.size() > 0 ? found.toArray(new ShapedOreRecipe[found.size()]) : null;
    }

    public static ShapelessOreRecipe[] findShapelessOreRecipe(ItemStack result) {
        return findShapelessOreRecipe(result, Integer.MAX_VALUE);
    }

    public static ShapelessOreRecipe[] findShapelessOreRecipe(ItemStack result, int depth) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<ShapelessOreRecipe> found = new ArrayList<>();
        for( IRecipe recipe : recipes ) {
            if( recipe instanceof ShapelessOreRecipe ) {
                ShapelessOreRecipe sRecipe = ((ShapelessOreRecipe) recipe);
                ItemStack recipeResult = sRecipe.getRecipeOutput();
                if( ItemStackUtils.areStacksEqual(recipeResult, result, result.hasTagCompound()) ) {
                    found.add(sRecipe);
                    if( depth >= found.size() ) {
                        break;
                    }
                }
            }
        }

        return found.size() > 0 ? found.toArray(new ShapelessOreRecipe[found.size()]) : null;
    }
}
