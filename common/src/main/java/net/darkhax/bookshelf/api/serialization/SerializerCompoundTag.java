package net.darkhax.bookshelf.api.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class SerializerCompoundTag implements ISerializer<CompoundTag> {

    public static final ISerializer<CompoundTag> SERIALIZER = new SerializerCompoundTag();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private SerializerCompoundTag() {

    }

    @Override
    public CompoundTag fromJSON(JsonElement json) {

        try {

            if (json.isJsonObject()) {

                // Some numbers like shorts and array tags like IntArrayNBT can not be
                // deserialized from objects due to NBT spec being dumb.
                return TagParser.parseTag(GSON.toJson(json));
            }
            else {

                return TagParser.parseTag(GsonHelper.convertToString(json, "nbt"));
            }
        }
        catch (final CommandSyntaxException e) {

            throw new JsonParseException("Failed to read NBT from " + GsonHelper.getType(json), e);
        }
    }

    @Override
    public JsonElement toJSON(CompoundTag toWrite) {

        // While object is supported, some objects can never be deserialized properly.
        return new JsonPrimitive(toWrite.toString());
    }

    @Override
    public CompoundTag fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readNbt();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, CompoundTag toWrite) {

        buffer.writeNbt(toWrite);
    }

    @Override
    public Tag toNBT(CompoundTag toWrite) {

        return toWrite;
    }

    @Override
    public CompoundTag fromNBT(Tag nbt) {

        if (nbt instanceof CompoundTag compound) {

            return compound;
        }

        throw new NBTParseException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}