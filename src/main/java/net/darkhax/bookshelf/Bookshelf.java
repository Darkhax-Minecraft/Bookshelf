package net.darkhax.bookshelf;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.darkhax.bookshelf.command.CommandItemColor;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.common.network.packet.PacketSyncPlayerProperties;
import net.darkhax.bookshelf.handler.ExpansionHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    public static SimpleNetworkWrapper network;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        network = NetworkRegistry.INSTANCE.newSimpleChannel("Bookshelf");
        Utilities.registerMessage(network, PacketSyncPlayerProperties.class, 0, Side.CLIENT);
        
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        
        ExpansionHandler.expandEnchantmentList();
        ExpansionHandler.expandPotionArray();
    }
    
    @EventHandler
    public void init (FMLInitializationEvent event) {
        
        proxy.init();
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        proxy.postInit();
    }
    
    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        event.registerServerCommand(new CommandItemColor());
    }
}