package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public record Register<T>(String owner, BiConsumer<ResourceLocation, T> registerFunc) {

    public void add(String path, T value) {
        this.add(ResourceLocation.fromNamespaceAndPath(this.owner, path), value);
    }

    public void add(ResourceLocation id, T value) {
        this.registerFunc.accept(id, value);
    }
}