package net.darkhax.bookshelf.registry;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class SpriteRegistry {
    
    private final String ownerId;
    private final Logger logger;
    
    private final Multimap<ResourceLocation, ResourceLocation> sprites;
    
    public SpriteRegistry(String ownerId, Logger logger) {
        
        this.ownerId = ownerId;
        this.logger = logger;
        this.sprites = ArrayListMultimap.create();
    }
    
    public void initialize (IEventBus bus) {
        
        if (!this.sprites.isEmpty()) {
            
            bus.addListener(this::registerSprites);
        }
    }
    
    public ResourceLocation registerSprite (ResourceLocation atlas, String sprite) {
        
        return this.registerSprite(atlas, new ResourceLocation(this.ownerId, sprite));
    }
    
    public ResourceLocation registerSprite (ResourceLocation atlas, ResourceLocation sprite) {
        
        this.sprites.put(atlas, sprite);
        return sprite;
    }
    
    private void registerSprites (TextureStitchEvent.Pre event) {
        
        for (final ResourceLocation toAdd : this.sprites.get(event.getMap().location())) {
            
            event.addSprite(toAdd);
        }
        
        this.logger.info("Registered {} sprite injections.", this.sprites.size());
    }
}