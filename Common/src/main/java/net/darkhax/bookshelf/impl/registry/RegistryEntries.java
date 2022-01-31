package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RegistryEntries<T> implements IRegistryEntries<T> {

    /**
     * The ID of the mod that owns this registry manager.
     */
    private final String ownerId;

    /**
     * A map containing all values registered through the registry manager.
     */
    private final Map<ResourceLocation, T> values;

    /**
     * An unmodifiable view of the {@link #values} map.
     */
    private final Map<ResourceLocation, T> valuesUnmodifiable;

    /**
     * A list of insert listeners registered to the manager.
     */
    private final List<BiConsumer<ResourceLocation, T>> insertListeners;

    /**
     * Creates a new registry manager.
     *
     * @param ownerId The ID of the mod that owns the registry manager.
     */
    public RegistryEntries(String ownerId) {

        this.ownerId = ownerId;
        this.values = new LinkedHashMap<>();
        this.valuesUnmodifiable = Collections.unmodifiableMap(this.values);
        this.insertListeners = new LinkedList<>();
    }

    @Override
    public <VT extends T> VT add(VT value, ResourceLocation id) {

        if (this.values.containsKey(id)) {

            throw new IllegalArgumentException("The ID " + id.toString() + " has already been registered.");
        }

        this.values.put(id, value);
        this.insertListeners.forEach(listener -> listener.accept(id, value));
        return value;
    }

    @Override
    public Map<ResourceLocation, T> getEntries() {

        return this.valuesUnmodifiable;
    }

    @Override
    public String getOwner() {

        return this.ownerId;
    }

    @Override
    public boolean isEmpty() {

        return this.values.isEmpty();
    }

    @Override
    public void addInsertListener(BiConsumer<ResourceLocation, T> listener) {

        this.insertListeners.add(listener);
    }
}