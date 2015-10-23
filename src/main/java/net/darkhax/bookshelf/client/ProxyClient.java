package net.darkhax.bookshelf.client;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.util.Utilities;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.common.MinecraftForge;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        MinecraftForge.EVENT_BUS.register(new RenderingHandler());
        Utilities.currentBlockDamage = ReflectionHelper.findField(PlayerControllerMP.class, "g", "field_78770_f", "curBlockDamageMP");
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void postInit () {
    
    }
}
