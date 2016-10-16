package net.darkhax.bookshelf.lib.modutils.baubles;

import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * This implementation of IBauble provides default implementations of most methods. This is to
 * allow cleaner item code, as only one new method is required, and the rest can be overriden
 * as needed.
 */
@Optional.Interface(modid = "Baubles", iface = "baubles.api.IBauble")
public abstract class ItemBauble extends Item implements IBauble {
    
    @Override
    public void onWornTick (ItemStack itemstack, EntityLivingBase player) {
        
    }
    
    @Override
    public void onEquipped (ItemStack itemstack, EntityLivingBase player) {
        
    }
    
    @Override
    public void onUnequipped (ItemStack itemstack, EntityLivingBase player) {
        
    }
    
    @Override
    public boolean canEquip (ItemStack itemstack, EntityLivingBase player) {
        
        return true;
    }
    
    @Override
    public boolean canUnequip (ItemStack itemstack, EntityLivingBase player) {
        
        return true;
    }
}