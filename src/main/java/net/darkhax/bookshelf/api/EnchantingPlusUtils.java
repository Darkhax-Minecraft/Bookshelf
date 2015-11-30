package net.darkhax.bookshelf.api;

import java.awt.Color;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.lib.util.Utilities;

public class EnchantingPlusUtils {
    
    /**
     * Access to the Enchanting Plus creative tab. This tab is used for all items and blocks in
     * the Enchanting Plus mod.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabEnchantingPlus = Utilities.getTabFromLabel("eplus");
    
    /**
     * Blacklists an Enchantment from being available on the Enchanting Plus table.
     * 
     * @param enchant: The Enchantment to blacklist.
     */
    public static void blacklistEnchantment (Enchantment enchant) {
        
        FMLInterModComms.sendMessage("eplus", "blacklistEnchantments", "" + enchant.effectId);
    }
    
    /**
     * Blacklists an Item from the Enchanting Plus enchantment table.
     * 
     * @param item: The Item to blacklist.
     */
    public static void blacklistItem (Item item) {
        
        blacklistItem(new ItemStack(item));
    }
    
    /**
     * Blacklists an ItemStack from the Enchanting Plus enchantment table.
     * 
     * @param stack: The ItemStack to blacklist.
     */
    public static void blacklistItem (ItemStack stack) {
        
        FMLInterModComms.sendMessage("eplus", "blacklistItems", stack);
    }
    
    /**
     * Sets the color of the ribbon used on enchanted scrolls which contain an enchantment
     * belonging to this type.
     * 
     * @param type: The EnumEnchantmentType to apply the color to.
     * @param color: The Color to associate with the EnumEnchantmentType.
     */
    public static void setEnchantmentColor (EnumEnchantmentType type, Color color) {
        
        setEnchantmentColor(type, color.getRGB());
    }
    
    /**
     * Sets the color of the ribbon used on enchanted scrolls which contain an enchantment
     * belonging to this type.
     * 
     * @param type: The EnumEnchantmentType to apply this color to.
     * @param color: An RGB integer which represents the color to assign to the
     *            EnumEnchantmentType.
     */
    public static void setEnchantmentColor (EnumEnchantmentType type, int color) {
        
        FMLInterModComms.sendMessage("eplus", "setEnchantmentColor", type.name() + ":" + color);
    }
}