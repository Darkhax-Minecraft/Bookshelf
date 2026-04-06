package net.darkhax.bookshelf.common.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookshelfMod {

    public static final String MOD_ID = "bookshelf";
    public static final String MOD_NAME = "Bookshelf";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}