package net.darkhax.bookshelf;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.features.bookshelves.FeatureBookshelves;
import net.darkhax.bookshelf.features.playerheads.FeaturePlayerHeads;
import net.darkhax.bookshelf.features.supporters.FeatureSupporters;
import net.darkhax.bookshelf.handler.ConfigurationHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    /**
     * A list of feature objects. The feature system is specifically intended for features that
     * are bundled with the library and should not be considered part of the API/Library.
     */
    public static List<Feature> features = new ArrayList<Feature>();
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        
        features.add(new FeatureBookshelves());
        features.add(new FeaturePlayerHeads());
        features.add(new FeatureSupporters());
        
        new ConfigurationHandler(event.getSuggestedConfigurationFile());
        
        for (final Feature feature : features)
            feature.onPreInit();
            
        proxy.preInit();
    }
}