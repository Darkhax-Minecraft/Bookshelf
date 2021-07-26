package net.darkhax.bookshelf.crafting.recipes.smithing;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeEnchantment extends SmithingRecipe {

    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();

    private final List<EnchantmentData> enchantments;

    public SmithingRecipeEnchantment (ResourceLocation recipeId, Ingredient base, Ingredient addition, List<EnchantmentData> enchantments) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.enchantments = enchantments;
    }

    @Override
    public ItemStack assemble (IInventory inv) {

        final ItemStack stack = inv.getItem(0).copy();

        for (final EnchantmentData data : this.enchantments) {

            if (EnchantmentHelper.getItemEnchantmentLevel(data.enchantment, stack) < data.level) {

                stack.enchant(data.enchantment, data.level);
            }
        }

        return stack;
    }

    @Override
    public boolean matches (IInventory inv, World world) {

        final ItemStack input = inv.getItem(0);

        for (final EnchantmentData data : this.enchantments) {

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
    public IRecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipeEnchantment> {

        @Override
        public SmithingRecipeEnchantment fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition"));
            final List<EnchantmentData> enchants = Serializers.ENCHANTMENT_DATA.readList(json, "enchantments");
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }

        @Override
        public SmithingRecipeEnchantment fromNetwork (ResourceLocation recipeId, PacketBuffer buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final List<EnchantmentData> enchants = Serializers.ENCHANTMENT_DATA.readList(buffer);
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }

        @Override
        public void toNetwork (PacketBuffer buffer, SmithingRecipeEnchantment recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            Serializers.ENCHANTMENT_DATA.writeList(buffer, recipe.enchantments);
        }
    }
}