package net.darkhax.bookshelf.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionBase extends Potion {
    
    public ResourceLocation icon;
    public ItemStack iconStack;
    
    /**
     * 
     * @param id
     * @param isBad
     * @param colour
     * @param textures
     * @param icon
     */
    public PotionBase(int id, boolean isBad, int colour, ResourceLocation textures, int icon) {
        
        super(id, isBad, colour);
        this.icon = new ResourceLocation("bookshelf:textures/inventory/test.png");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect (int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        
        if (this.icon != null) {
            
            Minecraft.getMinecraft().renderEngine.bindTexture(this.icon);
            mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 238, 19, 18, 18);
        }
        
        else if (ItemStackUtils.isValidStack(this.iconStack)) {
            
            Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(1));
            mc.currentScreen.drawTexturedModelRectFromIcon(x + 8, y + 8, this.iconStack.getIconIndex(), 16, 16);
        }
    }
}