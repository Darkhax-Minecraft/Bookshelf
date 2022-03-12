package net.darkhax.bookshelf.api.registry;

import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistryObject<T> extends CachedSupplier<T> implements IRegistryObject<T> {

    private final ResourceLocation id;

    protected RegistryObject(ResourceLocation id, Supplier<T> delegate) {

        super(delegate);
        this.id = id;
    }

    @Override
    public ResourceLocation getRegistryName() {

        return this.id;
    }
}