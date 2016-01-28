package net.darkhax.bookshelf.recipe;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class NBTBrewingRecipe extends  BrewingRecipe {
    
    public NBTBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
        
        super(input, ingredient, output);
    }
    
    @Override
    public boolean isIngredient (ItemStack stack) {
        
        boolean isIngredient = OreDictionary.itemMatches(this.ingredient, stack, false) && ItemStackUtils.areStacksEqual(this.ingredient, stack, true);
        return isIngredient;
    }
    
    @Override
    public boolean isInput(ItemStack stack) {
        
        return OreDictionary.itemMatches(this.input, stack, false) && ItemStackUtils.areStacksEqual(this.input, stack, true);
    }
    
    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        
        return isInput(input) && isIngredient(ingredient) ? this.output.copy() : null;
    }
}
