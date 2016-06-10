package net.darkhax.bookshelf.handler;

import java.io.File;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {
    
    private static Configuration config;
    
    public ConfigurationHandler(File file) {
        
        config = new Configuration(file);
        MinecraftForge.EVENT_BUS.register(this);
        this.syncConfigData();
    }
    
    @SubscribeEvent
    public void onConfigChange (OnConfigChangedEvent event) {
        
        if (event.getModID().equals(Constants.MOD_ID))
            this.syncConfigData();
    }
    
    private void syncConfigData () {
        
        for (final Feature feature : Bookshelf.features)
            feature.setupConfig(config);
            
        if (config.hasChanged())
            config.save();
    }
}