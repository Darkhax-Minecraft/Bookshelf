package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.network.PacketBuffer;

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
    public Short read (PacketBuffer buffer) {

        return buffer.readShort();
    }

    @Override
    public void write (PacketBuffer buffer, Short toWrite) {

        buffer.writeShort(toWrite);
    }

    @Override
    public INBT writeNBT (Short toWrite) {

        return ShortNBT.valueOf(toWrite);
    }

    @Override
    public Short read (INBT nbt) {

        if (nbt instanceof NumberNBT) {

            return ((NumberNBT) nbt).getAsShort();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}