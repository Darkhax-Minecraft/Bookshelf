package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public final class SerializerText implements ISerializer<Component> {

    public static final ISerializer<Component> SERIALIZER = new SerializerText();

    @Override
    public Component fromJSON(JsonElement json) {

        return Component.Serializer.fromJson(json);
    }

    @Override
    public JsonElement toJSON(Component toWrite) {

        return Component.Serializer.toJsonTree(toWrite);
    }

    @Override
    public Component fromByteBuf(FriendlyByteBuf buffer) {

        final String jsonString = buffer.readUtf();
        return Component.Serializer.fromJson(jsonString);
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Component toWrite) {

        buffer.writeUtf(Component.Serializer.toJson(toWrite));
    }

    @Override
    public Tag toNBT(Component toWrite) {

        return Serializers.STRING.toNBT(Component.Serializer.toJson(toWrite));
    }

    @Override
    public Component fromNBT(Tag nbt) {

        return Component.Serializer.fromJson(Serializers.STRING.fromNBT(nbt));
    }
}