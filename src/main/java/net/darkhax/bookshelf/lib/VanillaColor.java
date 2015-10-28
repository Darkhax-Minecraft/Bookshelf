package net.darkhax.bookshelf.lib;

import java.awt.Color;

public enum VanillaColor {
    
    BLACK("Black", new Color(25, 25, 25)),
    RED("Red", new Color(153, 51, 51)),
    GREEN("Green", new Color(102, 127, 51)),
    BROWN("Brown", new Color(102, 76, 51)),
    BLUE("Blue", new Color(51, 76, 178)),
    PURPLE("Purple", new Color(127, 63, 178)),
    CYAN("Cyan", new Color(76, 127, 153)),
    LIGHT_GRAY("LightGray", new Color(153, 153, 153)),
    GRAY("Gray", new Color(76, 76, 76)),
    PINK("Pink", new Color(242, 127, 165)),
    LIME("Lime", new Color(127, 204, 25)),
    YELLOW("Yellow", new Color(229, 229, 51)),
    LIGHT_BLUE("LightBlue", new Color(102, 153, 216)),
    MAGENTAG("Magenta", new Color(178, 76, 216)),
    ORANGE("Orange", new Color(216, 127, 5)),
    WHITE("White", new Color(255, 255, 255));
    
    /**
     * An English representation of the name for the color.
     */
    public String name;
    
    /**
     * An instance of Color that reflects the vanilla color.
     */
    public Color color;
    
    /**
     * A simple enumeration of all the vanilla Minecraft colors. This contains a string
     * representation of the color, which is mostly used for Ore Dictionary stuff, and a Color
     * which holds all the data for the color.
     * 
     * @param name: A name which represents the color within Minecraft. Example: Gray
     * @param color: A Color which holds the RGB value for this color.
     */
    VanillaColor(String name, Color color) {
        
        this.name = name;
        this.color = color;
    }
    
    /**
     * Provides the OreDictionary name for the dye item associated with this color.
     * 
     * @return String: A string which represents the associated dye within the OreDictionary.
     */
    public String getDyeName () {
        
        return "dye" + name;
    }
    
    /**
     * Provides the OreDictionary name for the Dyed Glass Pane associated with this color.
     * 
     * @return String: A string which represents the associated Dyed Glass Pane within the
     *         OreDictionary.
     */
    public String getGlassPaneName () {
        
        return "paneGlass" + name;
    }
    
    /**
     * Provides the OreDictionary name for the Dyed Glass Block associated with this color.
     * 
     * @return String: A String which represents the associated Dyed Glass Block within the
     *         OreDictionary.
     */
    public String getGlassBlockName () {
        
        return "blockGlass" + name;
    }
    
    /**
     * Attempts to retrieve a color, based on a provided name. The provided name does not need
     * to match the casings of the actual color name.
     * 
     * @param name: The name of the color you are looking for. Example: Green
     * @return VanillaColor: A VenillaColor which reflects the provided name. If no name is
     *         found, it will be null.
     */
    public static VanillaColor getColorByName (String name) {
        
        for (VanillaColor color : VanillaColor.values())
            if (color.name.equalsIgnoreCase(name))
                return color;
                
        return null;
    }
}
