package net.darkhax.bookshelf.crafting.recipes.smithing;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeEnchantment extends UpgradeRecipe {

    public static final RecipeSerializer<?> SERIALIZER = new Serializer();

    private final List<EnchantmentInstance> enchantments;

    public SmithingRecipeEnchantment (ResourceLocation recipeId, Ingredient base, Ingredient addition, List<EnchantmentInstance> enchantments) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.enchantments = enchantments;
    }

    @Override
    public ItemStack assemble (Container inv) {

        final ItemStack stack = inv.getItem(0).copy();

        for (final EnchantmentInstance data : this.enchantments) {

            if (EnchantmentHelper.getItemEnchantmentLevel(data.enchantment, stack) < data.level) {

                stack.enchant(data.enchantment, data.level);
            }
        }

        return stack;
    }

    @Override
    public boolean matches (Container inv, Level world) {

        final ItemStack input = inv.getItem(0);

        for (final EnchantmentInstance data : this.enchantments) {

            if (EnchantmentHelper.getItemEnchantmentLevel(data.enchantment, input) < data.level) {

                return super.matches(inv, world);
            }
        }

        return false;
    }

    @Override
    public boolean isSpecial () {

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SmithingRecipeEnchantment> {

        @Override
        public SmithingRecipeEnchantment fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            final List<EnchantmentInstance> enchants = Serializers.ENCHANTMENT_DATA.readList(json, "enchantments");
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }

        @Override
        public SmithingRecipeEnchantment fromNetwork (ResourceLocation recipeId, FriendlyByteBuf buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final List<EnchantmentInstance> enchants = Serializers.ENCHANTMENT_DATA.readList(buffer);
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }

        @Override
        public void toNetwork (FriendlyByteBuf buffer, SmithingRecipeEnchantment recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            Serializers.ENCHANTMENT_DATA.writeList(buffer, recipe.enchantments);
        }
    }
}