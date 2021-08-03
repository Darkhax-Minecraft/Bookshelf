package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.FriendlyByteBuf;

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
    public Integer read (FriendlyByteBuf buffer) {

        return buffer.readInt();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Integer toWrite) {

        buffer.writeInt(toWrite);
    }

    @Override
    public Tag writeNBT (Integer toWrite) {

        return IntTag.valueOf(toWrite);
    }

    @Override
    public Integer read (Tag nbt) {

        if (nbt instanceof NumericTag) {

            return ((NumericTag) nbt).getAsInt();
        }

        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}