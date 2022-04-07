package net.darkhax.bookshelf.impl.data.recipes;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.data.recipes.IRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class WrappedRecipeSerializer<T extends Recipe<?>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {

    private final IRecipeSerializer<T> delegate;

    public WrappedRecipeSerializer(IRecipeSerializer<T> delegate) {

        this.delegate = delegate;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json) {

        return delegate.fromJson(recipeId, json);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf byteBuf) {

        return delegate.fromNetwork(recipeId, byteBuf);
    }

    @Override
    public void toNetwork(FriendlyByteBuf byteBuf, T toWrite) {

        delegate.toNetwork(byteBuf, toWrite);
    }
}