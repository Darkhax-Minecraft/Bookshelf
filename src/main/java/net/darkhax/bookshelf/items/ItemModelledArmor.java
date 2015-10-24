package net.darkhax.bookshelf.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.darkhax.bookshelf.util.Utilities;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public abstract class ItemModelledArmor extends ItemArmor {
    
    /**
     * A special implementation of ItemArmor which provides built in support for special armor
     * models, and their respective textures.
     * 
     * @param material: An ArmorMaterial to use for this armor types. Used for durability,
     *            protection amounts, and so on.
     * @param armorType: The type of armor. 0 is helmet, 1 is plate, 2 is legs and 3 is boots
     */
    public ItemModelledArmor(ArmorMaterial material, int armorType) {
        
        super(material, 4, armorType);
    }
    
    @Override
    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type) {
        
        return getModelTextureName(stack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel (EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
        
        ModelBiped armorModel = getArmorModel(stack);
        Utilities.synArmorModelToEntity(armorModel, armorSlot, entityLiving);
        return armorModel;
    }
    
    /**
     * Provides the correct model for the piece of armor being rendered.
     * 
     * @param stack: The ItemStack of the armor that is being rendered.
     * @return ModelBiped: The model that should be rendered for the given piece of armor.
     */
    @SideOnly(Side.CLIENT)
    public abstract ModelBiped getArmorModel (ItemStack stack);
    
    /**
     * Used to generate the path for the texture that should be used for the model. You do not
     * need to worry about adding the .png extension, it is added for you.
     * 
     * @param stack: The ItemStack of the armor that is being rendered.
     * @return String: The full string path to the texture. Example:
     *         yourmod:textures/entity/player/armor/armor_texture
     */
    @SideOnly(Side.CLIENT)
    public abstract String getModelTextureName (ItemStack stack);
}
