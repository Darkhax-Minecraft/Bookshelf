package net.darkhax.bookshelf.features;

import net.darkhax.bookshelf.creativetab.CreativeTabSkulls;
import net.minecraftforge.common.config.Configuration;

public class FeaturePlayerHeads extends Feature {
    
    private boolean enabled = true;
    
    @Override
    public void onPreInit () {
        
        if (this.enabled)
            new CreativeTabSkulls();
    }
    
    @Override
    public void setupConfig (Configuration config) {
        
        this.enabled = config.getBoolean("Enabled", "Player Heads", true, "While enabled, variations of the vanilla mob heads will be added in the form of a new creative tab.");
    }
}