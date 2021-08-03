package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A loot condition that checks for a village.
 */
public class CheckVillage extends LootConditionPositional {

    public static final Serializer SERIALIZER = new Serializer();

    private CheckVillage () {

        super(CheckVillage::test);
    }

    private static boolean test (LootContext ctx, BlockPos pos) {

        return ctx.getLevel().isVillage(pos);
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckVillage;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckVillage> {

        private final CheckVillage instance = new CheckVillage();

        @Override
        public void serialize (JsonObject json, CheckVillage value, JsonSerializationContext context) {

        }

        @Override
        public CheckVillage deserialize (JsonObject json, JsonDeserializationContext context) {

            return this.instance;
        }
    }
}