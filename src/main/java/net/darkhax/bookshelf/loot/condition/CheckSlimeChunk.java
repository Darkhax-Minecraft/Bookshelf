package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A loot condition that checks for a slime chunk.
 */
public class CheckSlimeChunk extends LootConditionPositional {

    public static final Serializer SERIALIZER = new Serializer();

    private CheckSlimeChunk () {

        super(CheckSlimeChunk::test);
    }

    private static boolean test (LootContext ctx, BlockPos pos) {

        return WorldUtils.isSlimeChunk(ctx.getLevel(), pos);
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckSlimeChunk;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckSlimeChunk> {

        private final CheckSlimeChunk instance = new CheckSlimeChunk();

        @Override
        public void serialize (JsonObject json, CheckSlimeChunk value, JsonSerializationContext context) {

        }

        @Override
        public CheckSlimeChunk deserialize (JsonObject json, JsonDeserializationContext context) {

            return this.instance;
        }
    }
}