/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Use net.darkhax.bookshelf.serialization.Serializers instead.
 */
@Deprecated
public final class MCJsonUtils {
    
    @Nullable
    public static JsonObject loadJson (Gson gson, IResourceManager manager, ResourceLocation name) throws IOException {
        
        try (Reader reader = new BufferedReader(new InputStreamReader(manager.getResource(name).getInputStream(), StandardCharsets.UTF_8));) {
            
            return JSONUtils.fromJson(gson, reader, JsonObject.class);
        }
    }
    
    public static <T extends IForgeRegistryEntry<T>> T getRegistryEntry (JsonObject json, String memberName, IForgeRegistry<T> registry) {
        
        if (json.has(memberName)) {
            
            return getRegistryEntry(json.get(memberName), memberName, registry);
        }
        
        else {
            
            throw new JsonSyntaxException("Missing required value " + memberName);
        }
    }
    
    public static <T extends IForgeRegistryEntry<T>> T getRegistryEntry (JsonElement json, String memberName, IForgeRegistry<T> registry) {
        
        if (json == null) {
            
            throw new JsonSyntaxException("The property " + memberName + " is missing.");
        }
        
        if (json.isJsonPrimitive()) {
            
            final String rawId = json.getAsString();
            final ResourceLocation registryId = ResourceLocation.tryCreate(rawId);
            
            if (registryId != null) {
                
                final T registryEntry = registry.getValue(registryId);
                
                if (registryEntry != null) {
                    
                    return registryEntry;
                }
                
                else {
                    
                    throw new JsonSyntaxException("No entry found for id " + rawId);
                }
            }
            
            else {
                
                throw new JsonSyntaxException("Registry id " + rawId + " for property " + memberName + " was not a valid format.");
            }
        }
        
        else {
            
            throw new JsonSyntaxException("Expected " + memberName + " to be a JSON primitive. was " + JSONUtils.toString(json));
        }
    }
    
    public static Block getBlock (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.BLOCKS);
    }
    
    public static Fluid getFluid (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.FLUIDS);
    }
    
    public static Item getItem (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.ITEMS);
    }
    
    public static Effect getPotion (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.POTIONS);
    }
    
    public static Biome getBiome (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.BIOMES);
    }
    
    public static SoundEvent getSound (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.SOUND_EVENTS);
    }
    
    public static Potion getPotionType (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.POTION_TYPES);
    }
    
    public static Enchantment getEnchantment (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.ENCHANTMENTS);
    }
    
    public static EntityType<?> getEntity (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.ENTITIES);
    }
    
    public static TileEntityType<?> getTileEntity (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.TILE_ENTITIES);
    }
    
    public static ParticleType<?> getParticleType (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.PARTICLE_TYPES);
    }
    
    public static PaintingType getPainting (JsonObject json, String memberName) {
        
        return getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.PAINTING_TYPES);
    }
    
    public static BlockState deserializeBlockState (JsonObject json) {
        
        // Read the block from the forge registry.
        final Block block = getBlock(json, "block");
        
        // Start off with the default state.
        BlockState state = block.getDefaultState();
        
        // If the properties member exists, attempt to assign properties to the block state.
        if (json.has("properties")) {
            
            final JsonElement propertiesElement = json.get("properties");
            
            if (propertiesElement.isJsonObject()) {
                
                final JsonObject props = propertiesElement.getAsJsonObject();
                
                // Iterate each member of the properties object. Expecting a simple key to
                // primitive string structure.
                for (final Entry<String, JsonElement> property : props.entrySet()) {
                    
                    // Check the block for the property. Keys = property names.
                    final Property blockProperty = block.getStateContainer().getProperty(property.getKey());
                    
                    if (blockProperty != null) {
                        
                        if (property.getValue().isJsonPrimitive()) {
                            
                            // Attempt to parse the value with the the property.
                            final String valueString = property.getValue().getAsString();
                            final Optional<Comparable> propValue = blockProperty.parseValue(valueString);
                            
                            if (propValue.isPresent()) {
                                
                                // Update the state with the new property.
                                try {
                                    
                                    state = state.with(blockProperty, propValue.get());
                                }
                                
                                catch (final Exception e) {
                                    
                                    Bookshelf.LOG.error("Failed to update state for block {}. The mod that adds this block has issues.", block.getRegistryName());
                                    Bookshelf.LOG.catching(e);
                                }
                            }
                            
                            else {
                                
                                throw new JsonSyntaxException("The property " + property.getKey() + " with value " + valueString + " coul not be parsed!");
                            }
                        }
                        
                        else {
                            
                            throw new JsonSyntaxException("Expected property value for " + property.getKey() + " to be primitive string. Got " + JSONUtils.toString(property.getValue()));
                        }
                    }
                    
                    else {
                        
                        throw new JsonSyntaxException("The property " + property.getKey() + " is not valid for block " + block.getRegistryName());
                    }
                }
            }
            
            else {
                
                throw new JsonSyntaxException("Expected properties to be an object. Got " + JSONUtils.toString(propertiesElement));
            }
        }
        
        return state;
    }
    
    public static List<String> getStrings (JsonElement element) {
        
        if (JSONUtils.isString(element)) {
            
            return Collections.singletonList(element.getAsString());
        }
        
        else if (element.isJsonArray()) {
            
            final List<String> entries = new ArrayList<>();
            
            for (final JsonElement entry : element.getAsJsonArray()) {
                
                entries.addAll(getStrings(entry));
            }
            
            return entries;
        }
        
        return Collections.emptyList();
    }
    
    public static List<ResourceLocation> getIds (JsonElement element) {
        
        return getStrings(element).stream().map(ResourceLocation::tryCreate).collect(Collectors.toList());
    }
}