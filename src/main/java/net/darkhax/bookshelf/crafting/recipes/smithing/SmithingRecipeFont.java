package net.darkhax.bookshelf.crafting.recipes.smithing;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.bookshelf.util.TextUtils;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeFont extends UpgradeRecipe {

    public static final RecipeSerializer<?> SERIALIZER = new Serializer();

    private final ResourceLocation fontId;

    public SmithingRecipeFont (ResourceLocation recipeId, Ingredient base, Ingredient addition, ResourceLocation fontId) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.fontId = fontId;
    }

    @Override
    public ItemStack assemble (Container inv) {

        final ItemStack stack = inv.getItem(0).copy();
        stack.setHoverName(TextUtils.applyFont(stack.getHoverName(), this.fontId));
        return stack;
    }

    @Override
    public boolean isSpecial () {

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SmithingRecipeFont> {

        @Override
        public SmithingRecipeFont fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            final ResourceLocation font = Serializers.RESOURCE_LOCATION.read(json, "font");
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }

        @Override
        public SmithingRecipeFont fromNetwork (ResourceLocation recipeId, FriendlyByteBuf buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final ResourceLocation font = ResourceLocation.tryParse(buffer.readUtf());
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }

        @Override
        public void toNetwork (FriendlyByteBuf buffer, SmithingRecipeFont recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeUtf(recipe.fontId.toString());
        }
    }
}