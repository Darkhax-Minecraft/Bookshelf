package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class ForgeRegistryRegistryHelper {

    private final String ownerId;
    private final Logger logger;

    private final Map<ResourceLocation, RegistryBuilder<?>> registries;

    public ForgeRegistryRegistryHelper (String ownerId, Logger logger) {

        this.ownerId = ownerId;
        this.logger = logger;
        this.registries = new HashMap<>();
    }

    public <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> register (String id, Class<T> type) {

        final ResourceLocation registryId = new ResourceLocation(this.ownerId, id);
        final RegistryBuilder<T> builder = new RegistryBuilder<T>().setName(registryId).setType(type);
        this.registries.put(registryId, builder);
        return builder;
    }

    public void initialize (IEventBus bus) {

        if (!this.registries.isEmpty()) {

            bus.addListener(this::registerRegistries);
        }
    }

    private void registerRegistries (RegistryEvent.NewRegistry event) {

        for (final Entry<ResourceLocation, RegistryBuilder<?>> newEntry : this.registries.entrySet()) {

            newEntry.getValue().create();
        }

        this.logger.info("Created {} new registries.", this.registries.size());
    }
}