package net.darkhax.bookshelf.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeUtils {
    
    /**
     * A cache of type names to their value. This is implemented to make type lookup much
     * faster as forge does not offer this for some reason. This is populated by the first call
     * to {@link #getType(String)}
     */
    private static Map<String, Type> typeMap = new HashMap<>();
    
    /**
     * Gets a list of biomes for a type string.
     *
     * @param typeName The name of the type. This should be upper case.
     * @return The biomes of that type.
     */
    public static Set<Biome> getBiomesForType (String typeName) {
        
        final Type type = getType(typeName);
        return getBiomesForTypes(type);
    }
    
    /**
     * Gets a type by it's string name.
     *
     * @param name The name to look for. This should be upper case.
     * @return The biome type.
     */
    public static BiomeDictionary.Type getType (String name) {
        
        if (typeMap.isEmpty()) {
            
            for (final Type type : BiomeDictionary.Type.getAll()) {
                
                typeMap.put(type.getName(), type);
            }
        }
        
        return typeMap.get(name.toUpperCase());
    }
    
    /**
     * Gets a set of biomes for multiple types.
     *
     * @param types The types to get for.
     * @return A set of the biomes.
     */
    public static Set<Biome> getBiomesForTypes (Type... types) {
        
        final Set<Biome> biomes = new HashSet<>();
        
        for (final Type type : types) {
            
            if (type != null) {
                
                biomes.addAll(BiomeDictionary.getBiomes(type));
            }
        }
        
        return biomes;
    }
}
