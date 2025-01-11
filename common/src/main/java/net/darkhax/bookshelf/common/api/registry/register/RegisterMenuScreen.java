package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class RegisterMenuScreen {

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bind(MenuType<? extends M> type, ScreenFactory<M, U> factory) {
        Services.GAMEPLAY.bindMenu(type, factory);
    }

    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T menu, Inventory playerInv, Component title);
    }
}