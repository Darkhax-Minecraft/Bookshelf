package net.darkhax.bookshelf.api.registry;

import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RegistryEntries<V> implements IOwnedRegistryEntries<V> {

    private final CachedSupplier<String> ownerId;
    private final Map<ResourceLocation, IRegistryObject<? extends V>> rawValues = new LinkedHashMap<>();
    private final Map<ResourceLocation, V> entries = new LinkedHashMap<>();
    private final Map<ResourceLocation, V> unmodifiableEntries = Collections.unmodifiableMap(entries);
    private final List<BiConsumer<ResourceLocation, V>> registryListeners = new LinkedList<>();

    private boolean built = false;

    public RegistryEntries(String ownerId) {

        this(() -> ownerId);
    }

    public RegistryEntries(Supplier<String> idProvider) {

        this.ownerId = CachedSupplier.cache(idProvider);
    }

    @Override
    public String getOwner() {

        return this.ownerId.get();
    }

    @Override
    public <VT extends V> IRegistryObject<VT> add(Supplier<VT> value, ResourceLocation id) {

        if (this.built) {

            throw new IllegalStateException("This registry has already been built. New values are not being supported. Owner=" + this.getOwner() + " id=" + id);
        }

        if (this.rawValues.containsKey(id)) {

            throw new IllegalStateException("The ID " + id + " has already been registered.");
        }

        final IRegistryObject<VT> registryObject = new RegistryObject<VT>(id, value);
        this.rawValues.put(id, registryObject);
        return registryObject;
    }

    @Override
    public Map<ResourceLocation, V> getEntries() {

        if (!this.built) {

            throw new IllegalStateException("Attempted to access registry values before they were built. Owner=" + this.ownerId);
        }

        return this.unmodifiableEntries;
    }

    @Override
    public void addRegistryListener(BiConsumer<ResourceLocation, V> listener) {

        if (this.built) {

            throw new IllegalStateException("Attempted to define registry listener after entries have already been built. Owner=" + this.ownerId);
        }

        this.registryListeners.add(listener);
    }

    @Override
    public void build(BiConsumer<ResourceLocation, V> registerFunc) {

        this.built = true;
        this.entries.clear();
        this.rawValues.forEach((k, v) -> {

            final V value = v.get();

            if (value != null) {

                this.entries.put(k, value);
            }

            registerFunc.accept(k, value);
            this.registryListeners.forEach(listener -> listener.accept(k, value));
        });
    }

    @Override
    public Iterator<V> iterator() {

        return this.getEntries().values().iterator();
    }
}