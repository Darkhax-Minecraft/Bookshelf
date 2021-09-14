package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

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
        
        final Vector3d pos = ctx.getParamOrNull(LootParameters.ORIGIN);
        
        if (pos != null) {
            
            return this.power.matches(ctx.getLevel().getBestNeighborSignal(new BlockPos(pos)));
        }
        
        return false;
    }
    
    @Override
    public LootConditionType getType () {
        
        return Bookshelf.instance.conditionCheckPower;
    }
    
    static class Serializer implements ILootSerializer<CheckPower> {
        
        @Override
        public void serialize (JsonObject json, CheckPower value, JsonSerializationContext context) {
            
            json.add("value", value.power.serializeToJson());
        }
        
        @Override
        public CheckPower deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckPower(IntBound.fromJson(json));
        }
    }
}