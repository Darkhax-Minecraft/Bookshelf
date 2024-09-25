package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record MenuRegister(String owner) {

    public void add(String path, ClientMenuFactory<? extends AbstractContainerMenu> clientFactory) {
        this.add(ResourceLocation.fromNamespaceAndPath(this.owner, path), clientFactory);
    }

    public void add(ResourceLocation id, ClientMenuFactory<? extends AbstractContainerMenu> clientFactory) {
        Services.PLATFORM.unsafeRegisterMenu(id, clientFactory);
    }

    public interface ClientMenuFactory<T extends AbstractContainerMenu> {
        T create(int containerId, Inventory playerInventory);
    }
}
