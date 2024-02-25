package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
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
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(ShapelessRecipe::getGroup),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(ShapelessDurabilityRecipe::getOutput),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                    final Ingredient[] nonEmptyIngredients = ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                    if (nonEmptyIngredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    }
                    return nonEmptyIngredients.length > 9 ? DataResult.error(() -> "Too many ingredients for shapeless recipe") : DataResult.success(NonNullList.of(Ingredient.EMPTY, nonEmptyIngredients));
                }, DataResult::success).forGetter(ShapelessDurabilityRecipe::getIngredients),
                BookshelfCodecs.INT.get("damageAmount", ShapelessDurabilityRecipe::getDamageAmount, 1)
        ).apply(instance, ShapelessDurabilityRecipe::new));

        @Override
        public Codec<ShapelessDurabilityRecipe> codec() {

            return CODEC;
        }

        @Override
        public ShapelessDurabilityRecipe fromNetwork(FriendlyByteBuf buffer) {

            final String group = buffer.readUtf();
            final CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            final int ingredientSize = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);

            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            final int damageAmount = buffer.readVarInt();

            return new ShapelessDurabilityRecipe(group, category, output, ingredients, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapelessDurabilityRecipe toWrite) {

            buffer.writeUtf(toWrite.getGroup());
            buffer.writeEnum(toWrite.category());
            buffer.writeVarInt(toWrite.getIngredients().size());

            for (Ingredient ingredient : toWrite.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(toWrite.output);
            buffer.writeVarInt(toWrite.damageAmount);
        }
    }
}
