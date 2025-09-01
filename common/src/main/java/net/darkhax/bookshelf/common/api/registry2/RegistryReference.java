package net.darkhax.bookshelf.common.api.registry2;

import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents an entry in a game registry.
 *
 * @param key   The key the value was registered with.
 * @param value A supplier that produces the registered value.
 * @param <K>   The type of the registry key.
 * @param <V>   The type of the registered value.
 */
public record RegistryReference<K, V>(K key, CachedSupplier<V> value) {

    /**
     * A helper method that produces a reference for a registry that uses ResourceLocation based keys.
     *
     * @param key   The key the value was registered with.
     * @param value A supplier that produces the registered value.
     * @param <V>   The type of the registered value.
     * @return A reference to the registry entry.
     */
    public static <V> RegistryReference<ResourceLocation, V> location(ResourceLocation key, CachedSupplier<V> value) {
        return new RegistryReference<>(key, value);
    }

    /**
     * A helper method that produces a reference for a registry that uses ResourceKey.
     *
     * @param key The key to lookup.
     * @param <V> The type of the value held in the registry.
     * @return A reference to a value in a registry.
     */
    public static <V> RegistryReference<ResourceKey<V>, V> resource(ResourceKey<V> key) {
        return new RegistryReference<>(key, CachedSupplier.of(key));
    }

    /**
     * A helper method that produces a reference for a registry that uses ResourceKey.
     *
     * @param registryKey The key for the registry the value is registered in.
     * @param key         The key the value was registered with.
     * @param value       A supplier that produces the registered value.
     * @param <V>         The type of the registered value.
     * @return A reference to the registry entry.
     */
    public static <V> RegistryReference<ResourceKey<V>, V> resource(ResourceKey<? extends Registry<V>> registryKey, ResourceLocation key, CachedSupplier<V> value) {
        return new RegistryReference<>(ResourceKey.create(registryKey, key), value);
    }

    /**
     * A helper method that produces a reference for a registry that uses ResourceKey.
     *
     * @param registry The registry the value is registered in.
     * @param key      The key the value was registered with.
     * @param value    A supplier that produces the registered value.
     * @param <V>      The type of the registered value.
     * @return A reference to the registry entry.
     */
    public static <V> RegistryReference<ResourceKey<V>, V> resource(Registry<V> registry, ResourceLocation key, CachedSupplier<V> value) {
        return resource(registry.key(), key, value);
    }
}