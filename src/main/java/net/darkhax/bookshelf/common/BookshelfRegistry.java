package net.darkhax.bookshelf.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class BookshelfRegistry {
    
    /**
     * A List of all the anvil recipes that have been registered with Bookshelf.
     */
    private static final List<AnvilRecipe> anvilRecipes = new ArrayList<AnvilRecipe>();
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight The ItemStack required in the right slot.
     * @param experience The amount of experience levels this recipe will cost. Must be at
     *        least one for this to work.
     * @param output The ItemStack to be created by the recipe.
     */
    public static void addAnvilRecipe (ItemStack inputLeft, ItemStack inputRight, int experience, ItemStack output) {
        
        anvilRecipes.add(new AnvilRecipe(inputLeft, inputRight, experience, output));
    }
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight The ItemStack required in the left slot of the Anvil GUI.
     * @param requiredName A name requirement for this recipe to complete.
     * @param experience The amount of experience levels this recipe will cost. Must be at
     *        least one for this to work.
     * @param output The ItemStack to be created by this recipe.
     */
    public static void addAnvilRecipe (ItemStack inputLeft, ItemStack inputRight, String requiredName, int experience, ItemStack output) {
        
        anvilRecipes.add(new AnvilRecipe(inputLeft, inputRight, requiredName, experience, 0, output));
    }
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight The ItemStack required in the left slot of the Anvil GUI.
     * @param requiredName A name requirement for this recipe to complete.
     * @param experience The amount of experience levels this recipe will cost. Must be at
     *        least one for this to work.
     * @param materialCost The amount of items to consume from the right side of the Anvil GUI.
     *        If 0, all will be used.
     * @param output The ItemStack to be created by this recipe.
     */
    public static void addAnvilRecipe (ItemStack inputLeft, ItemStack inputRight, String requiredName, int experience, int materialCost, ItemStack output) {
        
        anvilRecipes.add(new AnvilRecipe(inputLeft, inputRight, requiredName, experience, materialCost, output));
    }
    
    public void addAnvilRecipe (AnvilRecipe recipe) {
        
        anvilRecipes.add(recipe);
    }
    
    /**
     * Retrieves the List of all registered anvil recipes.
     * 
     * @return List<AnvilRecipe> A List of all AnvilRecipes that have been registered with
     *         booksehfl.
     */
    public static List<AnvilRecipe> getAnvilRecipes () {
        
        return anvilRecipes;
    }
    
    public static class AnvilRecipe {
        
        /**
         * The ItemStack required in the left slot of the Anvil GUI.
         */
        public ItemStack inputLeft;
        
        /**
         * The ItemStack required in the right side of the Anvil GUI.
         */
        public ItemStack inputRight;
        
        /**
         * A name requirement for the recipe.
         */
        public String nameTaxt;
        
        /**
         * The amount of experience levels to charge for this recipe.
         */
        public int experienceUsed;
        
        /**
         * The amount of items to consume, from the right slot of the Anvil GUI. If 0, the
         * whole stack will be consumed.
         */
        public int materialCost;
        
        /**
         * The ItemStack output for this recipe.
         */
        public ItemStack output;
        
        /**
         * Constructs a new AnvilRecipe, using all of the required parameters, except for the
         * name requirement and material cost.
         * 
         * @param firstInput The ItemStack required in the left slot of the Anvil GUI.
         * @param secondInput The ItemStack required in the right slot of the Anvil GUI.
         * @param experience The amount of experience that this recipe should cost.
         * @param outputStack The ItemStack that will be created by this recipe.
         */
        public AnvilRecipe(ItemStack firstInput, ItemStack secondInput, int experience, ItemStack outputStack) {
            
            this(firstInput, secondInput, null, experience, 0, outputStack);
        }
        
        /**
         * Constructs a new AnvilRecipe, using all of the required parameters.
         * 
         * @param firstInput The ItemStack required in the left slot of the Anvil GUI.
         * @param secondInput The ItemStack required in the right slot of the Anvil GUI.
         * @param requiredName A name requirement for this recipe.
         * @param experience The amount of experience that this recipe should cost.
         * @param materialCost The amount of items to consume from the right slot of the Anvil
         *        GUI. If 0, the whole stack will be consumed.
         * @param outputStack The ItemStack that will be created by this recipe.
         */
        public AnvilRecipe(ItemStack firstInput, ItemStack secondInput, String requiredName, int experience, int materialCost, ItemStack outputStack) {
            
            this.inputLeft = firstInput;
            this.inputRight = secondInput;
            this.nameTaxt = requiredName;
            this.experienceUsed = experience;
            this.materialCost = materialCost;
            this.output = outputStack;
        }
        
        /**
         * A basic method for calculating the experience level cost. This can be overridden to
         * add your own calculations.
         * 
         * @param firstInput The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName The name entered into the rename field.
         * @return int The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public int getExperienceCost (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.experienceUsed;
        }
        
        /**
         * A basic method for calculating the material cost. This can be overridden to add your
         * own calculations.
         * 
         * @param firstInput The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName The name entered into the rename field.
         * @return int The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public int getMaterialCost (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.inputRight.stackSize;
        }
        
        /**
         * A basic method for generating the output. This can be overridden to change how your
         * output is created.
         * 
         * @param firstInput The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName The name entered into the rename field.
         * @return int The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public ItemStack getOutput (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.output.copy();
        }
    }
}
