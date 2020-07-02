package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.world.server.ServerWorld;

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
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        
        if (pos != null && this.loadStructure()) {

            ServerWorld world = ctx.getWorld();
            // return this.structure.isPositionInsideStructure(world, pos);
            // TODO: MCP-name func_241112_a_ -> getStructureManager
            return world.func_241112_a_().func_235010_a_(pos, true, this.structure).isValid();

        }
        
        return false;
    }
    
    private boolean loadStructure () {
        
        if (this.structure == null) {
            
            this.structure = Structure.field_236365_a_.get(this.structureName);

            if (this.structure == null) {
                
                Bookshelf.LOG.error("Loot table condition is looking for structure {} which doesn't exist.", this.structureName);
                return false;
            }
        }
        
        return true;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }

    static class Serializer implements LootCondtionSerializer<CheckStructure> {
        public LootConditionType lootConditionType = null;

        @Override
        public void setType(LootConditionType lcType) {
            lootConditionType = lcType;
        }

        @Override
        public String getName() {
            return Bookshelf.MOD_ID + ":check_structure";
        }

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