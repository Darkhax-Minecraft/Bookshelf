package net.darkhax.bookshelf.common.impl.data.ingredient;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FalseIngredient implements IngredientLogic<FalseIngredient> {

    public static final FalseIngredient SINGLETON = new FalseIngredient();
    public static final MapCodec<FalseIngredient> CODEC = MapCodec.unit(SINGLETON);
    public static final StreamCodec<RegistryFriendlyByteBuf, FalseIngredient> STREAM = StreamCodec.unit(SINGLETON);
    public static final CachedSupplier<Ingredient> INSTANCE = CachedSupplier.cache(() -> {
        final JsonObject obj = new JsonObject();
        obj.addProperty("fabric:type", "bookshelf:false");
        obj.addProperty("tag", "bookshelf:false");
        return Ingredient.CODEC.decode(JsonOps.INSTANCE, obj).getOrThrow().getFirst();
    });

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }
}