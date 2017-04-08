package net.darkhax.bookshelftest.config;

import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.darkhax.bookshelf.config.ConfigurationHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Config(name = "configtest")
@Mod(modid = "configtest", name = "Config Test", version = "1.0.0.0")
public class ConfigTest {

    public static ConfigurationHandler config;

    @Configurable(description = "A configurable integer in the main class")
    public static int mainInt = 10;

    @Configurable(name = "renamedProperty", description = "A configurable string in the main class")
    public static String mainString = "This is a string";

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        config = new ConfigurationHandler("configtest");
        config.init(event.getAsmData());
        config.sync();
    }
}