package net.darkhax.bookshelf.common.api.registry.adapters;

import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A basic registry adapter that can register into registries that are not standard vanilla registries.
 *
 * @param <V> The type of value held by the registry.
 */
public class GenericRegistryAdapter<V> implements RegistryAdapter<ResourceLocation, V> {

    /**
     * Context that is shared by all registry adapters owned by the same namespace.
     */
    protected final RegistrationContext context;

    /**
     * A function that accepts and registers a key and value supplier.
     */
    protected final BiConsumer<ResourceLocation, Supplier<V>> registryFunc;

    public GenericRegistryAdapter(RegistrationContext context, BiConsumer<ResourceLocation, Supplier<V>> registryFunc) {
        this.context = context;
        this.registryFunc = registryFunc;
    }

    /**
     * Adds a value to the registry. Values are not necessarily registered immediately.
     *
     * @param id    The ID to register the value under.
     * @param value A supplier that produces the value to register.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceLocation, V> add(ResourceLocation id, Supplier<V> value) {
        final CachedSupplier<V> cache = CachedSupplier.cache(value);
        this.registryFunc.accept(id, cache);
        return RegistryReference.location(id, cache);
    }

    /**
     * Adds a value to the registry. Values are not necessarily registered immediately.
     *
     * @param id    The ID to register the value under.
     * @param value The value to register.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceLocation, V> add(ResourceLocation id, V value) {
        return this.add(id, () -> value);
    }

    @Override
    public RegistryReference<ResourceLocation, V> add(String key, Supplier<V> value) {
        return this.add(ResourceLocation.fromNamespaceAndPath(this.context.namespace(), key), value);
    }
}