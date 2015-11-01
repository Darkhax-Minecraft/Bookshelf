package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        MinecraftForge.EVENT_BUS.register(new RenderingHandler());
        PlayerUtils.currentBlockDamage = ReflectionHelper.findField(PlayerControllerMP.class, "g", "field_78770_f", "curBlockDamageMP");
        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://raw.githubusercontent.com/Darkhax-Minecraft/Bookshelf/master/versions.json");
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void postInit () {
    
    }
}
