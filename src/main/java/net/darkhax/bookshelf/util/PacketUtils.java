/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Collection;
import java.util.Optional;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PacketUtils {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static BlockState deserializeBlockState (PacketBuffer buffer) {
        
        final ResourceLocation id = buffer.readResourceLocation();
        final Block block = ForgeRegistries.BLOCKS.getValue(id);
        
        if (block != null) {
            
            final int size = buffer.readInt();
            
            BlockState state = block.getDefaultState();
            
            for (int i = 0; i < size; i++) {
                
                final String propName = buffer.readString();
                final String value = buffer.readString();
                
                // Check the block for the property. Keys = property names.
                final Property blockProperty = block.getStateContainer().getProperty(propName);
                
                if (blockProperty != null) {
                    
                    // Attempt to parse the value with the the property.
                    final Optional<Comparable> propValue = blockProperty.parseValue(value);
                    
                    if (propValue.isPresent()) {
                        
                        // Update the state with the new property.
                        try {
                            
                            state = state.with(blockProperty, propValue.get());
                        }
                        
                        catch (final Exception e) {
                            
                            Bookshelf.LOG.error("Failed to read state for block {}. The mod that adds this block has issues.", block.getRegistryName());
                            Bookshelf.LOG.catching(e);
                        }
                    }
                }
            }
            
            return state;
        }
        
        return Blocks.AIR.getDefaultState();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void serializeBlockState (PacketBuffer buffer, BlockState state) {
        
        buffer.writeResourceLocation(state.getBlock().getRegistryName());
        
        final Collection<Property<?>> properties = state.func_235904_r_();
        
        buffer.writeInt(properties.size());
        
        for (final Property property : properties) {
            
            buffer.writeString(property.getName());
            buffer.writeString(state.get(property).toString());
        }
    }
    
    public static void serializeStringArray (PacketBuffer buffer, String[] strings) {
        
        buffer.writeInt(strings.length);
        
        for (final String string : strings) {
            
            buffer.writeString(string);
        }
    }
    
    public static String[] deserializeStringArray (PacketBuffer buffer) {
        
        final String[] strings = new String[buffer.readInt()];
        
        for (int i = 0; i < strings.length; i++) {
            
            strings[i] = buffer.readString();
        }
        
        return strings;
    }
    
    public static void serializeStringCollection (PacketBuffer buffer, Collection<String> strings) {
        
        buffer.writeInt(strings.size());
        
        for (final String string : strings) {
            
            buffer.writeString(string);
        }
    }
    
    public static void deserializeStringCollection (PacketBuffer buffer, Collection<String> toFill) {
        
        final int count = buffer.readInt();
        
        for (int i = 0; i < count; i++) {
            
            toFill.add(buffer.readString());
        }
    }
}