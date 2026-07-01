package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.mojang.serialization.Codec;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public record DataRegistryAdapter(RegistrationContext context, Adapter adapter) {

    public <T> ResourceKey<Registry<T>> addSynced(String name, Codec<T> dataCodec) {
        return this.add(name, dataCodec, dataCodec);
    }

    public <T> ResourceKey<Registry<T>> addUnsynced(String name, Codec<T> dataCodec) {
        return this.add(name, dataCodec, null);
    }

    @SuppressWarnings("unchecked")
    public <T> ResourceKey<Registry<T>> add(String name, Codec<T> dataCodec, @Nullable Codec<T> syncCodec) {
        final ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(context.namespace(), name));
        adapter.register(key, dataCodec, syncCodec);
        return key;
    }

    @FunctionalInterface
    public interface Adapter<T> {
        void register(ResourceKey<Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> syncCodec);
    }
}