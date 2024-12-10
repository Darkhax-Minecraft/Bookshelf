package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;

import java.util.function.BiConsumer;

public record RegisterCatVariant(String owner, BiConsumer<ResourceKey<CatVariant>, ResourceLocation> func) {

    public void add(String path) {
        this.add(path, ResourceLocation.fromNamespaceAndPath(this.owner, "textures/entity/cat/" + path + ".png"));
    }

    public void add(String path, ResourceLocation texture) {
        this.func.accept(ResourceKey.create(Registries.CAT_VARIANT, ResourceLocation.fromNamespaceAndPath(this.owner, path)), texture);
    }
}
