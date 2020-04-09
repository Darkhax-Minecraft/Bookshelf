package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.LootUtils;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

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
    
    static class Serializer extends ILootCondition.AbstractSerializer<CheckHarvestLevel> {
        
        Serializer() {
            
            super(new ResourceLocation(Bookshelf.MOD_ID, "check_harvest_level"), CheckHarvestLevel.class);
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