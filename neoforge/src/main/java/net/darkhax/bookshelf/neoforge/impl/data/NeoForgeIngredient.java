package net.darkhax.bookshelf.neoforge.impl.data;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class NeoForgeIngredient<T extends IngredientLogic<T>> implements ICustomIngredient {

    private final T logic;
    private final Supplier<IngredientType<?>> type;

    public NeoForgeIngredient(T logic, Supplier<IngredientType<?>> type) {
        this.logic = logic;
        this.type = type;
    }

    @Override
    public boolean test(@NotNull ItemStack stack) {
        return this.logic.test(stack);
    }

    @NotNull
    @Override
    public Stream<ItemStack> getItems() {
        return this.logic.getAllMatchingStacks().stream();
    }

    @Override
    public boolean isSimple() {
        return !this.logic.requiresTesting();
    }

    @NotNull
    @Override
    public IngredientType<?> getType() {
        return this.type.get();
    }

    public static <T extends IngredientLogic<T>> IngredientType<NeoForgeIngredient<T>> makeIngredientType(ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        final Supplier<IngredientType<?>> typeLookup = () -> NeoForgeRegistries.INGREDIENT_TYPES.get(id);
        final MapCodec<NeoForgeIngredient<T>> ingredientCodec = codec.xmap(l -> new NeoForgeIngredient<>(l, typeLookup), i -> i.logic);
        final StreamCodec<RegistryFriendlyByteBuf, NeoForgeIngredient<T>> ingredientStream = stream.map(l -> new NeoForgeIngredient<>(l, typeLookup), i -> i.logic);
        return new IngredientType<>(ingredientCodec, ingredientStream);
    }
}