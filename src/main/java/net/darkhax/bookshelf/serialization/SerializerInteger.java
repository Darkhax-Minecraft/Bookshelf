package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.network.PacketBuffer;

public final class SerializerInteger implements ISerializer<Integer> {

    public static final ISerializer<Integer> SERIALIZER = new SerializerInteger();

    private SerializerInteger () {

    }

    @Override
    public Integer read (JsonElement json) {

        return json.getAsInt();
    }

    @Override
    public JsonElement write (Integer toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Integer read (PacketBuffer buffer) {

        return buffer.readInt();
    }

    @Override
    public void write (PacketBuffer buffer, Integer toWrite) {

        buffer.writeInt(toWrite);
    }

    @Override
    public INBT writeNBT (Integer toWrite) {

        return IntNBT.valueOf(toWrite);
    }

    @Override
    public Integer read (INBT nbt) {

        if (nbt instanceof NumberNBT) {

            return ((NumberNBT) nbt).getAsInt();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}