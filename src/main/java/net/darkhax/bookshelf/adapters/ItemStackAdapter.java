package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackAdapter implements JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {

            final NBTTagCompound tag = JsonToNBT.getTagFromJson(json.toString());

            if (tag != null) {

                if (!tag.hasKey("Count")) {

                    tag.setInteger("Count", 1);
                }
                return new ItemStack(tag);
            }
        }
        catch (final NBTException e) {

            throw new JsonParseException(e);
        }

        return null;
    }
}