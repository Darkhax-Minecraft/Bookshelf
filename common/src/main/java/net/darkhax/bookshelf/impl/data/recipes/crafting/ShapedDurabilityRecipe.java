package net.darkhax.bookshelf.impl.data.recipes.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public final class ShapedDurabilityRecipe extends ShapedRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final ShapedRecipePattern pattern;
    private final int damageAmount;
    private final ItemStack output;

    public ShapedDurabilityRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean notify, int damageAmount) {
        super(group, category, pattern, result, notify);
        this.pattern = pattern;
        this.damageAmount = damageAmount;
        this.output = result;
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

    private ShapedRecipePattern getPattern() {
        return this.pattern;
    }

    private ItemStack getRecipeOutput() {
        return this.output;
    }

    private int getDamageAmount() {
        return this.damageAmount;
    }

    public static final class Serializer implements RecipeSerializer<ShapedDurabilityRecipe> {

        public static final Codec<ShapedDurabilityRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(ShapedRecipe::getGroup),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter(ShapedDurabilityRecipe::getPattern),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(ShapedDurabilityRecipe::getRecipeOutput),
                ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(ShapedRecipe::showNotification),
                BookshelfCodecs.INT.get("damageAmount", ShapedDurabilityRecipe::getDamageAmount)
        ).apply(instance, ShapedDurabilityRecipe::new));

        @Override
        public Codec<ShapedDurabilityRecipe> codec() {

            return CODEC;
        }

        @Override
        public ShapedDurabilityRecipe fromNetwork(FriendlyByteBuf buffer) {

            final String group = buffer.readUtf();
            final CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            final ShapedRecipePattern pattern = ShapedRecipePattern.fromNetwork(buffer);
            final ItemStack output = buffer.readItem();
            final boolean showNotif = buffer.readBoolean();
            final int damageAmount = buffer.readInt();
            return new ShapedDurabilityRecipe(group, category, pattern, output, showNotif, damageAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedDurabilityRecipe recipe) {

            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());
            recipe.pattern.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeBoolean(recipe.showNotification());
            buffer.writeInt(recipe.damageAmount);
        }
    }
}