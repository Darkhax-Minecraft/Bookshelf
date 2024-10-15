package net.darkhax.bookshelf.common.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String MOD_ID = "bookshelf";
    public static final String MOD_NAME = "Bookshelf";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, ResourceLocation> ID_CACHE = new HashMap<>();

    public static ResourceLocation id(String path) {
        return ID_CACHE.computeIfAbsent(path, p -> ResourceLocation.tryBuild(MOD_ID, p));
    }
}