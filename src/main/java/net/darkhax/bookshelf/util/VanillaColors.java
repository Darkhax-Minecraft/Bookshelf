package net.darkhax.bookshelf.util;

import java.awt.Color;

public enum VanillaColors {
    
    BLACK("dyeBlack", new Color(25, 25, 25)),
    RED("dyeRed", new Color(153, 51, 51)),
    GREEN("dyeGreen", new Color(102, 127, 51)),
    BROWN("dyeBrown", new Color(102, 76, 51)),
    BLUE("dyeBlue", new Color(51, 76, 178)),
    PURPLE("dyePurple", new Color(127, 63, 178)),
    CYAN("dyeCyan", new Color(76, 127, 153)),
    LIGHT_GRAY("dyeLightGray", new Color(153, 153, 153)),
    GRAY("dyeGray", new Color(76, 76, 76)),
    PINK("dyePink", new Color(242, 127, 165)),
    LIME("dyeLime", new Color(127, 204, 25)),
    YELLOW("dyeYellow", new Color(229, 229, 51)),
    LIGHT_BLUE("dyeLightBlue", new Color(102, 153, 216)),
    MAGENTA("dyeMagenta", new Color(178, 76, 216)),
    ORANGE("dyeOrange", new Color(216, 127, 5)),
    WHITE("dyeWhite", new Color(255, 255, 255));
    
    /**
     * The oreDictionary name of the dye that relates to the color.
     */
    public String colorName;
    
    /**
     * An instance of Color that represents the color.
     */
    public Color colorObj;
    
    /**
     * An Enumeration of all the vanilla Minecraft colors. Each color value is based on the
     * color information provided by the Minecraft Wiki. This Enumeration also links each Color
     * with it's oreDictionary dye counterpart.
     * 
     * @param name: The OreDictionary entry for the name of the associated dye.
     * @param color: A Color object that represents the color of the entry.
     */
    VanillaColors(String name, Color color) {
        
        colorName = name;
        colorObj = color;
    }
}