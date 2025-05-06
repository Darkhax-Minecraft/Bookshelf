package net.darkhax.bookshelf.common.api.registry;

import net.minecraft.core.Registry;

@FunctionalInterface
public interface RegistryHandler<T> {
    void handle(Registry<T> registry);
}