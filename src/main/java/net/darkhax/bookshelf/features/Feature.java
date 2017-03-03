package net.darkhax.bookshelf.features;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Feature {

    /**
     * Called during the mod pre-initialization phase.
     */
    public void onPreInit () {

        // This should be overridden if you want to use it.
    }

    /**
     * Called during the mod initialization phase.
     */
    public void onInit () {

        // This should be overridden if you want to use it.
    }

    /**
     * Called during the mod post initialization phase
     */
    public void onPostInit () {

        // This should be overridden if you want to use it.
    }

    /**
     * Called during setup of the configuration file.
     *
     * @param config The incofiguration file instance.
     */
    public void setupConfig (Configuration config) {

        // This should be overridden if you want to use it.
    }

    /**
     * Called during the end of the initialization phase. Intended for handling any rendering
     * related code.
     */
    @SideOnly(Side.CLIENT)
    public void setupRendering () {

        // This should be overridden if you want to use it.
    }
}
