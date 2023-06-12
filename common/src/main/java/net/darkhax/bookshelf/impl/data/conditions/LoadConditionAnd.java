package net.darkhax.bookshelf.impl.data.conditions;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;

/**
 * This condition will only be met if all internally held conditions are met.
 */
public class LoadConditionAnd implements ILoadConditionSerializer<ILoadCondition> {

    @Override
    public ILoadCondition fromJson(JsonObject json) {

        final ILoadCondition[] conditions = LoadConditions.getConditions(json.get("conditions"));

        return () -> {

            for (ILoadCondition condition : conditions) {

                if (!condition.test()) {

                    return false;
                }
            }

            return true;
        };
    }
}