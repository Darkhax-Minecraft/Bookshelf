package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A registry adapter that can register new types of ingredients.
 */
@SuppressWarnings("rawtypes")
public class IngredientTypeAdapter extends GenericRegistryAdapter<IngredientTypeAdapter.IngredientType> {

    public IngredientTypeAdapter(RegistrationContext context, BiConsumer<ResourceLocation, Supplier<IngredientType>> registryFunc) {
        super(context, registryFunc);
    }

    /**
     * Adds a new type of ingredient to the game.
     *
     * @param key    The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param codec  A map codec that constructs the ingredient logic from map data like JSON.
     * @param stream A ByteBuf codec that constructs the ingredient logic from network data.
     * @param <T>    The type of the ingredient logic.
     * @return A reference to the registry entry.
     */
    public <T extends IngredientLogic<T>> RegistryReference<ResourceLocation, IngredientType> add(String key, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        return this.add(key, new IngredientType<>(codec, stream));
    }

    /**
     * An internal type that holds a map codec and the ByteBuf codec for a custom ingredient type.
     *
     * @param codec  A codec that reads the ingredient from map data, like JSON.
     * @param stream A ByteBuf codec that reads the ingredient from network data.
     * @param <T>    The type of the custom ingredient logic.
     */
    public record IngredientType<T extends IngredientLogic<T>>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
    }
}