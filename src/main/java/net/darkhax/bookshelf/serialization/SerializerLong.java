package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.network.PacketBuffer;

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
    public Long read (PacketBuffer buffer) {

        return buffer.readLong();
    }

    @Override
    public void write (PacketBuffer buffer, Long toWrite) {

        buffer.writeLong(toWrite);
    }

    @Override
    public INBT writeNBT (Long toWrite) {

        return LongNBT.valueOf(toWrite);
    }

    @Override
    public Long read (INBT nbt) {

        if (nbt instanceof NumberNBT) {

            return ((NumberNBT) nbt).getAsLong();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}