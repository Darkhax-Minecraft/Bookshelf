package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public final class SerializerResourceLocation implements ISerializer<ResourceLocation> {

    public static final ISerializer<ResourceLocation> SERIALIZER = new SerializerResourceLocation();

    private SerializerResourceLocation() {

    }

    @Override
    public ResourceLocation fromJSON(JsonElement json) {

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
    public JsonElement toJSON(ResourceLocation toWrite) {

        return new JsonPrimitive(toWrite.toString());
    }

    @Override
    public ResourceLocation fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readResourceLocation();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, ResourceLocation toWrite) {

        buffer.writeResourceLocation(toWrite);
    }

    @Override
    public Tag toNBT(ResourceLocation toWrite) {

        return StringTag.valueOf(toWrite.toString());
    }

    @Override
    public ResourceLocation fromNBT(Tag nbt) {

        return new ResourceLocation(Serializers.STRING.fromNBT(nbt));
    }
}