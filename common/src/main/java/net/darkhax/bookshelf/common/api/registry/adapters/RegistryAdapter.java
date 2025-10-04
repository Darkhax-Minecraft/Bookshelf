package net.darkhax.bookshelf.common.api.registry.adapters;

import net.darkhax.bookshelf.common.api.registry.RegistryReference;

import java.util.function.Supplier;

/**
 * Provides a loader agnostic interface for registering content.
 *
 * @param <K> The type of key used by the registry.
 * @param <V> The type of value being registered.
 */
public interface RegistryAdapter<K, V> {

    /**
     * Adds a value to the registry. Values are not necessarily registered immediately.
     *
     * @param key   The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param value The value to register.
     * @return A reference to the registry entry.
     */
    default RegistryReference<K, V> add(String key, V value) {
        return this.add(key, () -> value);
    }

    /**
     * Adds a value to the registry. Values are not necessarily registered immediately.
     *
     * @param key   The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param value A supplier that produces the value to register.
     * @return A reference to the registry entry.
     */
    RegistryReference<K, V> add(String key, Supplier<V> value);
}