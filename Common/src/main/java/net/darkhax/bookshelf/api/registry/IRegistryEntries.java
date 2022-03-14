package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IRegistryEntries<V> extends Iterable<V> {

    <VT extends V> IRegistryObject<VT> add(Supplier<VT> value, ResourceLocation id);

    Map<ResourceLocation, V> getEntries();

    void addInsertListener(BiConsumer<ResourceLocation, IRegistryObject<? extends V>> listener);

    void addRegistryListener(BiConsumer<ResourceLocation, V> listener);

    void build(BiConsumer<ResourceLocation, V> registerFunc);

    @Nullable
    default V getEntry(ResourceLocation id) {

        return this.getEntries().get(id);
    }

    @Nullable
    default V getOrDefault(ResourceLocation key, V fallback) {

        return getOrDefault(key, () -> fallback);
    }

    @Nullable
    default V getOrDefault(ResourceLocation key, Supplier<V> fallback) {

        V value;
        return (((value = this.getEntries().get(key)) != null) || this.getEntries().containsKey(key)) ? value : fallback.get();
    }

    default void ifPresent(ResourceLocation key, BiConsumer<ResourceLocation, V> consumer) {

        final V value = this.getEntries().get(key);

        if (value != null || this.getEntries().containsKey(key)) {

            consumer.accept(key, value);
        }
    }

    default void ifAbsent(ResourceLocation key, Consumer<ResourceLocation> consumer) {

        if (!this.getEntries().containsKey(key)) {

            consumer.accept(key);
        }
    }
}