package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IRegistryReader<T> extends Iterable<T> {

    @Nullable
    T get(ResourceLocation id);

    @Nullable
    ResourceLocation getId(T value);

    default Stream<T> streamValues() {

        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<T> parallelStreamValues() {

        return StreamSupport.stream(spliterator(), true);
    }
}
