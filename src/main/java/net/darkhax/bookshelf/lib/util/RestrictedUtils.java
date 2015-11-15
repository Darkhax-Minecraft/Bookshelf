package net.darkhax.bookshelf.lib.util;

import java.lang.reflect.Field;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;

public class RestrictedUtils {
    
    /**
     * Access to the guiLeft field in GuiContainer. Used to get the starting X position of a
     * GUI.
     */
    private static Field guiLeft = null;
    
    /**
     * Access to the guiTop field in GuiContainer. Used to get the starting Y position of a
     * GUI.
     */
    private static Field guiTop = null;
    
    /**
     * Retrieves the starting X position of a Gui. This is handled through reflection. If the
     * guiLeft field has not been made accessible, this method will do that.
     * 
     * @param gui: An instance of the GuiContainer to grab the guiLeft variable from.
     * @return int: The staring X position of the passed Gui.
     */
    public static int getGuiLeft (GuiContainer gui) {
        
        if (guiLeft == null) {
            
            guiLeft = ReflectionHelper.findField(GuiContainer.class, "i", "field_147003", "guiLeft");
            guiLeft.setAccessible(true);
        }
        
        try {
            
            return guiLeft.getInt(gui);
        }
        
        catch (Exception exception) {
            
            exception.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Retrieves the starting Y position of a Gui. This is handled through reflection. If the
     * guiTop field has not been made accessible, this method will do that.
     * 
     * @param gui: An instance of the GuiContainer to grab the guiTop variable from.
     * @return int: The starting Y position of the passed Gui.
     */
    public static int getGuiTop (GuiContainer gui) {
        
        if (guiTop == null) {
            
            guiTop = ReflectionHelper.findField(GuiContainer.class, "r", "field_147009_r", "guiTop");
            guiTop.setAccessible(true);
        }
        
        try {
            
            return guiTop.getInt(gui);
        }
        
        catch (Exception exception) {
            
            exception.printStackTrace();
            return 0;
        }
    }
}
