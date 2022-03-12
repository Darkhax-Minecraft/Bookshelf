package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface IOwnedRegistryEntries<V> extends IRegistryEntries<V> {

    String getOwner();

    default <VT extends V> IRegistryObject<VT> add(Supplier<VT> value, String id) {

        return this.add(value, new ResourceLocation(this.getOwner(), id));
    }
}