package net.darkhax.bookshelf.api.item.tab;

import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
public interface IDisplayGenerator {

    void display(CreativeModeTab.ItemDisplayParameters params, IOutputWrapper output);
}