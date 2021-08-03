package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A loot condition that checks the ID of the dimension.
 */
public class CheckDimensionId implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation dimensionId;

    public CheckDimensionId (ResourceLocation dimensionId) {

        this.dimensionId = dimensionId;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Level world = ctx.getLevel();
        final ResourceKey<Level> dimension = world.dimension();
        return dimension != null && dimension.location().equals(this.dimensionId);
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckDimensionId> {

        @Override
        public CheckDimensionId deserialize (JsonObject json, JsonDeserializationContext context) {

            final ResourceLocation id = ResourceLocation.tryParse(GsonHelper.getAsString(json, "dimension"));

            return new CheckDimensionId(id);
        }

        @Override
        public void serialize (JsonObject json, CheckDimensionId value, JsonSerializationContext context) {

            json.addProperty("dimension", value.dimensionId.toString());
        }
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckDimension;
    }
}