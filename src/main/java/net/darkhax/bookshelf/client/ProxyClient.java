package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.features.Feature;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        for (final Feature feature : Bookshelf.features)
            feature.setupRendering();
    }
}