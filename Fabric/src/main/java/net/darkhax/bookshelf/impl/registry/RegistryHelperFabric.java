package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryHelper;
import net.darkhax.bookshelf.impl.item.CreativeTabBuilderFabric;
import net.darkhax.bookshelf.impl.resources.WrappedReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class RegistryHelperFabric extends RegistryHelper {

    public RegistryHelperFabric(String ownerId) {

        super(ownerId);
    }

    @Override
    public ICreativeTabBuilder<?> createTabBuilder(String tabId) {

        return new CreativeTabBuilderFabric(new ResourceLocation(this.ownerId, tabId));
    }

    @Override
    public void init() {

        this.consumeVanillaRegistry(blocks, Registry.BLOCK);
        this.consumeVanillaRegistry(items, Registry.ITEM);
        this.consumeVanillaRegistry(enchantments, Registry.ENCHANTMENT);
        this.consumeVanillaRegistry(paintings, Registry.MOTIVE);
        this.consumeVanillaRegistry(mobEffects, Registry.MOB_EFFECT);
        this.consumeVanillaRegistry(attributes, Registry.ATTRIBUTE);
        this.consumeVanillaRegistry(villagerProfessions, Registry.VILLAGER_PROFESSION);

        this.serverReloadListeners.getEntries().forEach((k, v) -> ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new WrappedReloadListener(k, v)));

        if (Services.PLATFORM.isPhysicalClient()) {

            this.clientReloadListeners.getEntries().forEach((k, v) -> ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedReloadListener(k, v)));
        }
    }

    private <T> void consumeVanillaRegistry(IRegistryEntries<T> toRegister, Registry<T> registry) {

        toRegister.getEntries().forEach((id, value) -> Registry.register(registry, id, value));
    }
}