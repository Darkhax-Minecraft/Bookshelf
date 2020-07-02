package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * This loot condition checks if the position the loot is being generated at has redstone
 * power.
 */
public class CheckPower implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final IntBound power;
    
    public CheckPower(IntBound power) {
        
        this.power = power;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        
        if (pos != null) {
            
            return this.power.test(ctx.getWorld().getRedstonePowerFromNeighbors(pos));
        }
        
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }

    static class Serializer implements LootCondtionSerializer<CheckPower> {
        public LootConditionType lootConditionType = null;

        @Override
        public void setType(LootConditionType lcType) {
            lootConditionType = lcType;
        }

        @Override
        public String getName() {
            return Bookshelf.MOD_ID + ":check_redstone_power";
        }

        @Override
        public void serialize (JsonObject json, CheckPower value, JsonSerializationContext context) {
            
            json.add("value", value.power.serialize());
        }
        
        @Override
        public CheckPower deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckPower(IntBound.fromJson(json));
        }
    }
}