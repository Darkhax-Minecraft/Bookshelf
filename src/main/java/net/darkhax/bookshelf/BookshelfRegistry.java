/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.crafting.AnvilRecipe;
import net.darkhax.bookshelf.crafting.IAnvilRecipe;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.ModTrackingList;
import net.minecraft.item.ItemStack;

public class BookshelfRegistry {
    
    /**
     * A List of crash messages. When a crash happens, one of these messages will be randomly
     * selected.
     */
    private static final List<String> crashComments = new ArrayList<>();
    
    /**
     * A List of all the anvil recipes that have been registered with Bookshelf.
     */
    // TODO Add support for oredict recipes by default.
    // TODO add JEI support for showing recipes!
    private static final List<IAnvilRecipe> anvilRecipes = new ModTrackingList<>(new ArrayList<IAnvilRecipe>());
    
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
    public static void addAnvilRecipe (AnvilRecipe recipe) {
        
        anvilRecipes.add(recipe);
    }
    
    /**
     * Retrieves the List of all registered anvil recipes.
     *
     * @return List<AnvilRecipe> A List of all AnvilRecipes that have been registered with
     *         bookshelf.
     */
    public static List<IAnvilRecipe> getAnvilRecipes () {
        
        return anvilRecipes;
    }
    
    /**
     * Gets a crash comment randomly from {@link #crashComments}. This includes all of the
     * vanilla entries as well.
     *
     * @return A random crash comment message.
     */
    public static String getCrashComment () {
        
        return crashComments.get(Constants.RANDOM.nextInt(crashComments.size()));
    }
    
    /**
     * Adds a new crash comment message. If the crash comment message feature is enabled, it
     * will have a chance in showing up in crash logs.
     *
     * @param message The message to add.
     */
    public static void addCrashComment (String message) {
        
        crashComments.add(message);
    }
}