package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.function.Supplier;

public class ClassRegistryEntries<V> extends RegistryEntries<Tuple<Class, V>> {

    public ClassRegistryEntries(String ownerId) {

        super(ownerId);
    }

    public ClassRegistryEntries(Supplier<String> idProvider) {

        super(idProvider);
    }

    public void add(Class clazz, Supplier<V> instance, String id) {

        this.add(() -> new Tuple<>(clazz, instance.get()), id);
    }

    public void add(Class clazz, Supplier<V> instance, ResourceLocation id) {

        this.add(() -> new Tuple<>(clazz, instance.get()), id);
    }
}
