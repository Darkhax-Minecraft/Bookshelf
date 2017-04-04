package net.darkhax.bookshelf.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModelArmorExtended extends ModelBiped {

    /**
     * Override this hook to add new model parts.
     */
    public abstract void addModelParts ();

    /**
     * Updates the pose/state of the model to reflect that of the entity it's attatched to.
     *
     * @param entity The entity to sync the model with.
     * @param partialTicks The partial ticks.
     */
    private void syncModel (Entity entity, float partialTicks) {

        final EntityLivingBase living = (EntityLivingBase) entity;
        this.isSneak = living != null ? living.isSneaking() : false;
        this.isChild = living != null ? living.isChild() : false;

        if (living != null && living instanceof EntityPlayer) {

            final EntityPlayer player = (EntityPlayer) living;
            final ArmPose poseMainhand = this.getArmPose(player.getHeldItemMainhand(), player);
            final ArmPose poseOffhand = this.getArmPose(player.getHeldItemOffhand(), player);
            final boolean isRightHanded = player.getPrimaryHand() == EnumHandSide.RIGHT;

            this.rightArmPose = isRightHanded ? poseMainhand : poseOffhand;
            this.leftArmPose = isRightHanded ? poseOffhand : poseMainhand;
            this.swingProgress = player.getSwingProgress(partialTicks);
        }
    }

    /**
     * Utility method for updating the rotation angles for a model part.
     *
     * @param render The model renderer.
     * @param x The rotation angle on the X axis.
     * @param y The rotation angle on the Y axis.
     * @param z The rotation angle on the Z exis.
     */
    public void setRotateAngle (ModelRenderer render, float x, float y, float z) {

        render.rotateAngleX = x;
        render.rotateAngleY = y;
        render.rotateAngleZ = z;
    }

    /**
     * Gets the arm pose for a player's hand.
     *
     * @param stack The stack in the player's hand.
     * @param player The player to get info for.
     * @return The pose of the player's hand.
     */
    private ArmPose getArmPose (ItemStack stack, EntityPlayer player) {

        if (stack.isEmpty())
            return ArmPose.EMPTY;

        ArmPose pose = ModelBiped.ArmPose.ITEM;

        if (player.getItemInUseCount() > 0) {

            final EnumAction action = stack.getItemUseAction();
            pose = action == EnumAction.BLOCK ? ArmPose.BLOCK : action == EnumAction.BOW ? ArmPose.BOW_AND_ARROW : pose;
        }

        return pose;
    }

    @Override
    public void render (Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {

        this.addModelParts();

        GlStateManager.pushMatrix();

        // Fix for armor stands being annoying
        if (entity instanceof EntityArmorStand) {

            netHeadYaw = 0;
            GlStateManager.translate(0F, 0.15F, 0F);
        }

        this.syncModel(entity, partialTicks);
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
        GlStateManager.popMatrix();
    }
}