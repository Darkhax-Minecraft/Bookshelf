package net.darkhax.bookshelf.crafting.recipes.smithing;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.bookshelf.util.TextUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeFont extends SmithingRecipe {

    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();

    private final ResourceLocation fontId;

    public SmithingRecipeFont (ResourceLocation recipeId, Ingredient base, Ingredient addition, ResourceLocation fontId) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.fontId = fontId;
    }

    @Override
    public ItemStack assemble (IInventory inv) {

        final ItemStack stack = inv.getItem(0).copy();
        stack.setHoverName(TextUtils.applyFont(stack.getHoverName(), this.fontId));
        return stack;
    }

    @Override
    public boolean isSpecial () {

        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipeFont> {

        @Override
        public SmithingRecipeFont fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition"));
            final ResourceLocation font = Serializers.RESOURCE_LOCATION.read(json, "font");
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }

        @Override
        public SmithingRecipeFont fromNetwork (ResourceLocation recipeId, PacketBuffer buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final ResourceLocation font = ResourceLocation.tryParse(buffer.readUtf());
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }

        @Override
        public void toNetwork (PacketBuffer buffer, SmithingRecipeFont recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeUtf(recipe.fontId.toString());
        }
    }
}