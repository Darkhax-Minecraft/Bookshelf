package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationTypeAdapter implements JsonDeserializer<ResourceLocation> {

    @Override
    public ResourceLocation deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return new ResourceLocation(json.getAsString());
    }
}