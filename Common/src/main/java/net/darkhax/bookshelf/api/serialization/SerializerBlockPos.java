package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class SerializerBlockPos implements ISerializer<BlockPos> {

    public static final ISerializer<BlockPos> SERIALIZER = new SerializerBlockPos();

    @Override
    public BlockPos fromJSON(JsonElement json) {

        // If it's an object explicitly read each axis.
        if (json.isJsonObject()) {

            final JsonObject object = json.getAsJsonObject();
            final int x = Serializers.INT.fromJSON(object, "x");
            final int y = Serializers.INT.fromJSON(object, "y");
            final int z = Serializers.INT.fromJSON(object, "z");
            return new BlockPos(x, y, z);
        }

        // If it's an array of 3 elements read in X,Y,Z order.
        else if (json.isJsonArray()) {

            final JsonArray array = json.getAsJsonArray();

            if (array.size() == 3) {

                return new BlockPos(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
            }

            throw new JsonParseException("Invalid number of args in array. Expected 3 but got " + array.size());
        }

        // If it's a primitive, try to read it as a long.
        else if (json.isJsonPrimitive()) {

            final JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {

                return BlockPos.of(json.getAsLong());
            }

            throw new JsonParseException("Expected JSON primitive to be a number.");
        }

        throw new JsonParseException("BlockPos data is not in a readable format.");
    }

    @Override
    public JsonElement toJSON(BlockPos toWrite) {

        final JsonObject object = new JsonObject();
        object.addProperty("x", toWrite.getX());
        object.addProperty("y", toWrite.getY());
        object.addProperty("z", toWrite.getZ());
        return object;
    }

    @Override
    public BlockPos fromByteBuf(FriendlyByteBuf buffer) {

        return BlockPos.of(buffer.readLong());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, BlockPos toWrite) {

        buffer.writeLong(toWrite.asLong());
    }

    @Override
    public Tag toNBT(BlockPos toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putInt("x", toWrite.getX());
        tag.putInt("y", toWrite.getY());
        tag.putInt("z", toWrite.getZ());
        return tag;
    }

    @Override
    public BlockPos fromNBT(Tag nbt) {

        // If it's a compound explicitly read each axis.
        if (nbt instanceof CompoundTag compound) {

            final int x = compound.getInt("x");
            final int y = compound.getInt("y");
            final int z = compound.getInt("z");
            return new BlockPos(x, y, z);
        }

        // If it's a list of 3 elements read in X,Y,Z order.
        else if (nbt instanceof ListTag list) {

            if (list.size() == 3) {

                return new BlockPos(Serializers.INT.fromNBT(list.get(0)), Serializers.INT.fromNBT(list.get(1)), Serializers.INT.fromNBT(list.get(2)));
            }

            throw new NBTParseException("Invalid number of args in array. Expected 3 but got " + list.size());
        }

        // If it's a number, try to read it as a long.
        else if (nbt instanceof NumericTag numberTag) {

            return BlockPos.of(numberTag.getAsLong());
        }

        throw new NBTParseException("BlockPos data is not in a readable format.");
    }

}
