package net.darkhax.bookshelf.serialization;

import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;

public class SerializerINamedTag<T> implements ISerializer<INamedTag<T>> {

    private final Function<ResourceLocation, INamedTag<T>> builder;

    public SerializerINamedTag (Function<ResourceLocation, INamedTag<T>> builder) {

        this.builder = builder;
    }

    @Override
    public INamedTag<T> read (JsonElement json) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(json);
        return this.builder.apply(id);
    }

    @Override
    public JsonElement write (INamedTag<T> toWrite) {

        return Serializers.RESOURCE_LOCATION.write(toWrite.getName());
    }

    @Override
    public INamedTag<T> read (PacketBuffer buffer) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(buffer);
        return this.builder.apply(id);
    }

    @Override
    public void write (PacketBuffer buffer, INamedTag<T> toWrite) {

        Serializers.RESOURCE_LOCATION.write(buffer, toWrite.getName());
    }
}