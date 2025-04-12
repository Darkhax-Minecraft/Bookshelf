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

public class AllOfIngredient implements IngredientLogic<AllOfIngredient> {

    public static final MapCodec<AllOfIngredient> CODEC = MapCodecs.flexibleList(Ingredient.CODEC).xmap(AllOfIngredient::new, i -> i.ingredients).fieldOf("ingredients");
    public static final StreamCodec<RegistryFriendlyByteBuf, AllOfIngredient> STREAM = StreamCodecs.list(StreamCodecs.INGREDIENT_NON_EMPTY).map(AllOfIngredient::new, v -> v.ingredients);

    private final List<Ingredient> ingredients;

    public AllOfIngredient(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean test(ItemStack stack) {
        for (Ingredient ingredient : this.ingredients) {
            if (!ingredient.test(stack)) {
                return false;
            }
        }
        return true;
    }
}
