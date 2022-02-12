package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class RegistryReaderForge<T extends IForgeRegistryEntry<T>> implements IRegistryReader<T> {

    private final IForgeRegistry<T> registry;

    public RegistryReaderForge(IForgeRegistry<T> registry) {

        this.registry = registry;
    }

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

    @NotNull
    @Override
    public Iterator<T> iterator() {

        return registry.iterator();
    }
}
