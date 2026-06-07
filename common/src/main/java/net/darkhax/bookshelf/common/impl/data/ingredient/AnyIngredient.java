package net.darkhax.bookshelf.common.impl.data.ingredient;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AnyIngredient implements IngredientLogic<AnyIngredient> {

    public static final AnyIngredient INSTANCE = new AnyIngredient();
    public static final MapCodec<AnyIngredient> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, AnyIngredient> STREAM = StreamCodec.unit(INSTANCE);

    private final CachedSupplier<List<Item>> items = CachedSupplier.cache(() -> BuiltInRegistries.ITEM.stream().toList());
    private final CachedSupplier<List<ItemStack>> stacks = CachedSupplier.cache(() -> BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).toList());

    @Override
    public boolean test(ItemStack stack) {
        return true;
    }

    @Override
    public List<Item> getMatchingItems() {
        return this.items.get();
    }

    @Override
    public List<ItemStack> getAllMatchingStacks() {
        return this.stacks.get();
    }
}