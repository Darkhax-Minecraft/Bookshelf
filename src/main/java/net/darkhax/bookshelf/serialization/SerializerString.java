package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerString implements ISerializer<String> {

    public static final ISerializer<String> SERIALIZER = new SerializerString();

    private SerializerString () {

    }

    @Override
    public String read (JsonElement json) {

        return json.getAsString();
    }

    @Override
    public JsonElement write (String toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public String read (FriendlyByteBuf buffer) {

        return buffer.readUtf();
    }

    @Override
    public void write (FriendlyByteBuf buffer, String toWrite) {

        buffer.writeUtf(toWrite);
    }

    @Override
    public Tag writeNBT (String toWrite) {

        return StringTag.valueOf(toWrite);
    }

    @Override
    public String read (Tag nbt) {

        if (nbt instanceof StringTag) {

            return ((StringTag) nbt).getAsString();
        }

        throw new IllegalArgumentException("Expected NBT to be a string tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}