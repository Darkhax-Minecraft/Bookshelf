package net.darkhax.bookshelf.asm;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ASMConfig {
    
    private static Configuration config;
    
    public static void loadConfigOptions () {
        
        config = new Configuration(new File("config/bookshelf_asm.cfg"));
        
        config.save();
    }
}