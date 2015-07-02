package net.darkhax.bookshelf;

import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.util.Constants;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static net.darkhax.bookshelf.common.ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
    
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