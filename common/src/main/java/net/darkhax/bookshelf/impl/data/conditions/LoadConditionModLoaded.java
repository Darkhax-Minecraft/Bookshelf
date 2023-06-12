package net.darkhax.bookshelf.impl.data.conditions;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;

import java.util.List;

/**
 * This condition will only be met if all specified mod IDs are loaded and available.
 */
public class LoadConditionModLoaded implements ILoadConditionSerializer<ILoadCondition> {

    @Override
    public ILoadCondition fromJson(JsonObject json) {

        final List<String> mods = Serializers.STRING.fromJSONList(json, "mods");

        return () -> {

            for (String modId : mods) {

                if (!Services.PLATFORM.isModLoaded(modId)) {

                    return false;
                }
            }

            return true;
        };
    }
}