package net.darkhax.bookshelf.crafting.brewing;

import java.util.Arrays;

import net.darkhax.bookshelf.crafting.item.IngredientPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class BrewingRecipePotion extends BrewingRecipe {

    public BrewingRecipePotion (Item input, MobEffectInstance... effects) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setCustomEffects(new ItemStack(Items.POTION), Arrays.asList(effects)));
    }

    public BrewingRecipePotion (Item input, Potion potion) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
    }

    public BrewingRecipePotion (ItemStack input, MobEffectInstance... effects) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setCustomEffects(new ItemStack(Items.POTION), Arrays.asList(effects)));
    }

    public BrewingRecipePotion (ItemStack input, Potion potion) {

        super(IngredientPotion.AWKWARD, Ingredient.of(input), PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
    }
}