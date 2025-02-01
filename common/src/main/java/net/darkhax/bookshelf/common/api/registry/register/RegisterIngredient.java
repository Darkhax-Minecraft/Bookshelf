package net.darkhax.bookshelf.common.api.registry.register;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.darkhax.bookshelf.common.api.function.TriConsumer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("rawtypes")
public record RegisterIngredient(String owner, TriConsumer<ResourceLocation, MapCodec, StreamCodec> registryFunc) {
    public <T extends IngredientLogic<T>> void add(String path, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        this.registryFunc.accept(ResourceLocation.fromNamespaceAndPath(this.owner, path), codec, stream);
    }
}