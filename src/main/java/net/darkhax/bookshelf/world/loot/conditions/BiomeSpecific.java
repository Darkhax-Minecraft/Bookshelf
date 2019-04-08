/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.world.loot.conditions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeSpecific implements LootCondition {
    
    /**
     * The Biome to limit things to.
     */
    private final Biome biome;
    
    public BiomeSpecific(Biome biome) {
        
        this.biome = biome;
    }
    
    @Override
    public boolean testCondition (Random rand, LootContext context) {
        
        return context.getKiller() != null && this.biome != null ? this.biome == context.getWorld().getBiome(context.getKiller().getPosition()) : false;
    }
    
    public static class Serializer extends LootCondition.Serializer<BiomeSpecific> {
        
        public Serializer() {
            
            super(new ResourceLocation(Bookshelf.MOD_ID, "biome_specific"), BiomeSpecific.class);
        }
        
        @Override
        public void serialize (JsonObject json, BiomeSpecific value, JsonSerializationContext context) {
            
            json.add("biome_id", context.serialize(value.biome.getRegistryName().toString()));
        }
        
        @Override
        public BiomeSpecific deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new BiomeSpecific(ForgeRegistries.BIOMES.getValue(new ResourceLocation(context.deserialize(json.get("biome_id"), String.class))));
        }
    }
}