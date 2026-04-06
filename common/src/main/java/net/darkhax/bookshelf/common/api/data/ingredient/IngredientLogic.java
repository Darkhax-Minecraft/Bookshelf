package net.darkhax.bookshelf.common.api.data.ingredient;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface IngredientLogic<T extends IngredientLogic<T>> {

    boolean test(ItemStack stack);

    default List<Item> getMatchingItems() {
        final List<Item> matching = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            final ItemStack stack = item.getDefaultInstance();
            if (this.test(stack)) {
                matching.add(item);
            }
        }
        return matching;
    }

    default List<ItemStack> getAllMatchingStacks() {
        final List<ItemStack> matching = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            final ItemStack stack = item.getDefaultInstance();
            if (this.test(stack)) {
                matching.add(stack);
            }
        }
        return matching;
    }

    default boolean requiresTesting() {
        return true;
    }
}