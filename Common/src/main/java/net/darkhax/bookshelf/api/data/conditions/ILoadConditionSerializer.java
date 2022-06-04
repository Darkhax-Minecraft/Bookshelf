package net.darkhax.bookshelf.api.data.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.serialization.ISerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

import java.util.HashMap;
import java.util.Map;

public interface ILoadConditionSerializer<T extends ILoadCondition> {

    T fromJson(JsonObject json);
}