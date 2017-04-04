package net.darkhax.bookshelf;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class BookshelfConfig {

    private static Configuration config;

    public BookshelfConfig (File file) {

        config = new Configuration(file);
        this.syncConfigData();
    }

    private void syncConfigData () {

        if (config.hasChanged()) {
            config.save();
        }
    }
}