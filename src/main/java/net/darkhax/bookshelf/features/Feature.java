/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.features;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Feature {
    
    /**
     * Whether or not this feature is enabled.
     */
    protected boolean enabled;
    
    /**
     * The category name used for this feature in the config file.
     */
    protected String configName;
    
    /**
     * Called when the mod enters the preInit phase of loading. This is after
     * {@link #setupConfiguration(Configuration)} but before {@link #onClientPreInit()}.
     */
    public void onPreInit () {
        
    }
    
    /**
     * Called when the mod enters the init phase of loading.
     */
    public void onInit () {
        
    }
    
    /**
     * Called when the mod enters the postInit phase of loading.
     */
    public void onPostInit () {
        
    }
    
    /**
     * Called after all features have finished their preInit phase. Used for recipes.
     */
    public void setupRecipes () {
        
    }
    
    /**
     * Called before {@link #onPreInit()}. Allows for configuration options to be
     * detected/generated. A feature being enabled or not is handled automatically by the
     * feature manager.
     *
     * @param config The configuration object to pull data from.
     */
    public void setupConfiguration (Configuration config) {
        
    }
    
    /**
     * Checks if the feature subscribes to any events. This should return true if the feature
     * uses events.
     *
     * @return Whether or not the feature subscribes to any events.
     */
    public boolean usesEvents () {
        
        return false;
    }
    
    /**
     * Checks if the feature should be enabled by default.
     *
     * @return Whether or not the feature should be enabled by default.
     */
    public boolean enabledByDefault () {
        
        return true;
    }
    
    /**
     * Called while the mod is in the client side preInit phase.
     */
    @SideOnly(Side.CLIENT)
    public void onClientPreInit () {
        
    }
    
    /**
     * Called while the mod is in the client side init phase.
     */
    @SideOnly(Side.CLIENT)
    public void onClientInit () {
        
    }
    
    /**
     * Called while the mod is in the client side postInit phase.
     */
    @SideOnly(Side.CLIENT)
    public void onClientPostInit () {
        
    }
}