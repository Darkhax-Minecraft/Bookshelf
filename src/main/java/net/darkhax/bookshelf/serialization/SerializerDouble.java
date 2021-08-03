package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerDouble implements ISerializer<Double> {

    public static final ISerializer<Double> SERIALIZER = new SerializerDouble();

    private SerializerDouble () {

    }

    @Override
    public Double read (JsonElement json) {

        return json.getAsDouble();
    }

    @Override
    public JsonElement write (Double toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Double read (FriendlyByteBuf buffer) {

        return buffer.readDouble();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Double toWrite) {

        buffer.writeDouble(toWrite);
    }

    @Override
    public Tag writeNBT (Double toWrite) {

        return DoubleTag.valueOf(toWrite);
    }

    @Override
    public Double read (Tag nbt) {

        if (nbt instanceof DoubleTag) {

            return ((DoubleTag) nbt).getAsDouble();
        }

        throw new IllegalArgumentException("Expected NBT to be a double tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}