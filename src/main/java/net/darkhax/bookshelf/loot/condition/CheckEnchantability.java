package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * This loot condition checks the enchantability of the item used.
 */
public class CheckEnchantability implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final IntBound enchantability;
    
    public CheckEnchantability(IntBound enchantIntBound) {
        
        this.enchantability = enchantIntBound;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final ItemStack stack = LootUtils.getItemContext(ctx);
        
        if (stack != null) {
            
            return this.enchantability.test(stack.getItemEnchantability());
        }
        
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }

    static class Serializer implements LootCondtionSerializer<CheckEnchantability> {
        public LootConditionType lootConditionType = null;

        @Override
        public void setType(LootConditionType lcType) {
            lootConditionType = lcType;
        }

        @Override
        public String getName() {
            return Bookshelf.MOD_ID + ":check_enchantability";
        }

        @Override
        public void serialize (JsonObject json, CheckEnchantability value, JsonSerializationContext context) {
            
            json.add("value", value.enchantability.serialize());
        }
        
        @Override
        public CheckEnchantability deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckEnchantability(IntBound.fromJson(json.get("value")));
        }
    }
}