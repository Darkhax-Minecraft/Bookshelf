package net.darkhax.bookshelf.crafting.recipes.smithing;

import com.google.gson.JsonObject;

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

public class SmithingRecipeRepairCost extends SmithingRecipe {

    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();

    private final int reduction;

    public SmithingRecipeRepairCost (ResourceLocation recipeId, Ingredient base, Ingredient addition, int reduction) {

        super(recipeId, base, addition, ItemStack.EMPTY);
        this.reduction = reduction;
    }

    @Override
    public ItemStack assemble (IInventory inv) {

        final ItemStack stack = inv.getItem(0).copy();
        final int repairCost = Math.max(0, stack.getBaseRepairCost() - this.reduction);

        stack.setRepairCost(repairCost);
        return stack;
    }

    @Override
    public boolean matches (IInventory inv, World world) {

        return inv.getItem(0).getBaseRepairCost() > 0 && super.matches(inv, world);
    }

    @Override
    public boolean isSpecial () {

        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer () {

        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipeRepairCost> {

        @Override
        public SmithingRecipeRepairCost fromJson (ResourceLocation recipeId, JsonObject json) {

            final Ingredient base = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "base"));
            final Ingredient addition = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition"));
            final int reduction = JSONUtils.getAsInt(json, "reduction", Integer.MAX_VALUE);
            return new SmithingRecipeRepairCost(recipeId, base, addition, reduction);
        }

        @Override
        public SmithingRecipeRepairCost fromNetwork (ResourceLocation recipeId, PacketBuffer buffer) {

            final Ingredient base = Ingredient.fromNetwork(buffer);
            final Ingredient addition = Ingredient.fromNetwork(buffer);
            final int reduction = buffer.readInt();
            return new SmithingRecipeRepairCost(recipeId, base, addition, reduction);
        }

        @Override
        public void toNetwork (PacketBuffer buffer, SmithingRecipeRepairCost recipe) {

            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeInt(recipe.reduction);
        }
    }
}