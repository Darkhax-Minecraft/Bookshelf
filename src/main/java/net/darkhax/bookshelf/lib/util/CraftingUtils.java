package net.darkhax.bookshelf.lib.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

public class CraftingUtils {
    
    /**
     * Creates a ShapedRecipe instance that can be used however you would like.
     * 
     * @param Output: The output of this recipe.
     * @param ingredients: The input for the recipe, same as a standard shaped recipe.
     * @return ShapedRecipe: An instance of ShapedRecipe that has not been registered to any
     *         CraftingManager.
     */
    public static ShapedRecipes getShapedRecipe (ItemStack Output, Object... ingredients) {
        
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        
        if (ingredients[i] instanceof String[]) {
            
            String[] astring = (String[]) ((String[]) ingredients[i++]);
            
            for (int l = 0; l < astring.length; ++l) {
                
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        
        else {
            
            while (ingredients[i] instanceof String) {
                
                String s2 = (String) ingredients[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }
        
        HashMap hashmap;
        
        for (hashmap = new HashMap(); i < ingredients.length; i += 2) {
            
            Character character = (Character) ingredients[i];
            ItemStack itemstack1 = null;
            
            if (ingredients[i + 1] instanceof Item)
                itemstack1 = new ItemStack((Item) ingredients[i + 1]);
                
            else if (ingredients[i + 1] instanceof Block)
                itemstack1 = new ItemStack((Block) ingredients[i + 1], 1, 32767);
                
            else if (ingredients[i + 1] instanceof ItemStack)
                itemstack1 = (ItemStack) ingredients[i + 1];
                
            hashmap.put(character, itemstack1);
        }
        
        ItemStack[] aitemstack = new ItemStack[j * k];
        
        for (int i1 = 0; i1 < j * k; ++i1) {
            
            char c0 = s.charAt(i1);
            
            if (hashmap.containsKey(Character.valueOf(c0)))
                aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
                
            else
                aitemstack[i1] = null;
        }
        
        ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, Output);
        return shapedrecipes;
    }
    
    /**
     * Creates a new ShapelessRecipe that can be used however you would like.
     * 
     * @param Output: The output for the recipe.
     * @param ingrediants: The inputs for the recipe. Must be an ItemStack, Block or Item.
     * @return ShapelessRecipe: An instance of a ShapelessRecipe that has not been registered
     *         with a CraftingManager.
     */
    public static ShapelessRecipes getShapelessRecipe (ItemStack Output, Object... ingrediants) {
        
        ArrayList input = new ArrayList();
        Object[] ingrediantArray = ingrediants;
        int recipeLength = ingrediants.length;
        
        for (int j = 0; j < recipeLength; ++j) {
            
            Object ingrediant = ingrediantArray[j];
            
            if (ingrediant instanceof ItemStack)
                input.add(((ItemStack) ingrediant).copy());
                
            else if (ingrediant instanceof Item)
                input.add(new ItemStack((Item) ingrediant));
                
            else {
                
                if (!(ingrediant instanceof Block))
                    throw new RuntimeException("Invalid shapeless recipy!");
                    
                input.add(new ItemStack((Block) ingrediant));
            }
        }
        
        return new ShapelessRecipes(Output, input);
    }
}