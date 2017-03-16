package net.darkhax.bookshelf;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.client.render.item.RenderItemWrapper;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.features.attribcap.FeatureAttributeFix;
import net.darkhax.bookshelf.features.bookshelves.FeatureBookshelves;
import net.darkhax.bookshelf.features.crashes.FeatureCrashComments;
import net.darkhax.bookshelf.features.playerheads.FeaturePlayerHeads;
import net.darkhax.bookshelf.features.supporters.FeatureSupporters;
import net.darkhax.bookshelf.handler.ConfigurationHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
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

    /**
     * A list of feature objects. The feature system is specifically intended for features that
     * are bundled with the library and should not be considered part of the API/Library.
     */
    public static final List<Feature> FEATURES = new ArrayList<>();

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());

        FEATURES.add(new FeatureBookshelves());
        FEATURES.add(new FeaturePlayerHeads());
        FEATURES.add(new FeatureSupporters());
        FEATURES.add(new FeatureAttributeFix());
        FEATURES.add(new FeatureCrashComments());

        new ConfigurationHandler(event.getSuggestedConfigurationFile());

        for (final Feature feature : FEATURES) {
            feature.onPreInit();
        }

        GameRegistry.registerTileEntity(TileEntityBasicChest.class, "basic_chest");
        proxy.preInit();
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void init (FMLInitializationEvent event) {

        RenderItemWrapper.instance();
    }
}