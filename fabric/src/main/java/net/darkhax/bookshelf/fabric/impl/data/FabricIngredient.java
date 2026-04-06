package net.darkhax.bookshelf.fabric.impl.data;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FabricIngredient<T extends IngredientLogic<T>> implements CustomIngredient {

    private final T logic;
    private final Supplier<CustomIngredientSerializer<?>> type;

    public FabricIngredient(T logic, Supplier<CustomIngredientSerializer<?>> type) {
        this.logic = logic;
        this.type = type;
    }

    @Override
    public boolean test(@NonNull ItemStack stack) {
        return this.logic.test(stack);
    }

    @NotNull
    @Override
    public Stream<Holder<Item>> items() {
        return this.logic.getMatchingItems().stream().map(i -> i.builtInRegistryHolder());
    }

    @Override
    public boolean requiresTesting() {
        return this.logic.requiresTesting();
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return this.type.get();
    }

    public static <T extends IngredientLogic<T>> CustomIngredientSerializer<FabricIngredient<T>> make(Identifier id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        final Supplier<CustomIngredientSerializer<?>> typeLookup = () -> CustomIngredientSerializer.get(id);
        final MapCodec<FabricIngredient<T>> ingredientCodec = codec.xmap(l -> new FabricIngredient<>(l, typeLookup), i -> i.logic);
        final StreamCodec<RegistryFriendlyByteBuf, FabricIngredient<T>> ingredientStream = stream.map(l -> new FabricIngredient<>(l, typeLookup), i -> i.logic);

        return new CustomIngredientSerializer<>() {
            @NotNull
            @Override
            public Identifier getIdentifier() {
                return id;
            }

            @NotNull
            @Override
            public MapCodec<FabricIngredient<T>> getCodec() {
                return ingredientCodec;
            }

            @NotNull
            @Override
            public StreamCodec<RegistryFriendlyByteBuf, FabricIngredient<T>> getStreamCodec() {
                return ingredientStream;
            }
        };
    }
}
