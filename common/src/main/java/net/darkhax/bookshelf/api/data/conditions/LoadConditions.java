package net.darkhax.bookshelf.api.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.darkhax.bookshelf.api.data.conditions.impl.And;
import net.darkhax.bookshelf.api.data.conditions.impl.Not;
import net.darkhax.bookshelf.api.data.conditions.impl.Or;
import net.darkhax.bookshelf.api.data.conditions.impl.OnPlatform;
import net.darkhax.bookshelf.api.data.conditions.impl.SpecificRegistryContains;
import net.darkhax.bookshelf.api.data.conditions.impl.ModLoaded;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class LoadConditions {


    private static final Map<ResourceLocation, ConditionType> CONDITION_TYPES = new HashMap<>();
    private static final Codec<ConditionType> CONDITION_TYPE_CODEC = ResourceLocation.CODEC.xmap(CONDITION_TYPES::get, ConditionType::id);
    public static final Codec<ILoadCondition> CONDITION_CODEC = CONDITION_TYPE_CODEC.dispatch(ILoadCondition::getType, ConditionType::codec);
    public static final CodecHelper<ILoadCondition> CODEC_HELPER = new CodecHelper<>(CONDITION_CODEC);

    public static final ConditionType AND = register(new ResourceLocation(Constants.MOD_ID, "and"), And.CODEC);
    public static final ConditionType NOT = register(new ResourceLocation(Constants.MOD_ID, "not"), Not.CODEC);
    public static final ConditionType OR = register(new ResourceLocation(Constants.MOD_ID, "or"), Or.CODEC);
    public static final ConditionType ON_PLATFORM = register(new ResourceLocation(Constants.MOD_ID, "on_platform"), OnPlatform.CODEC);
    public static final ConditionType MOD_LOADED = register(new ResourceLocation(Constants.MOD_ID, "mod_loaded"), ModLoaded.CODEC);
    public static final ConditionType BLOCK_EXISTS = SpecificRegistryContains.of("block_exists", BuiltInRegistries.BLOCK);
    public static final ConditionType ITEM_EXISTS = SpecificRegistryContains.of("item_exists", BuiltInRegistries.ITEM);
    public static final ConditionType ENCHANTMENT_EXISTS = SpecificRegistryContains.of("enchantment_exists", BuiltInRegistries.ENCHANTMENT);
    public static final ConditionType PAINTING_EXISTS = SpecificRegistryContains.of("painting_exists", BuiltInRegistries.PAINTING_VARIANT);
    public static final ConditionType MOB_EFFECT_EXISTS = SpecificRegistryContains.of("mob_effect_exists", BuiltInRegistries.MOB_EFFECT);
    public static final ConditionType POTION_EXISTS = SpecificRegistryContains.of("potion_exists", BuiltInRegistries.POTION);
    public static final ConditionType ATTRIBUTE_EXISTS = SpecificRegistryContains.of("attribute_exists", BuiltInRegistries.ATTRIBUTE);
    public static final ConditionType ENTITY_EXISTS = SpecificRegistryContains.of("entity_exists", BuiltInRegistries.ENTITY_TYPE);
    public static final ConditionType BLOCK_ENTITY_EXISTS = SpecificRegistryContains.of("block_entity_exists", BuiltInRegistries.BLOCK_ENTITY_TYPE);

    @Nullable
    public static ConditionType getType(ResourceLocation id) {

        return CONDITION_TYPES.get(id);
    }

    public static <T extends ILoadCondition> ConditionType register(ResourceLocation id, Codec<T> codec) {

        if (CONDITION_TYPES.containsKey(id)) {

            Constants.LOG.warn("JSON Load Serializer ID {} has already been assigned to {}. Replacing with {}.", id, CONDITION_TYPES.get(id).codec(), codec);
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

        return BookshelfCodecs.LOAD_CONDITION.getArray().decode(JsonOps.INSTANCE, conditionData).getOrThrow(false, error -> Constants.LOG.error("Failed to load conditions. Error: {}", error)).getFirst();
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

                if (!condition.allowLoading()) {

                    return false;
                }
            }
        }

        return true;
    }

    public record ConditionType(ResourceLocation id, Codec<? extends ILoadCondition> codec) {

    }
}
