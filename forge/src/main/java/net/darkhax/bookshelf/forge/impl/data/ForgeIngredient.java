package net.darkhax.bookshelf.forge.impl.data;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeIngredient<T extends IngredientLogic<T>> extends AbstractIngredient {

    private final T logic;
    private final Supplier<IIngredientSerializer<?>> type;

    public ForgeIngredient(T logic, Supplier<IIngredientSerializer<?>> type) {
        this.logic = logic;
        this.type = type;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.logic.test(stack);
    }

    @Override
    public boolean isSimple() {
        return !logic.requiresTesting();
    }

    @Override
    public ItemStack[] getItems() {
        return this.logic.getAllMatchingStacks().toArray(ItemStack[]::new);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return this.type.get();
    }

    public static <T extends IngredientLogic<T>> IIngredientSerializer<ForgeIngredient<T>> makeIngredientType(ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        final Supplier<IIngredientSerializer<?>> typeLookup = () -> ForgeRegistries.INGREDIENT_SERIALIZERS.get().getValue(id);
        final MapCodec<ForgeIngredient<T>> ingredientCodec = codec.xmap(l -> new ForgeIngredient<>(l, typeLookup), i -> i.logic);
        final StreamCodec<RegistryFriendlyByteBuf, ForgeIngredient<T>> ingredientStream = stream.map(l -> new ForgeIngredient<>(l, typeLookup), i -> i.logic);
        return new IIngredientSerializer<>() {
            @Override
            public MapCodec<? extends ForgeIngredient<T>> codec() {
                return ingredientCodec;
            }

            @Override
            public void write(RegistryFriendlyByteBuf buffer, ForgeIngredient<T> value) {
                ingredientStream.encode(buffer, value);
            }

            @Override
            public ForgeIngredient<T> read(RegistryFriendlyByteBuf buffer) {
                return ingredientStream.decode(buffer);
            }
        };
    }
}
