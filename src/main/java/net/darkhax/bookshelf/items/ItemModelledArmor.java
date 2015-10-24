package net.darkhax.bookshelf.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public abstract class ItemModelledArmor extends ItemArmor {

    public ItemModelledArmor(ArmorMaterial material, int type) {
        super(material, 4, type);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        ItemArmor item = (ItemArmor) stack.getItem();
        switch (item.armorType) {
            case 2: {
                return getModelTextureName() + "_1.png";
            } default: {
                return getModelTextureName() + "_0.png";
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        int type = ((ItemArmor) itemStack.getItem()).armorType;
        ModelBiped armorModel;
        if (type == 1 || type == 3) {
            armorModel = getArmorModel(0);
        } else {
            armorModel = getArmorModel(1);
        }

        if (armorModel != null) {
            armorModel.bipedHead.showModel = armorSlot == 0;
            armorModel.bipedHeadwear.showModel = armorSlot == 0;
            armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
            armorModel.bipedRightArm.showModel = armorSlot == 1;
            armorModel.bipedLeftArm.showModel = armorSlot == 1;
            armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
            armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;

            if (entityLiving instanceof EntityPlayer) {
                armorModel.aimedBow = ((EntityPlayer) entityLiving).getItemInUseDuration() > 2;
            }

            return armorModel;
        }

        return null;
    }

    /**
     * Used to give each armor piece the right model.
     *
     * @param pass: The current pass Minecraft is rendering. This is 1 for the leggings, and 0 for all other pieces.
     * @return ModelBiped: The model for the armor piece.
     */
    @SideOnly(Side.CLIENT)
    public abstract ModelBiped getArmorModel(int pass);

    /**
     * Used to give the model its texture.
     *
     * @return String: The full string path to the texture. Minecraft will use the texture file ending with "_0" for
     *         the leggings, and "_1" for the other pieces. Make sure NOT to add the suffices to the texture path.
     *         EG: testmod:textures/entity/player/armor/player_armor_test
     */
    public abstract String getModelTextureName();
}
