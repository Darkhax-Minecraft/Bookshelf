package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ResourceLocationException;

public final class SerializerResourceLocation implements ISerializer<ResourceLocation> {

    public static final ISerializer<ResourceLocation> SERIALIZER = new SerializerResourceLocation();

    private SerializerResourceLocation () {

    }

    @Override
    public ResourceLocation read (JsonElement json) {

        if (json.isJsonPrimitive()) {

            final String string = json.getAsString();

            try {

                return new ResourceLocation(string);
            }

            catch (final ResourceLocationException e) {

                throw new JsonParseException("Expected a valid resource location.", e);
            }
        }

        else {

            throw new JsonSyntaxException("Expected a string, was " + GsonHelper.getType(json));
        }
    }

    @Override
    public JsonElement write (ResourceLocation toWrite) {

        return new JsonPrimitive(toWrite.toString());
    }

    @Override
    public ResourceLocation read (FriendlyByteBuf buffer) {

        return buffer.readResourceLocation();
    }

    @Override
    public void write (FriendlyByteBuf buffer, ResourceLocation toWrite) {

        buffer.writeResourceLocation(toWrite);
    }

    @Override
    public Tag writeNBT (ResourceLocation toWrite) {

        return StringTag.valueOf(toWrite.toString());
    }

    @Override
    public ResourceLocation read (Tag nbt) {

        if (nbt instanceof StringTag) {

            return new ResourceLocation(((StringTag) nbt).getAsString());
        }

        throw new IllegalArgumentException("Expected NBT to be a double tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}