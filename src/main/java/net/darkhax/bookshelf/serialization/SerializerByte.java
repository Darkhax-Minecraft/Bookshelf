package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerByte implements ISerializer<Byte> {

    public static final ISerializer<Byte> SERIALIZER = new SerializerByte();

    private SerializerByte () {

    }

    @Override
    public Byte read (JsonElement json) {

        return json.getAsByte();
    }

    @Override
    public JsonElement write (Byte toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Byte read (FriendlyByteBuf buffer) {

        return buffer.readByte();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Byte toWrite) {

        buffer.writeByte(toWrite);
    }

    @Override
    public Tag writeNBT (Byte toWrite) {

        return ByteTag.valueOf(toWrite);
    }

    @Override
    public Byte read (Tag nbt) {

        if (nbt instanceof NumericTag) {

            return ((NumericTag) nbt).getAsByte();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}