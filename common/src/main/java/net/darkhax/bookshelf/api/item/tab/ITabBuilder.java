package net.darkhax.bookshelf.api.item.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface ITabBuilder {

    ITabBuilder title(Component tabTitle);

    ITabBuilder icon(Supplier<ItemStack> supplier);

    ITabBuilder displayItems(IDisplayGenerator displayGen);

    ITabBuilder hideTitle();

    ITabBuilder noScrollBar();

    ITabBuilder backgroundSuffix(String background);

    CreativeModeTab build();
}