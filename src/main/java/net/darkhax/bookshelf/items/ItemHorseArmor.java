package net.darkhax.bookshelf.items;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemHorseArmor extends Item {
    
    /**
     * Used to calculate the amount of protection this item should provide the horse with.
     * 
     * @param horse: An instance of the horse which is currently wearing this piece of armor.
     * @param stack: An instance of the ItemStack currently in the horse's armor slot.
     * @return int: An integer based representation of the armor points which should be given
     *         to a horse by this armor item.
     */
    public abstract int getArmorValue (EntityHorse horse, ItemStack stack);
    
    /**
     * Called every time an EntityHorse updates while wearing this piece of armor. Allows for
     * armor to alter the behavior and abilities of a horse.
     * 
     * @param horse: An instance of the horse which is currently wearing this piece of armor.
     * @param stack: An instance of the ItemStack currently in the horse's armor slot.
     */
    public abstract void onHorseUpdate (EntityHorse horse, ItemStack stack);
    
    /**
     * Used to provide a resource path for the armor's texture. Textures follow the same rules
     * and layouts as the vanilla horse armor textures. The domain for the texture can be
     * switched from minecraft to one of your chosing by preceding the path with 'domain:'
     * 
     * @param horse: An instance of the horse which is currently wearing this piece of armor.
     * @param stack: An instance of the ItemStack currently in the horse's armor slot.
     * @return String: A string based representation of the armor texture resource location.
     *         EG: testmod:textures/entity/horse/armor/horse_armor_test.png
     */
    @SideOnly(Side.CLIENT)
    public abstract String getArmorTexture (EntityHorse horse, ItemStack stack);
    
    /**
     * Provides a way to handle custom rendering when a horse is wearing this piece of horse
     * armor. The parameters are based on those supplied by the RenderLivingEvent.
     * 
     * @param entity: Instance of the EntityHorse wearing our custom armor.
     * @param stack: Instance of the ItemStack in the horses custom armor slot.
     * @param renderer: Instance of the RendererLivingEntity for rendering purposes.
     * @param posX: The entities x position.
     * @param posY: The Entity's y position.
     * @param posZ: The Entity's Z position.
     * @param flag: A flag which can be used to check what rendering stage is being used. 0:pre
     *            1:post 2:special-pre 3:special-post
     */
    @SideOnly(Side.CLIENT)
    public abstract void onArmorRendering (EntityHorse entity, ItemStack stack, RendererLivingEntity renderer, double posX, double posY, double posZ, int flag);
}
