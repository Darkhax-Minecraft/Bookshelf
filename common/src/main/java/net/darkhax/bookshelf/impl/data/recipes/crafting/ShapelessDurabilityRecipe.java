package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ShapelessDurabilityRecipe extends ShapelessRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final ItemStack output;
    private final int damageAmount;

    public ShapelessDurabilityRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, int damageAmount) {

        super(group, category, result, ingredients);
        this.damageAmount = damageAmount;
        this.output = result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {

        final NonNullList<ItemStack> keptItems = super.getRemainingItems(inv);
        return Services.INVENTORY_HELPER.keepDamageableItems(inv, keptItems, this.damageAmount);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {

        return SERIALIZER;
    }

    public ItemStack getOutput() {

        return this.output;
    }

    public int getDamageAmount() {
        return this.damageAmount;
    }

    public static class Serializer implements RecipeSerializer<ShapelessDurabilityRecipe> {

        private static final Codec<ShapelessDurabilityRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(ShapelessDurabilityRecipe::getGroup),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessDurabilityRecipe::category),
                CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(ShapelessDurabilityRecipe::getOutput),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(raw -> {
                    final Ingredient[] ingredients = raw.stream().filter(entry -> !entry.isEmpty()).toArray((size) -> new Ingredient[size]);
                    if (ingredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    }
                    else {
                        return ingredients.length > 9 ? DataResult.error(() -> "Too many ingredients for shapeless recipe") : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                    }
                }, DataResult::success).forGetter(ShapelessDurabilityRecipe::getIngredients),
                Codec.INT.fieldOf("damageAmount").orElse(1).forGetter(ShapelessDurabilityRecipe::getDamageAmount)
        ).apply(instance, ShapelessDurabilityRecipe::new));

        @Override
        public Codec<ShapelessDurabilityRecipe> codec() {

            return CODEC;
        }

        @Override
        public ShapelessDurabilityRecipe fromNetwork(FriendlyByteBuf buffer) {

            final String group = buffer.readUtf(32767);
            final int inputCount = buffer.readVarInt();
            final NonNullList<Ingredient> inputs = NonNullList.withSize(inputCount, Ingredient.EMPTY);

            for (int i = 0; i < inputCount; i++) {

                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            final CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);

            final ItemStack output = buffer.readItem();
            final int damageAmount = buffer.readVarInt();

            return new ShapelessDurabilityRecipe(group, category, output, inputs, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapelessDurabilityRecipe toWrite) {

            buffer.writeUtf(toWrite.getGroup());
            buffer.writeVarInt(toWrite.getIngredients().size());

            for (final Ingredient ingredient : toWrite.getIngredients()) {

                ingredient.toNetwork(buffer);
            }

            buffer.writeEnum(toWrite.category());

            buffer.writeItem(toWrite.output);
            buffer.writeVarInt(toWrite.damageAmount);
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray json) {

            final NonNullList<Ingredient> ingredients = NonNullList.create();

            for (final JsonElement element : json) {

                final Ingredient ingredient = Ingredient.CODEC_NONEMPTY.parse(JsonOps.INSTANCE, element).get().orThrow();

                if (!ingredient.isEmpty()) {

                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }
    }
}
