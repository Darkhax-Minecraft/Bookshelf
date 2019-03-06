package net.darkhax.bookshelf.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryEntryAdapter<T extends IForgeRegistryEntry<T>> implements JsonDeserializer<T> {

    private final IForgeRegistry<T> registry;

    public RegistryEntryAdapter(IForgeRegistry<T> registry) {

        this.registry = registry;
    }

    @Override
    public T deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final String id = json.getAsString();
        return id == null ? null : this.registry.getValue(new ResourceLocation(id));
    }
}