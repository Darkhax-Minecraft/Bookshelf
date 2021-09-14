package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
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
        
        final Vector3d pos = ctx.getParamOrNull(LootParameters.ORIGIN);
        
        if (pos != null) {
            
            final Biome biome = ctx.getLevel().getBiome(new BlockPos(pos));
            
            if (biome != null) {
                
                final RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
                return BiomeDictionary.hasType(biomeKey, this.biomeType);
            }
        }
        
        return false;
    }
    
    @Override
    public LootConditionType getType () {
        
        return Bookshelf.instance.conditionCheckBiomeTag;
    }
    
    static class Serializer implements ILootSerializer<CheckBiomeTag> {
        
        @Override
        public void serialize (JsonObject json, CheckBiomeTag value, JsonSerializationContext context) {
            
            json.addProperty("tag", value.biomeType.getName());
        }
        
        @Override
        public CheckBiomeTag deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final Type tag = Type.getType(JSONUtils.getAsString(json, "tag"));
            return new CheckBiomeTag(tag);
        }
    }
}