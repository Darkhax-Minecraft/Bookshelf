package net.darkhax.bookshelf.impl.data.recipes;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.data.recipes.IRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WrappedRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {

    private final IRecipeSerializer<T> delegate;

    public WrappedRecipeSerializer(IRecipeSerializer<T> delegate) {

        this.delegate = delegate;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json) {

        return this.delegate.fromJson(recipeId, json);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

        return this.delegate.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T toWrite) {

        this.delegate.toNetwork(buffer, toWrite);
    }
}