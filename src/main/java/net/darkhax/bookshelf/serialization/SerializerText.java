package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public final class SerializerText implements ISerializer<Component> {

    public static final ISerializer<Component> SERIALIZER = new SerializerText();

    @Override
    public Component read (JsonElement json) {

        return Component.Serializer.fromJson(json);
    }

    @Override
    public JsonElement write (Component toWrite) {

        return Component.Serializer.toJsonTree(toWrite);
    }

    @Override
    public Component read (FriendlyByteBuf buffer) {

        final String jsonString = buffer.readUtf();
        return Component.Serializer.fromJson(jsonString);
    }

    @Override
    public void write (FriendlyByteBuf buffer, Component toWrite) {

        buffer.writeUtf(Component.Serializer.toJson(toWrite));
    }
}