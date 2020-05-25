package net.darkhax.bookshelf.crafting.predicate;

import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ItemPredicateIngredient extends ItemPredicate {
    
    private final Ingredient ingredient;
    
    public ItemPredicateIngredient(Ingredient ingredient) {
        
        this.ingredient = ingredient;
    }
    
    @Override
    public boolean test (ItemStack stack) {
        
        return !stack.isEmpty() && this.ingredient.test(stack);
    }
    
    public static ItemPredicate fromJson (JsonObject json) {
        
        final JsonObject ingredientObj = JSONUtils.getJsonObject(json, "ingredient");
        return new ItemPredicateIngredient(CraftingHelper.getIngredient(ingredientObj));
    }
}