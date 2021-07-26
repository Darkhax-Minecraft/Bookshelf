package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerItemStack implements ISerializer<ItemStack> {

    public static final ISerializer<ItemStack> SERIALIZER = new SerializerItemStack();

    private SerializerItemStack () {

    }

    @Override
    public ItemStack read (JsonElement json) {

        if (json.isJsonObject()) {

            return ShapedRecipe.itemFromJson(json.getAsJsonObject());
        }

        else if (json.isJsonPrimitive()) {

            return new ItemStack(Serializers.ITEM.read(json));
        }

        throw new JsonParseException("Expected JSON object, got " + JSONUtils.getType(json));
    }

    @Override
    public JsonElement write (ItemStack toWrite) {

        final JsonObject json = new JsonObject();

        json.addProperty("item", toWrite.getItem().getRegistryName().toString());
        json.addProperty("count", toWrite.getCount());

        if (toWrite.hasTag()) {

            json.add("nbt", Serializers.NBT.write(toWrite.getTag()));
        }

        return json;
    }

    @Override
    public ItemStack read (PacketBuffer buffer) {

        return buffer.readItem();
    }

    @Override
    public void write (PacketBuffer buffer, ItemStack toWrite) {

        buffer.writeItem(toWrite);
    }

    @Override
    public INBT writeNBT (ItemStack toWrite) {

        return toWrite.save(new CompoundNBT());
    }

    @Override
    public ItemStack read (INBT nbt) {

        if (nbt instanceof CompoundNBT) {

            return ItemStack.of((CompoundNBT) nbt);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}