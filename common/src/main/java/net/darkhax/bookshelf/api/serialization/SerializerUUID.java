package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public final class SerializerUUID implements ISerializer<UUID> {

    public static final ISerializer<UUID> SERIALIZER = new SerializerUUID();

    private SerializerUUID() {

    }

    @Override
    public UUID fromJSON(JsonElement json) {

        return UUID.fromString(json.getAsString());
    }

    @Override
    public JsonElement toJSON(UUID toWrite) {

        return new JsonPrimitive(toWrite.toString());
    }

    @Override
    public UUID fromByteBuf(FriendlyByteBuf buffer) {

        return UUID.fromString(buffer.readUtf());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, UUID toWrite) {

        buffer.writeUtf(toWrite.toString());
    }

    @Override
    public Tag toNBT(UUID toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putUUID("id", toWrite);
        return tag;
    }

    @Override
    public UUID fromNBT(Tag nbt) {

        if (nbt instanceof CompoundTag compound) {

            return compound.getUUID("id");
        }

        throw new NBTParseException("Expected NBT to be a compund tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}