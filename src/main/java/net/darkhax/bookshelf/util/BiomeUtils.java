package net.darkhax.bookshelf.util;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeUtils {
    
    public static Set<Biome> getBiomesForTypes (Type... types) {
        
        final Set<Biome> biomes = new HashSet<>();
        
        for (final Type type : types) {
            
            biomes.addAll(BiomeDictionary.getBiomes(type));
        }
        
        return biomes;
    }
}
