package net.darkhax.bookshelf.crafting.recipes.smithing;

import com.google.gson.JsonObject;

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

public class SmithingRecipeRepairCost extends UpgradeRecipe {

    public static final RecipeSerializer<?> SERIALIZER = new Serializer();

    private final int reduction;

    public SmithingRecipeRepairCost (ResourceLocation recipeId, Ingredient base, Ingredient addition, int reduction) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.reduction = reduction;
    }

    @Override
    public ItemStack assemble (Container inv) {

        final ItemStack stack = inv.getItem(0).copy();
        final int repairCost = Math.max(0, stack.getBaseRepairCost() - this.reduction);

        stack.setRepairCost(repairCost);
        return stack;
    }

    @Override
    public boolean matches (Container inv, Level world) {

        return inv.getItem(0).getBaseRepairCost() > 0 && super.matches(inv, world);
    }

    @Override
    public boolean isSpecial () {

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SmithingRecipeRepairCost> {

        @Override
        public SmithingRecipeRepairCost fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            final int reduction = GsonHelper.getAsInt(json, "reduction", Integer.MAX_VALUE);
            return new SmithingRecipeRepairCost(recipeId, base, addition, reduction);
        }

        @Override
        public SmithingRecipeRepairCost fromNetwork (ResourceLocation recipeId, FriendlyByteBuf buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final int reduction = buffer.readInt();
            return new SmithingRecipeRepairCost(recipeId, base, addition, reduction);
        }

        @Override
        public void toNetwork (FriendlyByteBuf buffer, SmithingRecipeRepairCost recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeInt(recipe.reduction);
        }
    }
}