package net.darkhax.bookshelf;

import net.darkhax.bookshelf.command.CommandItemColor;
import net.darkhax.bookshelf.handler.EnchantmentListExpansionHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.handler.PotionArrayExpansionHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static net.darkhax.bookshelf.common.ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        new EnchantmentListExpansionHandler();
        new PotionArrayExpansionHandler();
    }
    
    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        
        proxy.init();
    }
    
    @Mod.EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        proxy.postInit();
    }
    
    @Mod.EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        event.registerServerCommand(new CommandItemColor());
    }
}