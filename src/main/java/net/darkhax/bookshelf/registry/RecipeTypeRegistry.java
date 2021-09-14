package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.util.RecipeUtils;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class RecipeTypeRegistry {
    
    private final String ownerId;
    private final Logger logger;
    private final Map<ResourceLocation, IRecipeType<?>> recipeTypes;
    
    public RecipeTypeRegistry(String ownerId, Logger logger) {
        
        this.ownerId = ownerId;
        this.logger = logger;
        this.recipeTypes = new HashMap<>();
    }
    
    public <T extends IRecipe<?>> IRecipeType<T> register (final String key) {
        
        final ResourceLocation id = new ResourceLocation(this.ownerId, key);
        final IRecipeType<T> type = IRecipeType.register(this.ownerId + ":" + key);
        this.recipeTypes.put(id, type);
        return type;
    }
    
    public void initialize (IEventBus bus) {
        
        if (!this.recipeTypes.isEmpty() && FMLEnvironment.dist == Dist.CLIENT) {
            
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onRecipesSynced);
        }
    }
    
    private void onRecipesSynced (RecipesUpdatedEvent event) {
        
        for (final IRecipeType<?> type : this.recipeTypes.values()) {
            
            final Map<ResourceLocation, ?> recipes = RecipeUtils.getRecipes(type, event.getRecipeManager());
            final int namespaces = recipes.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet()).size();
            this.logger.info("Loaded {} {} recipes from {} namespaces", recipes.size(), type.toString(), namespaces);
        }
    }
}
