package net.darkhax.bookshelf.crafting.predicate;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ItemPredicateIngredient extends ItemPredicate {

    private final Ingredient ingredient;

    public ItemPredicateIngredient (Ingredient ingredient) {

        this.ingredient = ingredient;
    }

    @Override
    public boolean matches (ItemStack stack) {

        return !stack.isEmpty() && this.ingredient.test(stack);
    }

    public static ItemPredicate fromJson (JsonObject json) {

        final JsonObject ingredientObj = GsonHelper.getAsJsonObject(json, "ingredient");
        return new ItemPredicateIngredient(CraftingHelper.getIngredient(ingredientObj));
    }
}