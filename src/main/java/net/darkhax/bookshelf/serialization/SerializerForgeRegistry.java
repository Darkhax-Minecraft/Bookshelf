package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class SerializerForgeRegistry<V extends IForgeRegistryEntry<V>> implements ISerializer<V> {

    private final IForgeRegistry<V> registry;

    public SerializerForgeRegistry (IForgeRegistry<V> registry) {

        this.registry = registry;
    }

    @Override
    public V read (JsonElement json) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(json);
        return this.getFromId(id);
    }

    @Override
    public JsonElement write (V toWrite) {

        return Serializers.RESOURCE_LOCATION.write(toWrite.getRegistryName());
    }

    @Override
    public V read (FriendlyByteBuf buffer) {

        final ResourceLocation id = buffer.readResourceLocation();
        return this.getFromId(id);
    }

    @Override
    public void write (FriendlyByteBuf buffer, V toWrite) {

        buffer.writeResourceLocation(toWrite.getRegistryName());
    }

    @Override
    public Tag writeNBT (V toWrite) {

        return Serializers.RESOURCE_LOCATION.writeNBT(toWrite.getRegistryName());
    }

    @Override
    public V read (Tag nbt) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(nbt);
        return this.getFromId(id);
    }

    private final V getFromId (ResourceLocation id) {

        if (this.registry.containsKey(id)) {

            return this.registry.getValue(id);
        }

        throw new JsonParseException("Could not find " + this.registry.getRegistryName().toString() + " with ID " + id.toString());
    }
}