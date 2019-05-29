package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryEntryAdapter<T extends IForgeRegistryEntry<T>> implements JsonDeserializer<T>, JsonSerializer<T> {

    private final IForgeRegistry<T> registry;

    public RegistryEntryAdapter(IForgeRegistry<T> registry) {

        this.registry = registry;
    }

    @Override
    public T deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        final String id = json.getAsString();
        return id == null ? null : this.registry.getValue(new ResourceLocation(id));
    }
    
    @Override
    public JsonElement serialize (T src, Type typeOfSrc, JsonSerializationContext context) {
        
        return new JsonPrimitive(src.getRegistryName().toString());
    }
}