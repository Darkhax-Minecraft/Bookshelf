package net.darkhax.bookshelf.serialization;

import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerUUID implements ISerializer<UUID> {

    public static final ISerializer<UUID> SERIALIZER = new SerializerUUID();

    private SerializerUUID () {

    }

    @Override
    public UUID read (JsonElement json) {

        return UUID.fromString(json.getAsString());
    }

    @Override
    public JsonElement write (UUID toWrite) {

        return new JsonPrimitive(toWrite.toString());
    }

    @Override
    public UUID read (FriendlyByteBuf buffer) {

        return UUID.fromString(buffer.readUtf());
    }

    @Override
    public void write (FriendlyByteBuf buffer, UUID toWrite) {

        buffer.writeUtf(toWrite.toString());
    }

    @Override
    public Tag writeNBT (UUID toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putUUID("id", toWrite);
        return tag;
    }

    @Override
    public UUID read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            return ((CompoundTag) nbt).getUUID("id");
        }

        throw new IllegalArgumentException("Expected NBT to be a compund tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}