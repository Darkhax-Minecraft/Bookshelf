package net.darkhax.bookshelf.crafting.brewing;

import java.util.Arrays;

import net.darkhax.bookshelf.crafting.item.IngredientPotion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class BrewingRecipePotion extends BrewingRecipe {

    public BrewingRecipePotion (Item input, EffectInstance... effects) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setCustomEffects(new ItemStack(Items.POTION), Arrays.asList(effects)));
    }

    public BrewingRecipePotion (Item input, Potion potion) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
    }

    public BrewingRecipePotion (ItemStack input, EffectInstance... effects) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setCustomEffects(new ItemStack(Items.POTION), Arrays.asList(effects)));
    }

    public BrewingRecipePotion (ItemStack input, Potion potion) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
    }
}