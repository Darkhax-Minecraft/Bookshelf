package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
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
            
            return this.enchantability.matches(stack.getItemEnchantability());
        }
        
        return false;
    }
    
    @Override
    public LootConditionType getType () {
        
        return Bookshelf.instance.conditionCheckEnchantability;
    }
    
    static class Serializer implements ILootSerializer<CheckEnchantability> {
        
        @Override
        public void serialize (JsonObject json, CheckEnchantability value, JsonSerializationContext context) {
            
            json.add("value", value.enchantability.serializeToJson());
        }
        
        @Override
        public CheckEnchantability deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckEnchantability(IntBound.fromJson(json.get("value")));
        }
    }
}