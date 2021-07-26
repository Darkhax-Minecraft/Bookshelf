package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

/**
 * This loot condition checks the rarity of the item used.
 */
public class CheckRarity implements ILootCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final String rarity;

    public CheckRarity (String rarity) {

        this.rarity = rarity;
    }

    @Override
    public LootConditionType getType () {

        return Bookshelf.instance.conditionCheckRarity;
    }

    @Override
    public boolean test (LootContext ctx) {

        final ItemStack stack = LootUtils.getItemContext(ctx);

        if (stack != null) {

            final Rarity stackRarity = stack.getRarity();
            return stackRarity != null && stackRarity.name().equalsIgnoreCase(this.rarity);
        }

        return false;
    }

    static class Serializer implements ILootSerializer<CheckRarity> {

        @Override
        public void serialize (JsonObject json, CheckRarity value, JsonSerializationContext context) {

            json.addProperty("rarity", value.rarity);
        }

        @Override
        public CheckRarity deserialize (JsonObject json, JsonDeserializationContext context) {

            return new CheckRarity(JSONUtils.getAsString(json, "rarity"));
        }
    }
}