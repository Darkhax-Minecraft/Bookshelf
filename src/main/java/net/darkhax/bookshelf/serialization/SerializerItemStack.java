package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class SerializerItemStack implements ISerializer<ItemStack> {

    public static final ISerializer<ItemStack> SERIALIZER = new SerializerItemStack();

    private SerializerItemStack () {

    }

    @Override
    public ItemStack read (JsonElement json) {

        if (json.isJsonObject()) {

            return ShapedRecipe.itemStackFromJson(json.getAsJsonObject());
        }

        else if (json.isJsonPrimitive()) {

            return new ItemStack(Serializers.ITEM.read(json));
        }

        throw new JsonParseException("Expected JSON object, got " + GsonHelper.getType(json));
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
    public ItemStack read (FriendlyByteBuf buffer) {

        return buffer.readItem();
    }

    @Override
    public void write (FriendlyByteBuf buffer, ItemStack toWrite) {

        buffer.writeItem(toWrite);
    }

    @Override
    public Tag writeNBT (ItemStack toWrite) {

        return toWrite.save(new CompoundTag());
    }

    @Override
    public ItemStack read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            return ItemStack.of((CompoundTag) nbt);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}