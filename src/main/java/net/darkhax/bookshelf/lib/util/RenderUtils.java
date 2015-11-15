package net.darkhax.bookshelf.lib.util;

import java.awt.Color;
import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderUtils {
    
    /**
     * Access to the guiLeft field in GuiContainer. Used to get the starting X position of a
     * GUI.
     */
    public static Field guiLeft = null;
    
    /**
     * Access to the guiTop field in GuiContainer. Used to get the starting Y position of a
     * GUI.
     */
    public static Field guiTop = null;
    
    /**
     * Synchronizes a ModelBiped to an EntityLivingBase. This method is intended for use in
     * rendering armor models which use ModelBiped as a base.
     * 
     * @param armorModel: The armor model to synchronize to the entity.
     * @param armorSlot: The type of armor which is being used.
     * @param entity: The entity to synchronize the armor model to.
     */
    @SideOnly(Side.CLIENT)
    public static void synArmorModelToEntity (ModelBiped armorModel, int armorSlot, EntityLivingBase entity) {
        
        if (armorModel == null || entity == null)
            return;
            
        armorModel.bipedHead.showModel = armorSlot == 0;
        armorModel.bipedHeadwear.showModel = armorSlot == 0;
        armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        armorModel.bipedRightArm.showModel = armorSlot == 1;
        armorModel.bipedLeftArm.showModel = armorSlot == 1;
        armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.isSneak = entity.isSneaking();
        armorModel.isRiding = entity.isRiding();
        armorModel.isChild = entity.isChild();
        armorModel.heldItemRight = entity.getEquipmentInSlot(0) != null ? 1 : 0;
        
        if (entity instanceof EntityPlayer)
            armorModel.aimedBow = ((EntityPlayer) entity).getItemInUseDuration() > 2;
    }
    
    /**
     * A wrapper for glColor3f. This wrapper takes an integer and splits it up into it's RGB
     * components. This method primarily exists to reduce the complexity required to use the
     * glColor3f method through ASM.
     * 
     * @param colorVal: A single integer which represents the RGB compoinents of a color.
     */
    @SideOnly(Side.CLIENT)
    public static void renderColor (int colorVal) {
        
        Color color = new Color(colorVal);
        GL11.glColor3f(((float) color.getRed() / 255f), ((float) color.getGreen() / 255f), ((float) color.getBlue() / 255f));
    }
    
    @SideOnly(Side.CLIENT)
    public static void drawTextureModalRectSize (int posX, int posY, int u, int v, int width, int length, float size, float zLevel) {
        
        float f = 1 / size;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (posX + 0), (double) (posY + length), (double) zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + length) * f));
        tessellator.addVertexWithUV((double) (posX + width), (double) (posY + length), (double) zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + length) * f));
        tessellator.addVertexWithUV((double) (posX + width), (double) (posY + 0), (double) zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + 0) * f));
        tessellator.addVertexWithUV((double) (posX + 0), (double) (posY + 0), (double) zLevel, (double) ((float) (u) * f), (double) ((float) (v + 0) * f));
        tessellator.draw();
    }
    
    /**
     * Retrieves the starting X position of a Gui. This is handled through reflection. If the
     * guiLeft field has not been made accessible, this method will do that.
     * 
     * @param gui: An instance of the GuiContainer to grab the guiLeft variable from.
     * @return int: The staring X position of the passed Gui.
     */
    @SideOnly(Side.CLIENT)
    public static int getGuiLeft (GuiContainer gui) {
        
        if (guiLeft == null)
            return 0;
            
        try {
            
            return guiLeft.getInt(gui);
        }
        
        catch (Exception exception) {
            
            exception.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Retrieves the starting Y position of a Gui. This is handled through reflection. If the
     * guiTop field has not been made accessible, this method will do that.
     * 
     * @param gui: An instance of the GuiContainer to grab the guiTop variable from.
     * @return int: The starting Y position of the passed Gui.
     */
    @SideOnly(Side.CLIENT)
    public static int getGuiTop (GuiContainer gui) {
        
        if (guiTop == null)
            return 0;
            
        try {
            
            return guiTop.getInt(gui);
        }
        
        catch (Exception exception) {
            
            exception.printStackTrace();
            return 0;
        }
    }
}
