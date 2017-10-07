/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.rendering;

import net.darkhax.bookshelf.client.model.ModelPlayerMob;
import net.darkhax.bookshelf.entity.EntityPlayerMob;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class provides the basic rendering code for a player mob. A player mob is one that
 * resembles a player, like zombies. This also includes logic for baby/child variations. There
 * is an entity and model class for this mob available.
 */
@SideOnly(Side.CLIENT)
public abstract class RenderPlayerMob<T extends EntityPlayerMob> extends RenderLiving<T> {

    /**
     * The blend profile. If null, it will skip.
     */
    private GlStateManager.Profile blendProfile;
    
    public RenderPlayerMob (RenderManager renderManager, ModelPlayerMob model) {

        super(renderManager, model, 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.blendProfile = Profile.PLAYER_SKIN;
    }
    
    public void setBlendProfile(Profile profile) {
        
        this.blendProfile = profile;
    }

    @Override
    public ModelPlayerMob getMainModel () {

        return (ModelPlayerMob) super.getMainModel();
    }

    @Override
    public void doRender (T entity, double x, double y, double z, float entityYaw, float partialTicks) {

        double heightOffset = y;

        if (entity.isSneaking()) {

            heightOffset = y - 0.125D;
        }

        this.setModelVisibilities(entity);
        
        final boolean hasProfile = this.blendProfile != null;
        
        if (hasProfile) {
            
            GlStateManager.enableBlendProfile(this.blendProfile);
        }
        
        super.doRender(entity, x, heightOffset, z, entityYaw, partialTicks);
        
        if (hasProfile) {
            
            GlStateManager.disableBlendProfile(this.blendProfile);
        }
    }

    @Override
    public void transformHeldFull3DItemLayer () {

        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    protected void preRenderCallback (T entitylivingbaseIn, float partialTickTime) {

        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void setModelVisibilities (T entity) {

        final ModelPlayerMob model = this.getMainModel();
        final ItemStack mainhand = entity.getHeldItemMainhand();
        final ItemStack offhand = entity.getHeldItemOffhand();

        model.setVisible(true);
        this.setOverlayVisibility(entity, model);
        model.isSneak = entity.isSneaking();

        final ModelBiped.ArmPose mainArm = this.getPoseForStack(entity, mainhand);
        final ModelBiped.ArmPose offhandArm = this.getPoseForStack(entity, offhand);

        final boolean isRightHanded = entity.getPrimaryHand() == EnumHandSide.RIGHT;

        model.rightArmPose = isRightHanded ? mainArm : offhandArm;
        model.leftArmPose = isRightHanded ? offhandArm : mainArm;
    }

    public void setOverlayVisibility (T entity, ModelPlayerMob model) {

        model.bipedHeadwear.showModel = true;
        model.bodyOverlay.showModel = true;
        model.leftLegOverlay.showModel = true;
        model.rightLegLverlay.showModel = true;
        model.leftArmOverlay.showModel = true;
        model.rightArmOverlay.showModel = true;
    }

    public ArmPose getPoseForStack (T entity, ItemStack stack) {

        ModelBiped.ArmPose pose = ModelBiped.ArmPose.EMPTY;

        if (!stack.isEmpty()) {

            pose = ModelBiped.ArmPose.ITEM;

            if (entity.getItemInUseCount() > 0) {

                final EnumAction actionType = stack.getItemUseAction();

                pose = actionType == EnumAction.BLOCK ? ArmPose.BLOCK : actionType == EnumAction.BOW ? ArmPose.BOW_AND_ARROW : ArmPose.EMPTY;
            }
        }

        return pose;
    }
}