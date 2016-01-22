package net.darkhax.bookshelf;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    public static SimpleNetworkWrapper network;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        network = NetworkRegistry.INSTANCE.newSimpleChannel("Bookshelf");
        
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    }
    
    @EventHandler
    public void init (FMLInitializationEvent event) {
        
        proxy.init();
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        proxy.postInit();
    }
}