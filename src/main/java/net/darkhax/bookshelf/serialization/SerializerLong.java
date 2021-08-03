package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerLong implements ISerializer<Long> {

    public static final ISerializer<Long> SERIALIZER = new SerializerLong();

    private SerializerLong () {

    }

    @Override
    public Long read (JsonElement json) {

        return json.getAsLong();
    }

    @Override
    public JsonElement write (Long toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Long read (FriendlyByteBuf buffer) {

        return buffer.readLong();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Long toWrite) {

        buffer.writeLong(toWrite);
    }

    @Override
    public Tag writeNBT (Long toWrite) {

        return LongTag.valueOf(toWrite);
    }

    @Override
    public Long read (Tag nbt) {

        if (nbt instanceof NumericTag) {

            return ((NumericTag) nbt).getAsLong();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}