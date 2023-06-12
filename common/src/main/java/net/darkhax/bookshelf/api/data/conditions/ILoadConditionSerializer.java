package net.darkhax.bookshelf.api.data.conditions;

import com.google.gson.JsonObject;

public interface ILoadConditionSerializer<T extends ILoadCondition> {

    T fromJson(JsonObject json);
}