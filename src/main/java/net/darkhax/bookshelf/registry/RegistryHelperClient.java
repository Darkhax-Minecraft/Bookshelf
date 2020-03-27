package net.darkhax.bookshelf.registry;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class RegistryHelperClient extends RegistryHelper {
    
    public RegistryHelperClient(String modid, Logger logger, ItemGroup group) {
        
        super(modid, logger, group);
    }
    
    @Override
    public void initialize (IEventBus modBus) {
        
        super.initialize(modBus);
        
        if (!this.sprites.isEmpty()) {
            
            modBus.addListener(this::registerSprites);
        }
    }
    
    /**
     * Sprite Atlas Injectors
     */
    private final Multimap<ResourceLocation, ResourceLocation> sprites = ArrayListMultimap.create();
    
    public ResourceLocation registerSprite (ResourceLocation atlas, String sprite) {
        
        return this.registerSprite(atlas, new ResourceLocation(this.modid, sprite));
    }
    
    public ResourceLocation registerSprite (ResourceLocation atlas, ResourceLocation sprite) {
        
        this.sprites.put(atlas, sprite);
        return sprite;
    }
    
    private void registerSprites (TextureStitchEvent.Pre event) {
        
        for (final ResourceLocation toAdd : this.sprites.get(event.getMap().getTextureLocation())) {
            
            event.addSprite(toAdd);
        }
    }
}