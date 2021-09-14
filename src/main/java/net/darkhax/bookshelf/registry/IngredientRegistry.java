package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class IngredientRegistry {
    
    private final String ownerId;
    private final Logger logger;
    
    private final Map<ResourceLocation, IIngredientSerializer<?>> serializers;
    
    public IngredientRegistry(String ownerId, Logger logger) {
        
        this.ownerId = ownerId;
        this.logger = logger;
        this.serializers = new HashMap<>();
    }
    
    public <T extends Ingredient> IIngredientSerializer<T> register (String id, IIngredientSerializer<T> serializer) {
        
        this.serializers.put(new ResourceLocation(this.ownerId, id), serializer);
        return serializer;
    }
    
    public void initialize (IEventBus bus) {
        
        if (!this.serializers.isEmpty()) {
            
            bus.addGenericListener(IRecipeSerializer.class, this::registerIngredientSerializers);
        }
    }
    
    private void registerIngredientSerializers (RegistryEvent.Register<IRecipeSerializer<?>> event) {
        
        for (final Entry<ResourceLocation, IIngredientSerializer<?>> serializer : this.serializers.entrySet()) {
            
            CraftingHelper.register(serializer.getKey(), serializer.getValue());
        }
        
        this.logger.info("Registered {} ingredient serializers.", this.serializers.size());
    }
}