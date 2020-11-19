package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

public class CheckItem implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    /**
     * The predicate applied to the first item.
     */
    private final ItemPredicate predicate;
    
    public CheckItem(ItemPredicate predicate) {
        
        this.predicate = predicate;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final ItemStack stack = LootUtils.getItemContext(ctx);
        return stack != null && this.predicate.test(stack);
    }
    
    @Override
    public LootConditionType func_230419_b_ () {
        
        return Bookshelf.instance.conditionCheckItem;
    }
    
    static class Serializer implements ILootSerializer<CheckItem> {
        
        @Override
        public void serialize (JsonObject json, CheckItem value, JsonSerializationContext context) {
            
            json.add("predicate", value.predicate.serialize());
        }
        
        @Override
        public CheckItem deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final ItemPredicate predicate = ItemPredicate.deserialize(json.get("predicate"));
            return new CheckItem(predicate);
        }
    }
}