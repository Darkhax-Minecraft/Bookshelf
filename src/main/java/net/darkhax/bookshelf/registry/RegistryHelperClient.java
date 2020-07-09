package net.darkhax.bookshelf.registry;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;

public class RegistryHelperClient extends RegistryHelper {
    
    public final SpriteRegistry sprites;
    
    public RegistryHelperClient(String ownerId, Logger logger) {
        
        super(ownerId, logger);
        this.sprites = new SpriteRegistry(ownerId, logger);
    }
    
    @Override
    public void initialize (IEventBus modBus) {
        
        super.initialize(modBus);
        
        this.sprites.initialize(modBus);;
    }
}