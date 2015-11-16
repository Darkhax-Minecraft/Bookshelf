package net.darkhax.bookshelf.asm;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ASMConfigs {
    
    private static Configuration config;
    
    public static boolean potionDetectionEnabled = true;
    public static boolean catchPotionException = false;
    
    public static void loadConfigOptions () {
        
        config = new Configuration(new File("config/bookshelf_asm.cfg"));
        String category = "Duplicate Potion IDs";
        potionDetectionEnabled = config.getBoolean("potionDetectionEnabled", category, potionDetectionEnabled, "Should Bookshelf create a warning in the console, every time a duplicate potion exception is detected?");
        catchPotionException = config.getBoolean("catchPotionException", category, catchPotionException, "If enabled, Bookshelf will catch duplicate potion errors. This will prevent the game from crashing, and give you a list of potential replacement IDs.");
        config.save();
    }
}