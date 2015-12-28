package net.darkhax.bookshelf;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import net.darkhax.bookshelf.command.CommandItemColor;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.common.network.packet.*;
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
        Utilities.registerMessage(network, PacketAddPlayerProperties.class, 1, Side.CLIENT);
        Utilities.registerMessage(network, PacketRemovePlayerProperties.class, 2, Side.CLIENT);
        
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
        Utilities.checkDuplicatePotions();
    }
    
    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        event.registerServerCommand(new CommandItemColor());
    }
}