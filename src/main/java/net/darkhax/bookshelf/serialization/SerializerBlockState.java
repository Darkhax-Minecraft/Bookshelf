package net.darkhax.bookshelf.serialization;

import java.util.Map.Entry;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.util.GsonHelper;

public class SerializerBlockState implements ISerializer<BlockState> {

    public static final ISerializer<BlockState> SERIALIZER = new SerializerBlockState();

    private SerializerBlockState () {

    }

    @Override
    public BlockState read (JsonElement json) {

        if (json.isJsonObject()) {

            final JsonObject obj = json.getAsJsonObject();
            final Block block = Serializers.BLOCK.read(obj, "block");

            BlockState state = block.defaultBlockState();

            if (obj.has("properties")) {

                final JsonElement properties = obj.get("properties");

                for (final Entry<String, JsonElement> property : properties.getAsJsonObject().entrySet()) {

                    state = this.readProperty(state, property.getKey(), property.getValue());
                }
            }

            return state;
        }

        else {

            throw new JsonParseException("Expected properties to be an object. Recieved " + GsonHelper.getType(json));
        }
    }

    @Override
    public JsonElement write (BlockState toWrite) {

        final JsonObject json = new JsonObject();
        json.add("block", Serializers.BLOCK.write(toWrite.getBlock()));

        final JsonObject properties = new JsonObject();

        for (final Property prop : toWrite.getProperties()) {

            if (prop instanceof IntegerProperty) {

                properties.addProperty(prop.getName(), (int) toWrite.getValue((IntegerProperty) prop));
            }

            else if (prop instanceof BooleanProperty) {

                properties.addProperty(prop.getName(), (boolean) toWrite.getValue((BooleanProperty) prop));
            }

            else {

                properties.addProperty(prop.getName(), prop.getName(toWrite.getValue(prop)));
            }
        }

        json.add("properties", properties);
        return json;
    }

    @Override
    public BlockState read (FriendlyByteBuf buffer) {

        return Block.stateById(buffer.readInt());
    }

    @Override
    public void write (FriendlyByteBuf buffer, BlockState toWrite) {

        buffer.writeInt(Block.getId(toWrite));
    }

    private BlockState readProperty (BlockState state, String propName, JsonElement propValue) {

        final Property blockProperty = state.getBlock().getStateDefinition().getProperty(propName);

        if (blockProperty != null) {

            if (propValue.isJsonPrimitive()) {

                final String valueString = propValue.getAsString();
                final Optional<Comparable> parsedValue = blockProperty.getValue(valueString);

                if (parsedValue.isPresent()) {

                    try {

                        return state.setValue(blockProperty, parsedValue.get());
                    }

                    catch (final Exception e) {

                        Bookshelf.LOG.error("Failed to update state for block {}. The mod that adds this block may have an issue.", state.getBlock().getRegistryName());
                        Bookshelf.LOG.catching(e);
                        throw e;
                    }
                }

                else {

                    throw new JsonSyntaxException("The property " + propName + " with value " + valueString + " coul not be parsed!");
                }
            }

            else {

                throw new JsonSyntaxException("Expected property value for " + propName + " to be primitive string. Got " + GsonHelper.getType(propValue));
            }
        }

        else {

            throw new JsonSyntaxException("The property " + propName + " is not valid for block " + state.getBlock().getRegistryName());
        }
    }

    @Override
    public Tag writeNBT (BlockState toWrite) {

        return NbtUtils.writeBlockState(toWrite);
    }

    @Override
    public BlockState read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            return NbtUtils.readBlockState((CompoundTag) nbt);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}