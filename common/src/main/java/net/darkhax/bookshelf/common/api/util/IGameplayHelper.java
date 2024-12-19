package net.darkhax.bookshelf.common.api.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IGameplayHelper {

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