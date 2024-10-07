package net.darkhax.bookshelf.common.api.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface IItemHooks {

    default void addCreativeTabForms(CreativeModeTab tab, Consumer<ItemStack> displayItems) {
        // NO-OP
    }
}