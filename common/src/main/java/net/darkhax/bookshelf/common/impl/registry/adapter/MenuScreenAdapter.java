package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
public record MenuScreenAdapter(BiConsumer<MenuType, ScreenFactory> func) {

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bind(MenuType<? extends M> type, ScreenFactory<M, U> factory) {
        func.accept(type, factory);
    }

    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T menu, Inventory playerInv, Component title);
    }
}