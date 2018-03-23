package net.darkhax.bookshelf;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class BookshelfConfig {

    public static Configuration config;

    public static boolean oreDictMisc;
    public static boolean oreDictFence;
    public static boolean oreDictShulker;
    public static boolean oreDictSeeds;
    public static boolean oreDictRails;
    public static boolean oreDictArrows;

    public BookshelfConfig (File file) {

        config = new Configuration(file);
        this.syncConfigData();
    }

    private void syncConfigData () {

        final String category = "oredict";
        config.setCategoryComment(category, "WARNIG: Changing these config options may break recipes in mods that use Bookshelf.");
        oreDictMisc = config.getBoolean("misc", category, true, "Should misc blocks like magma blocks and bone blocks be put in the ore dict?");
        oreDictFence = config.getBoolean("fence", category, true, "Should vanilla fences and fence gates be ore dicted?");
        oreDictShulker = config.getBoolean("shulker", category, true, "Should vanilla shulker boxes be ore dicted?");
        oreDictSeeds = config.getBoolean("seeds", category, true, "Should vanilla seeds be ore dicted?");
        oreDictRails = config.getBoolean("rails", category, true, "Should vanilla rails be ore dicted?");
        oreDictArrows = config.getBoolean("arrows", category, true, "Should vanilla arrows be ore dicted?");

        if (config.hasChanged()) {
            config.save();
        }
    }
}