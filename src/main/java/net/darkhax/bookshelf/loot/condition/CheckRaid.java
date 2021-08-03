package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A loot condition that checks for an active raid.
 */
public class CheckRaid extends LootConditionPositional {

    public static final Serializer SERIALIZER = new Serializer();

    private CheckRaid () {

        super(CheckRaid::test);
    }

    private static boolean test (LootContext ctx, BlockPos pos) {

        return ctx.getLevel().isRaided(pos);
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckRaid;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckRaid> {

        private final CheckRaid instance = new CheckRaid();

        @Override
        public void serialize (JsonObject json, CheckRaid value, JsonSerializationContext context) {

        }

        @Override
        public CheckRaid deserialize (JsonObject json, JsonDeserializationContext context) {

            return this.instance;
        }
    }
}