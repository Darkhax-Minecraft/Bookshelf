package net.darkhax.bookshelf.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

    private static Configuration config;

    public ConfigurationHandler (File file) {

        config = new Configuration(file);
        this.syncConfigData();
    }

    private void syncConfigData () {

        if (config.hasChanged()) {
            config.save();
        }
    }
}