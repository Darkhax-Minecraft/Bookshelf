package net.darkhax.bookshelf.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;

public class ItemTest extends ItemHorseArmor {

    public ItemTest() {
        
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.setUnlocalizedName("test");
        this.maxStackSize = 1;
    }
    @Override
    public int getArmorValue (EntityHorse horse, ItemStack stack) {
    
        // TODO Auto-generated method stub
        return 9001;
    }

    @Override
    public void onHorseUpdate (EntityHorse horse, ItemStack stack) {
    
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getArmorTexture (EntityHorse horse, ItemStack stack) {
    
        // TODO Auto-generated method stub
        return "bookshelf:textures/entity/horse/armor/horse_armor_emerald.png";
    }

    @Override
    public void onArmorRendering () {
    
        // TODO Auto-generated method stub
        
    }
    
}
