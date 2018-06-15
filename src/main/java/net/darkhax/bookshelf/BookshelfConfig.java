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

    public static int translateEnchantmentCount;

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

        final String category1 = "translations";
        config.setCategoryComment(category1, "Adds support for additional levels. Lowering this can improve memory.");
        translateEnchantmentCount = config.getInt("enchantmentCount", category1, 256, 0, Short.MAX_VALUE, "The amount of enchantment levels to translate. 10 or less will disable this.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}