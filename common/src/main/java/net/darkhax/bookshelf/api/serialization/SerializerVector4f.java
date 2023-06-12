package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector4f;

public class SerializerVector4f implements ISerializer<Vector4f> {

    public static final ISerializer<Vector4f> SERIALIZER = new SerializerVector4f();

    @Override
    public Vector4f fromJSON(JsonElement json) {

        // If it's an object explicitly read each axis.
        if (json.isJsonObject()) {

            final JsonObject object = json.getAsJsonObject();
            final float x = Serializers.FLOAT.fromJSON(object, "x");
            final float y = Serializers.FLOAT.fromJSON(object, "y");
            final float z = Serializers.FLOAT.fromJSON(object, "z");
            final float w = Serializers.FLOAT.fromJSON(object, "w");
            return new Vector4f(x, y, z, w);
        }

        // If it's an array of 4 elements read in X,Y,Z,W order.
        else if (json.isJsonArray()) {

            final JsonArray array = json.getAsJsonArray();

            if (array.size() == 4) {

                return new Vector4f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat());
            }

            throw new JsonParseException("Invalid number of args in array. Expected 4 but got " + array.size());
        }

        throw new JsonParseException("Vector4f data is not in a readable format.");
    }

    @Override
    public JsonElement toJSON(Vector4f toWrite) {

        final JsonObject object = new JsonObject();
        object.addProperty("x", toWrite.x());
        object.addProperty("y", toWrite.y());
        object.addProperty("z", toWrite.z());
        object.addProperty("w", toWrite.w());
        return object;
    }

    @Override
    public Vector4f fromByteBuf(FriendlyByteBuf buffer) {

        return new Vector4f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Vector4f toWrite) {

        buffer.writeFloat(toWrite.x());
        buffer.writeFloat(toWrite.y());
        buffer.writeFloat(toWrite.z());
        buffer.writeFloat(toWrite.w());
    }

    @Override
    public Tag toNBT(Vector4f toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putFloat("x", toWrite.x());
        tag.putFloat("y", toWrite.y());
        tag.putFloat("z", toWrite.z());
        tag.putFloat("w", toWrite.w());
        return tag;
    }

    @Override
    public Vector4f fromNBT(Tag nbt) {

        // If it's a compound explicitly read each axis.
        if (nbt instanceof CompoundTag compound) {

            final float x = compound.getFloat("x");
            final float y = compound.getFloat("y");
            final float z = compound.getFloat("z");
            final float w = compound.getFloat("w");
            return new Vector4f(x, y, z, w);
        }

        // If it's a list of 4 elements read in X,Y,Z,W order.
        else if (nbt instanceof ListTag list) {

            if (list.size() == 4) {

                return new Vector4f(Serializers.FLOAT.fromNBT(list.get(0)), Serializers.FLOAT.fromNBT(list.get(1)), Serializers.FLOAT.fromNBT(list.get(2)), Serializers.FLOAT.fromNBT(list.get(3)));
            }

            throw new NBTParseException("Invalid number of args in array. Expected 4 but got " + list.size());
        }

        throw new NBTParseException("Vector4f data is not in a readable format.");
    }
}