package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

public class RegistryReaderVanilla<T> implements IRegistryReader<T> {

    private final Registry<T> registry;

    public RegistryReaderVanilla(Registry<T> registry) {

        this.registry = registry;
    }

    @Override
    public T get(ResourceLocation id) {

        return this.registry.get(id);
    }

    @Override
    public ResourceLocation getId(T value) {

        return this.registry.getKey(value);
    }

    @Override
    public ResourceLocation getRegistryName() {

        return this.registry.key().registry();
    }

    @Override
    public Iterator<T> iterator() {

        return registry.iterator();
    }
}
