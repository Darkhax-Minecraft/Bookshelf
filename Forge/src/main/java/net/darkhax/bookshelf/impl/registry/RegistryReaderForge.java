package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Iterator;

public record RegistryReaderForge<T extends IForgeRegistryEntry<T>>(IForgeRegistry<T> registry) implements IRegistryReader<T> {

    @Nullable
    @Override
    public T get(ResourceLocation id) {

        return this.registry.containsKey(id) ? this.registry.getValue(id) : null;
    }

    @Nullable
    @Override
    public ResourceLocation getId(T value) {

        return value.getRegistryName();
    }

    @Override
    public ResourceLocation getRegistryName() {

        return this.registry.getRegistryName();
    }

    @Override
    public Iterator<T> iterator() {

        return registry.iterator();
    }
}
