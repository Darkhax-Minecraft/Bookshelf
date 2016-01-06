package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.minecraftforge.common.MinecraftForge;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void postInit () {
    
    }
}
