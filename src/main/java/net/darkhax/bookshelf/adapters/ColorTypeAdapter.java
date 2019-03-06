package net.darkhax.bookshelf.adapters;

import java.awt.Color;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class ColorTypeAdapter implements JsonDeserializer<Color> {

    @Override
    public Color deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        String value = json.getAsString();

        if (!value.startsWith("#")) {
            value = "#" + value;
        }
        return Color.decode(value);
    }
}