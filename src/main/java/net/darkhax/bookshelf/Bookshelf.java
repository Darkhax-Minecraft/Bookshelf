package net.darkhax.bookshelf;

import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.items.ItemTest;
import net.darkhax.bookshelf.util.Constants;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static net.darkhax.bookshelf.common.ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
    
        proxy.preInit();
        
        GameRegistry.registerItem(new ItemTest(), "testmail");
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