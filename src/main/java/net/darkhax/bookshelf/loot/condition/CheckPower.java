package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

/**
 * This loot condition checks if the position the loot is being generated at has redstone
 * power.
 */
public class CheckPower implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final int min;
    private final int max;
    
    public CheckPower(int minPower, int maxPower) {
        
        this.min = minPower;
        this.max = maxPower;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        
        if (pos != null) {
            
            final int power = ctx.getWorld().getRedstonePowerFromNeighbors(pos);
            return this.min <= power && this.max >= power;
        }
        
        return false;
    }
    
    static class Serializer extends ILootCondition.AbstractSerializer<CheckPower> {
        
        Serializer() {
            
            super(new ResourceLocation(Bookshelf.MOD_ID, "check_redstone_power"), CheckPower.class);
        }
        
        @Override
        public void serialize (JsonObject json, CheckPower value, JsonSerializationContext context) {
            
            json.addProperty("min", value.min);
            json.addProperty("max", value.max);
        }
        
        @Override
        public CheckPower deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final int min = JSONUtils.getInt(json, "min", 1);
            final int max = JSONUtils.getInt(json, "max", 15);
            return new CheckPower(min, max);
        }
    }
}