package net.darkhax.bookshelf.common.api.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IGameplayHelper {

    RandomSource RNG = RandomSource.create();

    default ItemStack getCraftingRemainder(ItemStack input) {
        if (input.getItem().hasCraftingRemainingItem()) {
            final Item remainder = input.getItem().getCraftingRemainingItem();
            if (remainder != null) {
                return remainder.getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }
}