package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * This condition checks the harvest level of the tool used.
 */
public class CheckHarvestLevel implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final IntBound level;
    
    public CheckHarvestLevel(IntBound level) {
        
        this.level = level;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final ItemStack stack = LootUtils.getItemContext(ctx);
        
        if (stack != null && stack.getItem() instanceof TieredItem) {
            
            return this.level.test(((TieredItem) stack.getItem()).getTier().getHarvestLevel());
        }
        
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }

    static class Serializer implements LootCondtionSerializer<CheckHarvestLevel> {
        public LootConditionType lootConditionType = null;

        @Override
        public void setType(LootConditionType lcType) {
            lootConditionType = lcType;
        }

        @Override
        public String getName() {
            return Bookshelf.MOD_ID + ":check_harvest_level";
        }

        @Override
        public void serialize (JsonObject json, CheckHarvestLevel value, JsonSerializationContext context) {
            
            json.add("value", value.level.serialize());
        }
        
        @Override
        public CheckHarvestLevel deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckHarvestLevel(IntBound.fromJson(json.get("value")));
        }
    }
}