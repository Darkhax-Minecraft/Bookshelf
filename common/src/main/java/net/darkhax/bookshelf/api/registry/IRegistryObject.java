package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface IRegistryObject<T> extends Supplier<T> {

    ResourceLocation getRegistryName();
}
