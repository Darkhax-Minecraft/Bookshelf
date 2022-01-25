package net.darkhax.bookshelf.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class JSONHelper {

    private static final Gson GSON = new GsonBuilder().create();

    public static JsonElement getAsElement(String jsonString) {

        return GSON.fromJson(jsonString, JsonElement.class);
    }

    public static String getAsString(JsonElement element) {

        if (element instanceof JsonPrimitive primitive && primitive.isString()) {

            return primitive.getAsString();
        }

        throw new JsonParseException("Expected String value but got " + element.toString());
    }
}