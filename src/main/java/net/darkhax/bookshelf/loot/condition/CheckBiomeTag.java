package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

/**
 * A loot condition that checks the biome dictionary tags of the current biome.
 */
public class CheckBiomeTag implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    /**
     * The biome type to search for.
     */
    private final Type biomeType;
    
    public CheckBiomeTag(Type type) {
        
        this.biomeType = type;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        
        if (pos != null) {
            
            final Biome biome = ctx.getWorld().getBiome(pos);
            
            if (biome != null) {
                
                return BiomeDictionary.hasType(biome, this.biomeType);
            }
        }
        
        return false;
    }
    
    static class Serializer extends ILootCondition.AbstractSerializer<CheckBiomeTag> {
        
        Serializer() {
            
            super(new ResourceLocation(Bookshelf.MOD_ID, "check_biome_tag"), CheckBiomeTag.class);
        }
        
        @Override
        public void serialize (JsonObject json, CheckBiomeTag value, JsonSerializationContext context) {
            
            json.addProperty("tag", value.biomeType.getName());
        }
        
        @Override
        public CheckBiomeTag deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final Type tag = Type.getType(JSONUtils.getString(json, "tag"));
            return new CheckBiomeTag(tag);
        }
    }
}