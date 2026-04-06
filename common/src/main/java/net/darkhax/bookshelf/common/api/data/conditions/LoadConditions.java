package net.darkhax.bookshelf.common.api.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecHelper;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LoadConditions {

    private static final Map<Identifier, ConditionType> CONDITION_TYPES = new HashMap<>();
    private static final Codec<ConditionType> CONDITION_TYPE_CODEC = Identifier.CODEC.xmap(CONDITION_TYPES::get, ConditionType::id);
    public static final String LOAD_CONDITION_TAG = BookshelfMod.id("load_conditions").toString();
    public static final Codec<ILoadCondition> CONDITION_CODEC = CONDITION_TYPE_CODEC.dispatch(ILoadCondition::getType, ConditionType::codec);
    public static final MapCodecHelper<ILoadCondition> CODEC_HELPER = new MapCodecHelper<>(CONDITION_CODEC);

    @Nullable
    public static ConditionType getType(Identifier id) {
        return CONDITION_TYPES.get(id);
    }

    public static <T extends ILoadCondition> ConditionType register(Identifier id, MapCodec<T> codec) {
        if (CONDITION_TYPES.containsKey(id)) {
            BookshelfMod.LOG.warn("JSON Load Serializer ID {} has already been assigned to {}. Replacing with {}.", id, CONDITION_TYPES.get(id).codec(), codec);
        }
        final ConditionType type = new ConditionType(id, codec);
        CONDITION_TYPES.put(id, type);
        return type;
    }

    /**
     * Reads one or more conditions from a JSON element. If the element is an object an array of 1 will be returned.
     *
     * @param conditionData The condition data read from the raw JSON entry.
     * @return An array of load conditions read from the data.
     */
    public static ILoadCondition[] getConditions(JsonElement conditionData) {
        return MapCodecs.LOAD_CONDITION.arrayCodec().decode(JsonOps.INSTANCE, conditionData).getOrThrow().getFirst();
    }

    /**
     * Tests if a raw JSON element can be loaded. This will search for the condition property and attempt to deserialize
     * and test those conditions.
     *
     * @param rawJson The raw JSON data as read from the data/resource pack.
     * @return Whether the JSON entry should be loaded or not.
     */
    public static boolean canLoad(JsonObject rawJson) {
        if (rawJson.has(LOAD_CONDITION_TAG)) {
            final JsonElement conditionData = rawJson.get(LOAD_CONDITION_TAG);
            for (ILoadCondition condition : getConditions(conditionData)) {
                if (!condition.allowLoading()) {
                    return false;
                }
            }
        }
        return true;
    }
}