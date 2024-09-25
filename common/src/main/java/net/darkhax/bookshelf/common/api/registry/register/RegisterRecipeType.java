package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public record RegisterRecipeType(String owner, Consumer<ResourceLocation> registerFunc) {

    public void add(String path) {
        this.add(ResourceLocation.fromNamespaceAndPath(this.owner, path));
    }

    public void add(ResourceLocation id) {
        this.registerFunc.accept(id);
    }
}