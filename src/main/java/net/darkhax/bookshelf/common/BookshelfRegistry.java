package net.darkhax.bookshelf.common;

import java.util.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.darkhax.bookshelf.buff.Buff;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;

public class BookshelfRegistry {
    
    /**
     * A List of all the anvil recipes that have been registered with Bookshelf.
     */
    private static final List<AnvilRecipe> anvilRecipes = new ArrayList<AnvilRecipe>();
    
    /**
     * A BiMap which stores every single Buff effect that has been registered.
     */
    public static BiMap<String, Buff> buffMap = HashBiMap.create();
    
    /**
     * A HashMap that contains a list of descriptions for ingame items. The key is the
     * ItemStack, while the ArrayList contains a bunch of localization keys for the
     * descriptions of that item.
     */
    public static HashMap<ItemStack, ArrayList<String>> infoMap = new HashMap<ItemStack, ArrayList<String>>();
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft: The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight: The ItemStack required in the right slot.
     * @param experience: The amount of experience levels this recipe will cost. Must be at
     *            least one for this to work.
     * @param output: The ItemStack to be created by the recipe.
     */
    public static void addAnvilRecipe (ItemStack inputLeft, ItemStack inputRight, int experience, ItemStack output) {
        
        anvilRecipes.add(new AnvilRecipe(inputLeft, inputRight, experience, output));
    }
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft: The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight: The ItemStack required in the left slot of the Anvil GUI.
     * @param requiredName: A name requirement for this recipe to complete.
     * @param experience: The amount of experience levels this recipe will cost. Must be at
     *            least one for this to work.
     * @param output: The ItemStack to be created by this recipe.
     */
    public static void addAnvilRecipe (ItemStack inputLeft, ItemStack inputRight, String requiredName, int experience, ItemStack output) {
        
        anvilRecipes.add(new AnvilRecipe(inputLeft, inputRight, requiredName, experience, 0, output));
    }
    
    /**
     * Adds a new AnvilRecipe to the registry. Inputs can be null.
     * 
     * @param inputLeft: The ItemStack required in the left slot of the Anvil GUI.
     * @param inputRight: The ItemStack required in the left slot of the Anvil GUI.
     * @param requiredName: A name requirement for this recipe to complete.
     * @param experience: The amount of experience levels this recipe will cost. Must be at
     *            least one for this to work.
     * @param materialCost: The amount of items to consume from the right side of the Anvil
     *            GUI. If 0, all will be used.
     * @param output: The ItemStack to be created by this recipe.
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
     * @return List<AnvilRecipe>: A List of all AnvilRecipes that have been registered with
     *         booksehfl.
     */
    public static List<AnvilRecipe> getAnvilRecipes () {
        
        return anvilRecipes;
    }
    
    /**
     * Attempts to retrieve a Buff by its name.
     *
     * @param name: The name of the buff you are looking for.
     * @return Buff: The Buff, if its name was found. If not, null.
     */
    public static Buff getBuffFromString (String name) {
        
        return (buffMap.containsKey(name)) ? buffMap.get(name) : null;
    }
    
    /**
     * Registers a Buff with the buffMap.
     *
     * @param buff: The Buff to register.
     */
    public static void registerBuff (Buff buff) {
        
        if (buffMap.containsKey(buff.getPotionName()))
            throw new RuntimeException("An attempt was made to register a Potion with the name of " + buff.getPotionName() + " however it is already in use. " + buffMap.get(buff.getPotionName()).getClass().getName() + " " + buff.getClass().getName());
            
        buffMap.put(buff.getPotionName(), buff);
    }
    
    /**
     * Registers a localization key to an Item. This is used to provide information about that
     * Item. If information already exists, yours will be appended to it.
     * 
     * @param item: The Item to add information for.
     * @param localozationKey: The localization key used to retrieve the information.
     */
    public static void addInformation (Item item, String localizationKey) {
        
        addInformation(new ItemStack(item), localizationKey);
    }
    
