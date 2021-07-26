package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;

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
    public LootConditionType getType () {

        return Bookshelf.instance.conditionCheckSlimeChunk;
    }

    private static class Serializer implements ILootSerializer<CheckSlimeChunk> {

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