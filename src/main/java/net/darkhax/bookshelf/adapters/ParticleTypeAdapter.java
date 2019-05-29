package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.EnumParticleTypes;

public class ParticleTypeAdapter implements JsonDeserializer<EnumParticleTypes>, JsonSerializer<EnumParticleTypes> {

    @Override
    public EnumParticleTypes deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        final String id = json.getAsString();
        return id == null ? null : EnumParticleTypes.getByName(id);
    }

    @Override
    public JsonElement serialize (EnumParticleTypes src, Type typeOfSrc, JsonSerializationContext context) {
        
        return new JsonPrimitive(src.getParticleName());
    }
}