package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
    
        // RenderUtils.guiLeft = ReflectionHelper.findField(GuiContainer.class, "i", "field_147003_i", "guiLeft");
        // RenderUtils.guiTop = ReflectionHelper.findField(GuiContainer.class, "r", "field_147009_r", "guiTop");
        PlayerUtils.currentBlockDamage = ReflectionHelper.findField(PlayerControllerMP.class, "g", "field_78770_f", "curBlockDamageMP");
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void postInit () {
    
    }
}
