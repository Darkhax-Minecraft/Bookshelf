package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.recipes.IRecipeSerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.bookshelf.mixin.item.crafting.AccessorShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Map;

public final class ShapedDurabilityRecipe extends ShapedRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final int damageAmount;

    public ShapedDurabilityRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> input, ItemStack output, int damageAmount) {

        super(id, group, width, height, input, output);
        this.damageAmount = damageAmount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {

        return SERIALIZER.getWrapper();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {

        final NonNullList<ItemStack> keptItems = super.getRemainingItems(inv);
        return Services.INVENTORY_HELPER.keepDamageableItems(inv, keptItems, this.damageAmount);
    }

    public static final class Serializer extends IRecipeSerializer<ShapedDurabilityRecipe> {

        @Override
        public ShapedDurabilityRecipe fromJson(ResourceLocation id, JsonObject json) {

            final String group = Serializers.STRING.fromJSON(json, "group", "");
            final Map<String, Ingredient> ingredients = AccessorShapedRecipe.bookshelf$keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            final String[] pattern = AccessorShapedRecipe.bookshelf$shrink(AccessorShapedRecipe.bookshelf$patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            final int width = pattern[0].length();
            final int height = pattern.length;

            final NonNullList<Ingredient> inputs = AccessorShapedRecipe.bookshelf$dissolvePattern(pattern, ingredients, width, height);
            final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            final int damageAmount = Serializers.INT.fromJSON(json, "damageAmount", 1);
            return new ShapedDurabilityRecipe(id, group, width, height, inputs, output, damageAmount);
        }

        @Override
        public ShapedDurabilityRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            final int width = buffer.readInt();
            final int height = buffer.readInt();
            final String group = buffer.readUtf();

            final NonNullList<Ingredient> input = NonNullList.withSize(width * height, Ingredient.EMPTY);

            for (int i = 0; i < input.size(); i++) {

                input.set(i, Ingredient.fromNetwork(buffer));
            }

            final ItemStack output = buffer.readItem();
            final int damageAmount = buffer.readInt();

            return new ShapedDurabilityRecipe(recipeId, group, width, height, input, output, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedDurabilityRecipe recipe) {

            buffer.writeInt(recipe.getWidth());
            buffer.writeInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());

            for (final Ingredient ingredient : recipe.getIngredients()) {

                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.damageAmount);
        }
    }
}