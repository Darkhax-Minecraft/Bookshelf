package net.darkhax.bookshelf;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import net.darkhax.bookshelf.command.CommandItemColor;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.common.network.packet.PacketSyncPlayerProperties;
import net.darkhax.bookshelf.handler.ExpansionHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.Utilities;

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