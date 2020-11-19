package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.feature.structure.Structure;

/**
 * A loot condition for checking if it is inside a structure.
 */
public class CheckStructure implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    /**
     * The name of the structure.
     */
    private final String structureName;
    
    /**
     * The structure being checked for. This is lazy loaded by {@link #loadStructure()}.
     */
    private Structure<?> structure;
    
    public CheckStructure(String structureName) {
        
        this.structureName = structureName;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final Vector3d pos = ctx.get(LootParameters.field_237457_g_);
        
        if (pos != null && this.loadStructure()) {
            
            return WorldUtils.isInStructure(ctx.getWorld(), new BlockPos(pos), this.structure);
        }
        
        return false;
    }
    
    @Override
    public LootConditionType func_230419_b_ () {
        
        return Bookshelf.instance.conditionCheckStructure;
    }
    
    private boolean loadStructure () {
        
        if (this.structure == null) {
            
            this.structure = Structure.NAME_STRUCTURE_BIMAP.get(this.structureName);
            
            if (this.structure == null) {
                
                Bookshelf.LOG.error("Loot table condition is looking for structure {} which doesn't exist.", this.structureName);
                return false;
            }
        }
        
        return true;
    }
    
    static class Serializer implements ILootSerializer<CheckStructure> {
        
        @Override
        public void serialize (JsonObject json, CheckStructure value, JsonSerializationContext context) {
            
            json.addProperty("structure", value.structureName);
        }
        
        @Override
        public CheckStructure deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final String name = JSONUtils.getString(json, "structure");
            return new CheckStructure(name);
        }
    }
}