    /**
     * Registers a localization key to a Block. This is used to provide information about that
     * Item. If information already exists, yours will be appended to it.
     * 
     * @param Block: The Block to add information for.
     * @param localozationKey: The localization key used to retrieve the information.
     */
    public static void addInformation (Block block, String localizationKey) {
        
        addInformation(new ItemStack(block), localizationKey);
    }
    
    /**
     * Registers a localization key to an ItemStack. This is used to provide information about
     * that Item. If information already exists, yours will be appended to it.
     * 
     * @param stack: The ItemStack to add information for.
     * @param localozationKey: The localization key used to retrieve the information.
     */
    public static void addInformation (ItemStack stack, String localizationKey) {
        
        for (ItemStack keyStack : infoMap.keySet()) {
            
            if (ItemStackUtils.areStacksSimilar(keyStack, stack)) {
                infoMap.get(keyStack).add(localizationKey);
                return;
            }
        }
        
        ArrayList infoKeys = new ArrayList<String>();
        infoKeys.add(localizationKey);
        infoMap.put(stack, infoKeys);
    }
    
    /**
     * Checks to see if an ItemStack has any information registered.
     * 
     * @param stack: The ItemStack to check for.
     * @return boolean: Whether or not the ItemStack has information about it.
     */
    public static boolean doesStackHaveDescription (ItemStack stack) {
        
        for (ItemStack keyStack : infoMap.keySet())
            if (ItemStackUtils.areStacksSimilar(keyStack, stack))
                return true;
                
        return false;
    }
    
    /**
     * Retrieves a list of description translation keys for an ItemStack.
     * 
     * @param stack: The ItemStack to grab keys for.
     * @return ArrayList<String>: The array of description keys.
     */
    public static ArrayList<String> getDescriptionKeys (ItemStack stack) {
        
        for (ItemStack keyStack : infoMap.keySet())
            if (ItemStackUtils.areStacksSimilar(keyStack, stack))
                return infoMap.get(keyStack);
                
        return null;
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
         * @param firstInput: The ItemStack required in the left slot of the Anvil GUI.
         * @param secondInput: The ItemStack required in the right slot of the Anvil GUI.
         * @param experience: The amount of experience that this recipe should cost.
         * @param outputStack: The ItemStack that will be created by this recipe.
         */
        public AnvilRecipe(ItemStack firstInput, ItemStack secondInput, int experience, ItemStack outputStack) {
            
            this(firstInput, secondInput, null, experience, 0, outputStack);
        }
        
        /**
         * Constructs a new AnvilRecipe, using all of the required parameters.
         * 
         * @param firstInput: The ItemStack required in the left slot of the Anvil GUI.
         * @param secondInput: The ItemStack required in the right slot of the Anvil GUI.
         * @param requiredName: A name requirement for this recipe.
         * @param experience: The amount of experience that this recipe should cost.
         * @param materialCost: The amount of items to consume from the right slot of the Anvil
         *            GUI. If 0, the whole stack will be consumed.
         * @param outputStack: The ItemStack that will be created by this recipe.
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
         * @param firstInput: The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput: The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName: The name entered into the rename field.
         * @return int: The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public int getExperienceCost (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.experienceUsed;
        }
        
        /**
         * A basic method for calculating the material cost. This can be overridden to add your
         * own calculations.
         * 
         * @param firstInput: The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput: The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName: The name entered into the rename field.
         * @return int: The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public int getMaterialCost (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.inputRight.stackSize;
        }
        
        /**
         * A basic method for generating the output. This can be overridden to change how your
         * output is created.
         * 
         * @param firstInput: The ItemStack in the left slot of the Anvil GUI.
         * @param secondInput: The ItemStack in the right slot of the Anvil GUI.
         * @param enteredName: The name entered into the rename field.
         * @return int: The amount of experience levels to charge the player for. This must be
         *         at least 1 level for the recipe to work.
         */
        public ItemStack getOutput (ItemStack firstInput, ItemStack secondInput, String enteredName) {
            
            return this.output.copy();
        }
    }
}
