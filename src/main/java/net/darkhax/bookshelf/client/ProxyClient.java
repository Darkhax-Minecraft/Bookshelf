package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.minecraft.client.Minecraft;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        PlayerUtils.clientID = PlayerUtils.fixStrippedUUID(Minecraft.getMinecraft().getSession().getPlayerID());
        
        for (final Feature feature : Bookshelf.features)
            feature.setupRendering();
    }
}