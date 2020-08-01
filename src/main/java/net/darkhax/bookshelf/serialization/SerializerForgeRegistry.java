package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class SerializerForgeRegistry<V extends IForgeRegistryEntry<V>> implements ISerializer<V> {
    
    private final IForgeRegistry<V> registry;
    
    public SerializerForgeRegistry(IForgeRegistry<V> registry) {
        
        this.registry = registry;
    }
    
    @Override
    public V read (JsonElement json) {
        
        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(json);
        
        if (this.registry.containsKey(id)) {
            
            return this.registry.getValue(id);
        }
        
        else {
            
            throw new JsonParseException("Could not find " + this.registry.getRegistryName().toString() + " with ID " + id.toString());
        }
    }
    
    @Override
    public JsonElement write (V toWrite) {
        
        return Serializers.RESOURCE_LOCATION.write(toWrite.getRegistryName());
    }
    
    @Override
    public V read (PacketBuffer buffer) {
        
        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(buffer);
        
        if (this.registry.containsKey(id)) {
            
            return this.registry.getValue(id);
        }
        
        else {
            
            throw new IllegalStateException("Could not find " + this.registry.getRegistryName().toString() + " with ID " + id.toString());
        }
    }
    
    @Override
    public void write (PacketBuffer buffer, V toWrite) {
        
        Serializers.RESOURCE_LOCATION.write(buffer, toWrite.getRegistryName());
    }
}