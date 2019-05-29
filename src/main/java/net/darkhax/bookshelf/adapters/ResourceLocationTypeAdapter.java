package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationTypeAdapter implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation> {

    @Override
    public ResourceLocation deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        return new ResourceLocation(json.getAsString());
    }

    @Override
    public JsonElement serialize (ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
        
        return new JsonPrimitive(src.toString());
    }
}