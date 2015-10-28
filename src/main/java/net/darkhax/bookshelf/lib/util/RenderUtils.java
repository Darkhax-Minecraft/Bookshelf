package net.darkhax.bookshelf.lib.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class RenderUtils {
    
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
    
}
