package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.util.EnumParticleTypes;

public class ParticleTypeAdapter implements JsonDeserializer<EnumParticleTypes> {

    @Override
    public EnumParticleTypes deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final String id = json.getAsString();
        return id == null ? null : EnumParticleTypes.getByName(id);
    }
}