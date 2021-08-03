package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class CheckItem implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    /**
     * The predicate applied to the first item.
     */
    private final ItemPredicate predicate;

    public CheckItem (ItemPredicate predicate) {

        this.predicate = predicate;
    }

    @Override
    public boolean test (LootContext ctx) {

        final ItemStack stack = LootUtils.getItemContext(ctx);
        return stack != null && this.predicate.matches(stack);
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckItem;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckItem> {

        @Override
        public void serialize (JsonObject json, CheckItem value, JsonSerializationContext context) {

            json.add("predicate", value.predicate.serializeToJson());
        }

        @Override
        public CheckItem deserialize (JsonObject json, JsonDeserializationContext context) {

            final ItemPredicate predicate = ItemPredicate.fromJson(json.get("predicate"));
            return new CheckItem(predicate);
        }
    }
}