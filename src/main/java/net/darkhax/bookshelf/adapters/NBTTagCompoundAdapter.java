package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagCompoundAdapter implements JsonDeserializer<NBTTagCompound> {

    @Override
    public NBTTagCompound deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {

            return JsonToNBT.getTagFromJson(json.toString());
        }

        catch (final NBTException e) {

            throw new JsonParseException(e);
        }
    }
}