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

        return this.registry.getValue(id);
    }

    @Nullable
    @Override
    public ResourceLocation getId(T value) {

        return value.getRegistryName();
    }

    @Override
    public Iterator<T> iterator() {

        return registry.iterator();
    }
}
