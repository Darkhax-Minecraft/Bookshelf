package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerShort implements ISerializer<Short> {

    public static final ISerializer<Short> SERIALIZER = new SerializerShort();

    private SerializerShort () {

    }

    @Override
    public Short read (JsonElement json) {

        return json.getAsShort();
    }

    @Override
    public JsonElement write (Short toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Short read (FriendlyByteBuf buffer) {

        return buffer.readShort();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Short toWrite) {

        buffer.writeShort(toWrite);
    }

    @Override
    public Tag writeNBT (Short toWrite) {

        return ShortTag.valueOf(toWrite);
    }

    @Override
    public Short read (Tag nbt) {

        if (nbt instanceof NumericTag) {

            return ((NumericTag) nbt).getAsShort();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}