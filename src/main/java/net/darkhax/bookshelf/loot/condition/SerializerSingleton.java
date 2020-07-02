package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * A serializer for loot table conditions which exist as a singleton.
 *
 * @param <T> The type of the singleton.
 */
public class SerializerSingleton<T extends ILootCondition> implements LootCondtionSerializer<T> {
    
    private final T singleton;
    public LootConditionType lootConditionType = null;
    public String name;

    public SerializerSingleton(String modId, String id, Class<T> clazz, T singleton) {
        
        this(new ResourceLocation(modId, id), clazz, singleton);
    }
    
    public SerializerSingleton(String id, Class<T> clazz, T singleton) {
        
        this(ResourceLocation.tryCreate(id), clazz, singleton);
    }
    
    public SerializerSingleton(ResourceLocation location, Class<T> clazz, T singleton) {
        name = location.toString();
        this.singleton = singleton;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void serialize (JsonObject json, T value, JsonSerializationContext context) {
        
        // No need to serialize a singleton.
    }
    
    @Override
    public T deserialize (JsonObject json, JsonDeserializationContext context) {
        
        return this.singleton;
    }

    @Override
    public void setType(LootConditionType lcType) {
        lootConditionType = lcType;
    }
}