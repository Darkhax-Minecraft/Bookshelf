package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

/**
 * A loot condition that checks the biome dictionary tags of the current biome.
 */
public class CheckBiomeTag implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    /**
     * The biome type to search for.
     */
    private final Type biomeType;

    public CheckBiomeTag (Type type) {

        this.biomeType = type;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);

        if (pos != null) {

            final Biome biome = ctx.getLevel().getBiome(new BlockPos(pos));

            if (biome != null) {

                final ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
                return BiomeDictionary.hasType(biomeKey, this.biomeType);
            }
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckBiomeTag;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckBiomeTag> {

        @Override
        public void serialize (JsonObject json, CheckBiomeTag value, JsonSerializationContext context) {

            json.addProperty("tag", value.biomeType.getName());
        }

        @Override
        public CheckBiomeTag deserialize (JsonObject json, JsonDeserializationContext context) {

            final Type tag = Type.getType(GsonHelper.getAsString(json, "tag"));
            return new CheckBiomeTag(tag);
        }
    }
}