package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MenuTypeAdapter extends GenericRegistryAdapter<MenuTypeAdapter.ClientMenuFactory<? extends AbstractContainerMenu>> {

    public MenuTypeAdapter(RegistrationContext context, BiConsumer<ResourceLocation, Supplier<ClientMenuFactory<? extends AbstractContainerMenu>>> registryFunc) {
        super(context, registryFunc);
    }

    public interface ClientMenuFactory<T extends AbstractContainerMenu> {
        T create(int containerId, Inventory playerInventory);
    }
}