package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

/**
 * This loot condition checks if the position the loot is being generated at has redstone
 * power.
 */
public class CheckPower implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final Ints power;

    public CheckPower (Ints power) {

        this.power = power;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);

        if (pos != null) {

            return this.power.matches(ctx.getLevel().getBestNeighborSignal(new BlockPos(pos)));
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckPower;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckPower> {

        @Override
        public void serialize (JsonObject json, CheckPower value, JsonSerializationContext context) {

            json.add("value", value.power.serializeToJson());
        }

        @Override
        public CheckPower deserialize (JsonObject json, JsonDeserializationContext context) {

            return new CheckPower(Ints.fromJson(json));
        }
    }
}