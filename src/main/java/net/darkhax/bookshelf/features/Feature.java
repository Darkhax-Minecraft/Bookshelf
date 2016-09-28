package net.darkhax.bookshelf.features;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Feature {
    
    /**
     * Called during the mod pre-initialization phase.
     */
    public void onPreInit () {
        
    }
    
    /**
     * Called during the mod initialization phase.
     */
    public void onInit () {
        
    }
    
    /**
     * Called during the mod post initialization phase
     */
    public void onPostInit () {
        
    }
    
    /**
     * Called during setup of the configuration file.
     * 
     * @param config The incofiguration file instance.
     */
    public void setupConfig (Configuration config) {
        
    }
    
    /**
     * Called during the end of the initialization phase. Intended for handling any rendering
     * related code.
     */
    @SideOnly(Side.CLIENT)
    public void setupRendering () {
        
    }
}
