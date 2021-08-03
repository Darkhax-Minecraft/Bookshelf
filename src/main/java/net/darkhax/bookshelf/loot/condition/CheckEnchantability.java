package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * This loot condition checks the enchantability of the item used.
 */
public class CheckEnchantability implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final Ints enchantability;

    public CheckEnchantability (Ints enchantIntBound) {

        this.enchantability = enchantIntBound;
    }

    @Override
    public boolean test (LootContext ctx) {

        final ItemStack stack = LootUtils.getItemContext(ctx);

        if (stack != null) {

            return this.enchantability.matches(stack.getItemEnchantability());
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckEnchantability;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckEnchantability> {

        @Override
        public void serialize (JsonObject json, CheckEnchantability value, JsonSerializationContext context) {

            json.add("value", value.enchantability.serializeToJson());
        }

        @Override
        public CheckEnchantability deserialize (JsonObject json, JsonDeserializationContext context) {

            return new CheckEnchantability(Ints.fromJson(json.get("value")));
        }
    }
}