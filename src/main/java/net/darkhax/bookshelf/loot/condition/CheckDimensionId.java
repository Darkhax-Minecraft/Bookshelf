package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * A loot condition that checks the ID of the dimension.
 */
public class CheckDimensionId implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final ResourceLocation dimensionId;
    
    public CheckDimensionId(ResourceLocation dimensionId) {
        
        this.dimensionId = dimensionId;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final World world = ctx.getLevel();
        final RegistryKey<World> dimension = world.dimension();
        return dimension != null && dimension.location().equals(this.dimensionId);
    }
    
    static class Serializer implements ILootSerializer<CheckDimensionId> {
        
        @Override
        public CheckDimensionId deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final ResourceLocation id = ResourceLocation.tryParse(JSONUtils.getAsString(json, "dimension"));
            
            return new CheckDimensionId(id);
        }
        
        @Override
        public void serialize (JsonObject json, CheckDimensionId value, JsonSerializationContext context) {
            
            json.addProperty("dimension", value.dimensionId.toString());
        }
    }
    
    @Override
    public LootConditionType getType () {
        
        return Bookshelf.instance.conditionCheckDimension;
    }
}