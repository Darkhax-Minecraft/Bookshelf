/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class provides the basic model code for a player mob. A player mob is one that
 * resembles a player, like zombies. An entity and renderer class for this mob type also exist.
 */
@OnlyIn(Dist.CLIENT)
public class ModelPlayerMob extends ModelBiped {

    public ModelRenderer leftArmOverlay;
    public ModelRenderer rightArmOverlay;
    public ModelRenderer leftLegOverlay;
    public ModelRenderer rightLegLverlay;
    public ModelRenderer bodyOverlay;

    private final boolean isFeminine;

    public ModelPlayerMob (float modelSize, boolean isFeminine) {

        super(modelSize, 0.0F, 64, 64);
        this.isFeminine = isFeminine;

        if (isFeminine) {

            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            this.leftArmOverlay = new ModelRenderer(this, 48, 48);
            this.leftArmOverlay.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.leftArmOverlay.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.rightArmOverlay = new ModelRenderer(this, 40, 32);
            this.rightArmOverlay.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.rightArmOverlay.setRotationPoint(-5.0F, 2.5F, 10.0F);
        }

        else {

            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.leftArmOverlay = new ModelRenderer(this, 48, 48);
            this.leftArmOverlay.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.leftArmOverlay.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.rightArmOverlay = new ModelRenderer(this, 40, 32);
            this.rightArmOverlay.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.rightArmOverlay.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.leftLegOverlay = new ModelRenderer(this, 0, 48);
        this.leftLegOverlay.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.leftLegOverlay.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.rightLegLverlay = new ModelRenderer(this, 0, 32);
        this.rightLegLverlay.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.rightLegLverlay.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bodyOverlay = new ModelRenderer(this, 16, 32);
        this.bodyOverlay.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bodyOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render (Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild) {

            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
            this.leftLegOverlay.render(scale);
            this.rightLegLverlay.render(scale);
            this.leftArmOverlay.render(scale);
            this.rightArmOverlay.render(scale);
            this.bodyOverlay.render(scale);
        }

        else {

            if (entity.isSneaking()) {

                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }

            this.leftLegOverlay.render(scale);
            this.rightLegLverlay.render(scale);
            this.leftArmOverlay.render(scale);
            this.rightArmOverlay.render(scale);
            this.bodyOverlay.render(scale);
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles (float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        copyModelAngles(this.bipedLeftLeg, this.leftLegOverlay);
        copyModelAngles(this.bipedRightLeg, this.rightLegLverlay);
        copyModelAngles(this.bipedLeftArm, this.leftArmOverlay);
        copyModelAngles(this.bipedRightArm, this.rightArmOverlay);
        copyModelAngles(this.bipedBody, this.bodyOverlay);
    }

    @Override
    public void setVisible (boolean visible) {

        super.setVisible(visible);
        this.leftArmOverlay.showModel = visible;
        this.rightArmOverlay.showModel = visible;
        this.leftLegOverlay.showModel = visible;
        this.rightLegLverlay.showModel = visible;
        this.bodyOverlay.showModel = visible;
    }

    @Override
    public void postRenderArm (float scale, EnumHandSide side) {

        final ModelRenderer modelrenderer = this.getArmForSide(side);

        if (this.isFeminine) {

            final float f = 0.5F * (side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        }

        else {

            modelrenderer.postRender(scale);
        }
    }
}