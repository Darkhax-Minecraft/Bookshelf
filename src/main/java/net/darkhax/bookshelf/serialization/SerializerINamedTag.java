package net.darkhax.bookshelf.serialization;

import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.Tag.Named;
import net.minecraft.resources.ResourceLocation;

public class SerializerINamedTag<T> implements ISerializer<Named<T>> {

    private final Function<ResourceLocation, Named<T>> builder;

    public SerializerINamedTag (Function<ResourceLocation, Named<T>> builder) {

        this.builder = builder;
    }

    @Override
    public Named<T> read (JsonElement json) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(json);
        return this.builder.apply(id);
    }

    @Override
    public JsonElement write (Named<T> toWrite) {

        return Serializers.RESOURCE_LOCATION.write(toWrite.getName());
    }

    @Override
    public Named<T> read (FriendlyByteBuf buffer) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(buffer);
        return this.builder.apply(id);
    }

    @Override
    public void write (FriendlyByteBuf buffer, Named<T> toWrite) {

        Serializers.RESOURCE_LOCATION.write(buffer, toWrite.getName());
    }
}