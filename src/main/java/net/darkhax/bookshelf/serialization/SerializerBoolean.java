package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerBoolean implements ISerializer<Boolean> {

    public static final ISerializer<Boolean> SERIALIZER = new SerializerBoolean();

    private SerializerBoolean () {

    }

    @Override
    public Boolean read (JsonElement json) {

        return json.getAsBoolean();
    }

    @Override
    public JsonElement write (Boolean toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Boolean read (FriendlyByteBuf buffer) {

        return buffer.readBoolean();
    }

    @Override
    public void write (FriendlyByteBuf buffer, Boolean toWrite) {

        buffer.writeBoolean(toWrite);
    }

    @Override
    public Tag writeNBT (Boolean toWrite) {

        return ByteTag.valueOf(toWrite);
    }

    @Override
    public Boolean read (Tag nbt) {

        if (nbt instanceof ByteTag) {

            return ((ByteTag) nbt).getAsByte() != 0;
        }

        throw new IllegalArgumentException("Expected NBT to be a byte/boolean tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}