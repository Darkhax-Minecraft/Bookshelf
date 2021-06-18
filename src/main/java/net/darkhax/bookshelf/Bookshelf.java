package net.darkhax.bookshelf;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bookshelf {
    
    // System Constants
    public static final Random RANDOM = new Random();
    
    public static final String NEW_LINE = System.getProperty("line.separator");
    
    // Mod Constants
    public static final String MOD_ID = "bookshelf";
    
    public static final String MOD_NAME = "Bookshelf";
    
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
}