package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.mixin.accessors.item.crafting.AccessorShapedRecipe;
import net.darkhax.bookshelf.mixin.accessors.item.crafting.AccessorShapedRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ShapedDurabilityRecipe extends ShapedRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final int damageAmount;
    private final ItemStack output;

    public ShapedDurabilityRecipe(String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> input, ItemStack output, boolean showNotification, int damageAmount) {

        super(group, category, width, height, input, output, showNotification);
        this.damageAmount = damageAmount;
        this.output = output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {

        return SERIALIZER;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {

        final NonNullList<ItemStack> keptItems = super.getRemainingItems(inv);
        return Services.INVENTORY_HELPER.keepDamageableItems(inv, keptItems, this.damageAmount);
    }

    public static final class Serializer implements RecipeSerializer<ShapedDurabilityRecipe> {

        public static final Codec<ShapedDurabilityRecipe> CODEC = RawData.CODEC.flatXmap(rawData -> {

            String[] pattern = AccessorShapedRecipe.bookshelf$shrink(rawData.pattern);
            int recipeWidth = pattern[0].length();
            int recipeHeight = pattern.length;
            NonNullList<Ingredient> inputGrid = NonNullList.withSize(recipeWidth * recipeHeight, Ingredient.EMPTY);
            Set<String> unresolvedKeys = Sets.newHashSet(rawData.keys.keySet());

            for (int x = 0; x < pattern.length; x++) {

                final String patternRow = pattern[x];

                for (int y = 0; y < patternRow.length(); y++) {

                    final String patternKey = patternRow.substring(y, y + 1);
                    final Ingredient matchingIngredient = patternKey.equals(" ") ? Ingredient.EMPTY : rawData.keys.get(patternKey);

                    if (matchingIngredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + patternKey + "' but it's not defined in the key");
                    }

                    unresolvedKeys.remove(patternKey);
                    inputGrid.set(y + recipeWidth * x, matchingIngredient);
                }
            }

            if (!unresolvedKeys.isEmpty()) {

                return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + unresolvedKeys);
            }

            else {
                return DataResult.success(new ShapedDurabilityRecipe(rawData.group, rawData.category, recipeWidth, recipeHeight, inputGrid, rawData.result, rawData.showNotification, rawData.damageAmount));
            }
        }, (data) -> {
            throw new NotImplementedException("Serializing ShapedRecipe is not implemented yet.");
        });

        @Override
        public Codec<ShapedDurabilityRecipe> codec() {

            return CODEC;
        }

        @Override
        public ShapedDurabilityRecipe fromNetwork(FriendlyByteBuf buffer) {

            final int width = buffer.readInt();
            final int height = buffer.readInt();
            final String group = buffer.readUtf();

            final NonNullList<Ingredient> input = NonNullList.withSize(width * height, Ingredient.EMPTY);

            for (int i = 0; i < input.size(); i++) {

                input.set(i, Ingredient.fromNetwork(buffer));
            }

            final ItemStack output = buffer.readItem();
            final boolean showNotification = buffer.readBoolean();
            final int damageAmount = buffer.readInt();

            final CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);

            return new ShapedDurabilityRecipe(group, category, width, height, input, output, showNotification, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedDurabilityRecipe recipe) {

            buffer.writeInt(recipe.getWidth());
            buffer.writeInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());

            for (final Ingredient ingredient : recipe.getIngredients()) {

                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeBoolean(recipe.showNotification());
            buffer.writeInt(recipe.damageAmount);

            buffer.writeEnum(recipe.category());
        }

        private record RawData(String group, CraftingBookCategory category, Map<String, Ingredient> keys, List<String> pattern, ItemStack result, boolean showNotification, int damageAmount) {

            public static final Codec<RawData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(RawData::group),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(RawData::category),
                    ExtraCodecs.strictUnboundedMap(AccessorShapedRecipeSerializer.bookshelf$getSingleCharacterStringCodec(), Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(RawData::keys),
                    AccessorShapedRecipeSerializer.bookshelf$getPatternCodec().fieldOf("pattern").forGetter(RawData::pattern),
                    CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("results").forGetter(RawData::result),
                    ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(RawData::showNotification),
                    Codec.INT.fieldOf("damageAmount").orElse(1).forGetter(RawData::damageAmount)
            ).apply(instance, RawData::new));
        }
    }
}