package net.darkhax.bookshelf;

import net.darkhax.bookshelf.client.render.item.RenderItemWrapper;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER)
public class Bookshelf {

    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static ProxyCommon proxy;

    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new BookshelfEvents());

        new BookshelfConfig(event.getSuggestedConfigurationFile());

        GameRegistry.registerTileEntity(TileEntityBasicChest.class, "basic_chest");
        proxy.preInit();
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void init (FMLInitializationEvent event) {

        RenderItemWrapper.instance();
    }
}