package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerFloat implements ISerializer<Float> {

    public static final ISerializer<Float> SERIALIZER = new SerializerFloat();

    private SerializerFloat () {

    }

    @Override
    public Float read (JsonElement json) {

        return json.getAsFloat();
    }

    @Override
    public JsonElement write (Float toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Float read (FriendlyByteBuf buffer) {

        return buffer.readFloat();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Float toWrite) {

        buffer.writeFloat(toWrite);
    }

    @Override
    public Tag writeNBT (Float toWrite) {

        return FloatTag.valueOf(toWrite);
    }

    @Override
    public Float read (Tag nbt) {

        if (nbt instanceof NumericTag) {

            return ((NumericTag) nbt).getAsFloat();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}