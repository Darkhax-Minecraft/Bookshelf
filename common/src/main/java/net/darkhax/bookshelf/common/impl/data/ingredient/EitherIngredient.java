package net.darkhax.bookshelf.common.impl.data.ingredient;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.codecs.stream.StreamCodecs;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EitherIngredient implements IngredientLogic<EitherIngredient> {

    public static final MapCodec<EitherIngredient> CODEC = MapCodecs.flexibleList(Ingredient.CODEC).xmap(EitherIngredient::new, i -> i.ingredients).fieldOf("ingredients");
    public static final StreamCodec<RegistryFriendlyByteBuf, EitherIngredient> STREAM = StreamCodecs.list(StreamCodecs.INGREDIENT_NON_EMPTY).map(EitherIngredient::new, v -> v.ingredients);

    private final List<Ingredient> ingredients;

    public EitherIngredient(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean test(ItemStack stack) {
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.test(stack)) {
                return true;
            }
        }
        return false;
    }
}
