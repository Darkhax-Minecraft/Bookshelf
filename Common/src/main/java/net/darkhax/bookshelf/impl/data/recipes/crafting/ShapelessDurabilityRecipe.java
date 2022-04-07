package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.recipes.IRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ShapelessDurabilityRecipe extends ShapelessRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final int damageAmount;

    public ShapelessDurabilityRecipe(ResourceLocation recipeId, String group, ItemStack result, NonNullList<Ingredient> ingredients, int damageAmount) {

        super(recipeId, group, result, ingredients);
        this.damageAmount = damageAmount;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {

        final NonNullList<ItemStack> keptItems = super.getRemainingItems(inv);
        return Services.INVENTORY_HELPER.keepDamageableItems(inv, keptItems, this.damageAmount);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {

        return SERIALIZER.getWrapper();
    }

    public static class Serializer extends IRecipeSerializer<ShapelessDurabilityRecipe> {

        @Override
        public ShapelessDurabilityRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            final String group = GsonHelper.getAsString(json, "group", "");
            final NonNullList<Ingredient> inputs = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            final int damageAmount = GsonHelper.getAsInt(json, "damageAmount", 1);

            if (inputs.isEmpty()) {

                throw new JsonSyntaxException("No ingredients were found for the recipe!");
            }

            else if (inputs.size() > 9) {

                throw new JsonSyntaxException("Too many ingredients. Maximum is 9 but " + inputs.size() + " were given.");
            }

            else if (output.isEmpty()) {

                throw new JsonSyntaxException("The output of the recipe must not be empty!");
            }

            return new ShapelessDurabilityRecipe(recipeId, group, output, inputs, damageAmount);
        }

        @Override
        public ShapelessDurabilityRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            final String group = buffer.readUtf(32767);
            final int inputCount = buffer.readVarInt();
            final NonNullList<Ingredient> inputs = NonNullList.withSize(inputCount, Ingredient.EMPTY);

            for (int i = 0; i < inputCount; i++) {

                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            final ItemStack output = buffer.readItem();
            final int damageAmount = buffer.readVarInt();

            return new ShapelessDurabilityRecipe(recipeId, group, output, inputs, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapelessDurabilityRecipe toWrite) {

            buffer.writeUtf(toWrite.getGroup());
            buffer.writeVarInt(toWrite.getIngredients().size());

            for (final Ingredient ingredient : toWrite.getIngredients()) {

                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(toWrite.getResultItem());
            buffer.writeVarInt(toWrite.damageAmount);
        }

        private static NonNullList<Ingredient> readIngredients (JsonArray json) {

            final NonNullList<Ingredient> ingredients = NonNullList.create();

            for (final JsonElement element : json) {

                final Ingredient ingredient = Ingredient.fromJson(element);

                if (!ingredient.isEmpty()) {

                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }
    }
}
