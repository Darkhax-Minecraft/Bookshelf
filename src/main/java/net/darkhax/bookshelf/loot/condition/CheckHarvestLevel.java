package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * This condition checks the harvest level of the tool used.
 */
public class CheckHarvestLevel implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final Ints level;

    public CheckHarvestLevel (Ints level) {

        this.level = level;
    }

    @Override
    public boolean test (LootContext ctx) {

        final ItemStack stack = LootUtils.getItemContext(ctx);

        if (stack != null && stack.getItem() instanceof TieredItem) {

            return this.level.matches(((TieredItem) stack.getItem()).getTier().getLevel());
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckHarvestLevel;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.	Serializer<CheckHarvestLevel> {

        @Override
        public void serialize (JsonObject json, CheckHarvestLevel value, JsonSerializationContext context) {

            json.add("value", value.level.serializeToJson());
        }

        @Override
        public CheckHarvestLevel deserialize (JsonObject json, JsonDeserializationContext context) {

            return new CheckHarvestLevel(Ints.fromJson(json.get("value")));
        }
    }
}