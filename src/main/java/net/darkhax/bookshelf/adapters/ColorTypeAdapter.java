package net.darkhax.bookshelf.adapters;

import java.awt.Color;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ColorTypeAdapter implements JsonDeserializer<Color>, JsonSerializer<Color> {

    @Override
    public Color deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        String value = json.getAsString();

        if (!value.startsWith("#")) {
            value = "#" + value;
        }
        return Color.decode(value);
    }

    @Override
    public JsonElement serialize (Color src, Type typeOfSrc, JsonSerializationContext context) {
        
        return new JsonPrimitive(String.format("#%02x%02x%02x", src.getRed(), src.getGreen(), src.getBlue()));
    }
}