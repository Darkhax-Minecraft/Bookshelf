package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * A serializer for loot table conditions which exist as a singleton.
 *
 * @param <T> The type of the singleton.
 */
public class SerializerSingleton<T extends ILootCondition> extends ILootCondition.AbstractSerializer<T> {
    
    private final T singleton;
    
    public SerializerSingleton(String modId, String id, Class<T> clazz, T singleton) {
        
        this(new ResourceLocation(modId, id), clazz, singleton);
    }
    
    public SerializerSingleton(String id, Class<T> clazz, T singleton) {
        
        this(ResourceLocation.tryCreate(id), clazz, singleton);
    }
    
    public SerializerSingleton(ResourceLocation location, Class<T> clazz, T singleton) {
        
        super(location, clazz);
        this.singleton = singleton;
    }
    
    @Override
    public void serialize (JsonObject json, T value, JsonSerializationContext context) {
        
        // No need to serialize a singleton.
    }
    
    @Override
    public T deserialize (JsonObject json, JsonDeserializationContext context) {
        
        return this.singleton;
    }
}