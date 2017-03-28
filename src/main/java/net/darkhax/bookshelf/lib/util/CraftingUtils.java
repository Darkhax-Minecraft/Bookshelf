package net.darkhax.bookshelf.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.darkhax.bookshelf.lib.ModTrackingList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private CraftingUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets the current recipe in a crafting inventory.
     *
     * @param craftInv The crafting inventory.
     * @param world The current world.
     * @return The recipe being crafted.
     */
    public static IRecipe getRecipeForMatrix (InventoryCrafting craftInv, World world) {

        for (final IRecipe recipe : CraftingManager.getInstance().recipes) {

            if (recipe.matches(craftInv, world))
                return recipe;
        }

        return null;
    }

    /**
     * Gets the current crafting recipe if the GUI the player has open is a ContainerWorkbench
     *
     * @param player The player to get the recipe for.
     * @return The recipe in the current gui.
     */
    public static IRecipe getCurrentCraftingRecipe (EntityPlayer player) {

        final Container container = player.openContainer;

        if (container instanceof ContainerWorkbench) {

            final ContainerWorkbench crafting = (ContainerWorkbench) container;
            return getRecipeForMatrix(crafting.craftMatrix, player.getEntityWorld());
        }

        return null;
    }

    /**
     * Attempts to get the owner of an IRecipe. This will be the mod that was active when the
     * recipe was loaded.
     *
     * @param recipe The recipe to search.
     * @return The owner of the recipe. Can be null.
     */
    public static ModContainer getOwner (IRecipe recipe) {

        final List<IRecipe> recipes = CraftingManager.getInstance().recipes;

        if (recipes instanceof ModTrackingList)
            return ((ModTrackingList<IRecipe>) recipes).getModContainer(recipe);

        return null;
    }

    /**
     * Gets a map which links IRecipe objects to mod containers. While not every recipe will be
     * tracked, most of the modded ones will be.
     *
     * @return A map which links IRecipe objects to mod containers.
     */
    public static Map<IRecipe, ModContainer> getRecipeOwners () {

        final List<IRecipe> recipes = CraftingManager.getInstance().recipes;
        return recipes instanceof ModTrackingList ? ((ModTrackingList<IRecipe>) recipes).getTrackedEntries() : Collections.emptyMap();
    }

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

        final List<T> foundRecipes = new ArrayList<>();

        for (final IRecipe recipe : CraftingManager.getInstance().getRecipeList())
            if (condition.test(recipe)) {

                final ItemStack result = recipe.getRecipeOutput();

                if (ItemStackUtils.areStacksEqual(result, stack, result.hasTagCompound())) {
                    foundRecipes.add((T) recipe);
                }
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