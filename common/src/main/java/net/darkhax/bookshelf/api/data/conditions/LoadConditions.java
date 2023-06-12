package net.darkhax.bookshelf.api.data.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LoadConditions {

    /**
     * A registry containing all known load condition serializers.
     */
    private static final Map<ResourceLocation, ILoadConditionSerializer<?>> REGISTRY = new HashMap<>();

    /**
     * Registers a new load condition serializer.
     *
     * @param id         The type ID of the serializer.
     * @param serializer The serializer instance.
     */
    public static void register(ResourceLocation id, ILoadConditionSerializer<?> serializer) {

        if (REGISTRY.containsKey(id)) {

            Constants.LOG.warn("JSON Load Serializer ID {} has already been assigned to {}. Replacing with {}.", id, REGISTRY.get(id), serializer);
        }

        REGISTRY.put(id, serializer);
    }

    /**
     * Reads a load condition from a JSON object.
     *
     * @param conditionData The condition data read from the JSON entry.
     * @return The load condition that was deserialized.
     */
    public static ILoadCondition getCondition(JsonObject conditionData) {

        final ResourceLocation serializerId = Serializers.RESOURCE_LOCATION.fromJSON(conditionData, "type");
        final ILoadConditionSerializer<?> serializer = REGISTRY.get(serializerId);

        if (serializer != null) {

            return serializer.fromJson(conditionData);
        }

        else {

            throw new JsonParseException("Serializer ID " + serializerId.toString() + " is unknown!");
        }
    }

    /**
     * Reads one or more conditions from a JSON element. If the element is an object an array of 1 will be returned.
     *
     * @param conditionData The condition data read from the raw JSON entry.
     * @return An array of load conditions read from the data.
     */
    public static ILoadCondition[] getConditions(JsonElement conditionData) {

        if (conditionData instanceof JsonObject obj) {

            return new ILoadCondition[]{getCondition(obj)};
        }

        else if (conditionData instanceof JsonArray array) {

            ILoadCondition[] conditions = new ILoadCondition[array.size()];

            for (int i = 0; i < conditions.length; i++) {

                if (array.get(i) instanceof JsonObject obj) {

                    conditions[i] = getCondition(obj);
                }

                else {

                    throw new JsonParseException("Array of load conditions must contain exclusively objects! " + array.get(i).toString());
                }
            }

            return conditions;
        }

        else {

            throw new JsonParseException("Condition must be object or array of object! " + conditionData.toString());
        }
    }

    /**
     * Tests if a raw JSON element can be loaded. This will search for the condition property and attempt to deserialize
     * and test those conditions.
     *
     * @param rawJson The raw JSON data as read from the data/resource pack.
     * @return Whether the JSON entry should be loaded or not.
     */
    public static boolean canLoad(JsonObject rawJson) {

        if (rawJson.has("bookshelf:load_conditions")) {

            final JsonElement conditionData = rawJson.get("bookshelf:load_conditions");

            for (ILoadCondition condition : getConditions(conditionData)) {

                if (!condition.test()) {

                    return false;
                }
            }
        }

        return true;
    }
}
