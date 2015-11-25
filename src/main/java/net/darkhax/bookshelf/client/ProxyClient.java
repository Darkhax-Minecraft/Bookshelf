package net.darkhax.bookshelf.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.PlayerControllerMP;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.darkhax.bookshelf.lib.util.RenderUtils;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        MinecraftForge.EVENT_BUS.register(new RenderingHandler());
        RenderUtils.guiLeft = ReflectionHelper.findField(GuiContainer.class, "i", "field_147003_i", "guiLeft");
        RenderUtils.guiTop = ReflectionHelper.findField(GuiContainer.class, "r", "field_147009_r", "guiTop");
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
