/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
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