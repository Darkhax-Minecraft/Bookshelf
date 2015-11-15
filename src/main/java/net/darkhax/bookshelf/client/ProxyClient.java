package net.darkhax.bookshelf.client;

import net.minecraft.client.multiplayer.PlayerControllerMP;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.ReflectionHelper;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.lib.util.PlayerUtils;

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
