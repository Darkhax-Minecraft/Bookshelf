package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.mojang.serialization.Codec;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public record DataRegistryAdapter(RegistrationContext context, Adapter adapter) {

    public <T> ResourceKey<Registry<T>> addSynced(String name, Codec<T> dataCodec) {
        return this.add(name, dataCodec, dataCodec);
    }

    public <T> ResourceKey<Registry<T>> addSynced(ResourceKey<Registry<T>> key, Codec<T> dataCodec) {
        return this.add(key, dataCodec, dataCodec);
    }

    public <T> ResourceKey<Registry<T>> addUnsynced(String name, Codec<T> dataCodec) {
        return this.add(name, dataCodec, null);
    }

    public <T> ResourceKey<Registry<T>> addUnsynced(ResourceKey<Registry<T>> key, Codec<T> dataCodec) {
        return this.add(key, dataCodec, null);
    }

    public <T> ResourceKey<Registry<T>> add(String name, Codec<T> dataCodec, @Nullable Codec<T> syncCodec) {
        final ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(context.namespace(), name));
        return add(key, dataCodec, syncCodec);
    }

    @SuppressWarnings("unchecked")
    public <T> ResourceKey<Registry<T>> add(ResourceKey<Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> syncCodec) {
        if (!key.identifier().getNamespace().equals(context.namespace())) {
            BookshelfMod.LOG.warn("Mod {} attempted to create registry '{}' with an irregular namespace.", context, key.identifier());
        }
        adapter.register(key, dataCodec, syncCodec);
        return key;
    }

    @FunctionalInterface
    public interface Adapter<T> {
        void register(ResourceKey<Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> syncCodec);
    }
}