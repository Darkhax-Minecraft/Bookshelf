package net.darkhax.bookshelf.forge.impl.util;

import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ForgeGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack getCraftingRemainder(ItemStack input) {
        final Item item = input.getItem();
        return item.hasCraftingRemainingItem(input) ? item.getCraftingRemainingItem(input) : ItemStack.EMPTY;
    }
}