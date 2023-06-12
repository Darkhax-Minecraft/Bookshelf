package net.darkhax.bookshelf.impl.data.conditions;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;

/**
 * This condition will only be met when the specified platform is being used.
 */
public class LoadConditionPlatform implements ILoadConditionSerializer<ILoadCondition> {

    @Override
    public ILoadCondition fromJson(JsonObject json) {

        final String platform = Serializers.STRING.fromJSON(json, "platform");
        return () -> platform.equalsIgnoreCase(Services.PLATFORM.getName());
    }
}
