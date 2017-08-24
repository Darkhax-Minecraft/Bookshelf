/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.crafting.AnvilRecipe;
import net.darkhax.bookshelf.crafting.IAnvilRecipe;
import net.darkhax.bookshelf.lib.ModTrackingList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BookshelfRegistry {

    /**
     * A List of all the anvil recipes that have been registered with Bookshelf.
     */
    // TODO add JEI support for showing recipes!
    private static final ModTrackingList<IAnvilRecipe> anvilRecipes = new ModTrackingList<>();

    /**
     * Adds a new oredict anvil recipe to the list. This recipe will have no name requirement.
     *
     * @param leftSlot The item required in the left slot.
     * @param rightSlot The item required in the right slot.
     * @param experience The exp cost for the recipe.
     * @param materialCost The material cost for the recipe. 0 means all!
     * @param output The stack to give as an output.
     */
    public static void addAnvilRecipe (String leftSlot, ItemStack rightSlot, int experience, int materialCost, ItemStack output) {

        for (final ItemStack stack : OreDictionary.getOres(leftSlot)) {

            addAnvilRecipe(stack, rightSlot, null, experience, materialCost, output);
        }
    }

    /**
     * Adds a new oredict anvil recipe to the list. This recipe will have no name requirement.
     *
     * @param leftSlot The item required in the left slot.
     * @param rightSlot The item required in the right slot.
     * @param experience The exp cost for the recipe.
     * @param materialCost The material cost for the recipe. 0 means all!
     * @param output The stack to give as an output.
     */
    public static void addAnvilRecipe (ItemStack leftSlot, String rightSlot, int experience, int materialCost, ItemStack output) {

        for (final ItemStack stack : OreDictionary.getOres(rightSlot)) {

            addAnvilRecipe(leftSlot, stack, null, experience, materialCost, output);
        }
    }

    /**
     * Adds a new oredict anvil recipe to the list. This recipe will have no name requirement.
     *
     * @param leftSlot The item required in the left slot.
     * @param rightSlot The item required in the right slot.
     * @param experience The exp cost for the recipe.
     * @param materialCost The material cost for the recipe. 0 means all!
     * @param output The stack to give as an output.
     */
    public static void addAnvilRecipe (String leftSlot, String rightSlot, int experience, int materialCost, ItemStack output) {

        for (final ItemStack stackLeft : OreDictionary.getOres(leftSlot)) {

            for (final ItemStack stackRight : OreDictionary.getOres(rightSlot)) {

                addAnvilRecipe(stackLeft, stackRight, null, experience, materialCost, output);
            }
        }
    }

    /**
     * Adds a new anvil recipe to the list. This recipe will have no name requirement.
     *
     * @param leftSlot The item required in the left slot.
     * @param rightSlot The item required in the right slot.
     * @param experience The exp cost for the recipe.
     * @param materialCost The material cost for the recipe. 0 means all!
     * @param output The stack to give as an output.
     */
    public static void addAnvilRecipe (ItemStack leftSlot, ItemStack rightSlot, int experience, int materialCost, ItemStack output) {

        addAnvilRecipe(leftSlot, rightSlot, null, experience, materialCost, output);
    }

    /**
     * Adds a new anvil recipe to the list.
     *
     * @param leftSlot The item required in the left slot.
     * @param rightSlot The item required in the right slot.
     * @param requiredName The string required in the name field.
     * @param experience The exp cost for the recipe.
     * @param materialCost The material cost for the recipe. 0 means all!
     * @param output The stack to give as an output.
     */
    public static void addAnvilRecipe (ItemStack leftSlot, ItemStack rightSlot, String requiredName, int experience, int materialCost, ItemStack output) {

        addAnvilRecipe(new AnvilRecipe(leftSlot, rightSlot, requiredName, experience, materialCost, output));
    }

    /**
     * Adds a new anvil recipe to the list.
     *
     * @param recipe The recipe to add.
     */
    public static void addAnvilRecipe (IAnvilRecipe recipe) {

        anvilRecipes.add(recipe);
    }

    /**
     * Retrieves the List of all registered anvil recipes.
     *
     * @return A List of all AnvilRecipes that have been registered with bookshelf.
     */
    public static ModTrackingList<IAnvilRecipe> getAnvilRecipes () {

        return anvilRecipes;
    }
}