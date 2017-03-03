package net.darkhax.bookshelf.lib;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Constants {

    // Mod Constants
    public static final String MOD_ID = "bookshelf";

    public static final String MOD_NAME = "Bookshelf";

    public static final String VERSION_NUMBER = "1.4.4.0";

    public static final String PROXY_CLIENT = "net.darkhax.bookshelf.client.ProxyClient";

    public static final String PROXY_COMMON = "net.darkhax.bookshelf.common.ProxyCommon";

    // System Constants
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);

    public static final Random RANDOM = new Random();

    public static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private Constants () {

        throw new IllegalAccessError("Utility class");
    }
}
