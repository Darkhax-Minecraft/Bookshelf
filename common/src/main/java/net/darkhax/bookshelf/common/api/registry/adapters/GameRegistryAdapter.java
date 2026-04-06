package net.darkhax.bookshelf.common.api.registry.adapters;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A basic registry adapter that can register into most vanilla style registries.
 *
 * @param <V> The type of value held by the registry.
 */
public class GameRegistryAdapter<V> implements RegistryAdapter<ResourceKey<V>, V> {

    /**
     * Context that is shared by all registry adapters owned by the same namespace.
     */
    protected final RegistrationContext context;

    /**
     * The id of the registry being adapted.
     */
    protected final ResourceKey<Registry<V>> registryKey;

    /**
     * A function that accepts and registers a key and value supplier.
     */
    protected final BiConsumer<ResourceKey<V>, Supplier<V>> registryFunc;

    public GameRegistryAdapter(RegistrationContext context, ResourceKey<Registry<V>> registryKey, BiConsumer<ResourceKey<V>, Supplier<V>> registryFunc) {
        this.context = context;
        this.registryKey = registryKey;
        this.registryFunc = registryFunc;
    }

    @Override
    public RegistryReference<ResourceKey<V>, V> add(String key, Supplier<V> value) {
        final ResourceKey<V> resourceKey = ResourceKey.create(registryKey, Identifier.fromNamespaceAndPath(this.context.namespace(), key));
        this.registryFunc.accept(resourceKey, value);
        return RegistryReference.resource(resourceKey);
    }

    /**
     * Creates a new Identifier using the current namespace.
     *
     * @param key The path of the Identifier.
     * @return A new Identifier with the current namespace.
     */
    public final Identifier id(String key) {
        return Identifier.fromNamespaceAndPath(this.context.namespace(), key);
    }
}