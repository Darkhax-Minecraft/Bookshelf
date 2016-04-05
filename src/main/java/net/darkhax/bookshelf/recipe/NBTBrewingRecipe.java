package net.darkhax.bookshelf.recipe;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class NBTBrewingRecipe extends BrewingRecipe {
    
    public NBTBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
        
        super(input, ingredient, output);
    }
    
    @Override
    public boolean isIngredient (ItemStack stack) {
        
        boolean isIngredient = OreDictionary.itemMatches(this.getIngredient(), stack, false) && ItemStackUtils.areStacksEqual(this.getIngredient(), stack, true);
        return isIngredient;
    }
    
    @Override
    public boolean isInput (ItemStack stack) {
        
        return OreDictionary.itemMatches(this.getInput(), stack, false) && ItemStackUtils.areStacksEqual(this.getInput(), stack, true);
    }
    
    @Override
    public ItemStack getOutput (ItemStack input, ItemStack ingredient) {
        
        return isInput(input) && isIngredient(ingredient) ? this.getOutput().copy() : null;
    }
}
