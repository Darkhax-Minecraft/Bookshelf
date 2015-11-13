package net.darkhax.bookshelf.handler;

import java.util.List;

import net.darkhax.bookshelf.event.CreativeTabEvent;
import net.darkhax.bookshelf.event.ItemEnchantedEvent;
import net.darkhax.bookshelf.event.PotionCuredEvent;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;

public class BookshelfHooks {
    
    /**
     * A hook for triggering the ItemEnchantedEvent. This hook is only triggered from
     * ContainerEnchantment, however it is possible to integrate this hook into other mods and
     * enchanting methods.
     *
     * @param player: The player that is enchanting the item.
     * @param stack: The item that is being enchanted.
     * @param levels: The amount of levels consumed by the enchantment.
     * @param enchantments: A list of enchantments being added to the ItemStack.
     * @return List<EnchantmentData>: A list of enchantments to add to the ItemStack.
     */
    public static List<EnchantmentData> onItemEnchanted (EntityPlayer player, ItemStack stack, int levels, List<EnchantmentData> enchantments) {
        
        if (enchantments != null) {
            
            ItemEnchantedEvent event = new ItemEnchantedEvent(player, stack, levels, enchantments);
            MinecraftForge.EVENT_BUS.post(event);
            return event.enchantments;
        }
        
        return enchantments;
    }
    
    /**
     * A hook for the CreativeTabEvent.pre. This method is called before the tab initializes
     * its items.
     * 
     * @param tab: The tab being loaded.
     * @param itemList: The list of items contained by the tab.
     * @return boolean: Whether or not the event has been canceled. If it has, prevent further
     *         entries from being added to the tab contents.
     */
    public static boolean onCreativeTabDisplayPre (CreativeTabs tab, List itemList) {
        
        return MinecraftForge.EVENT_BUS.post(new CreativeTabEvent.Pre(tab, itemList));
    }
    
    /**
     * A hook for the CreativeTabEvent.post. This method is called after the tab has all of its
     * normal items loaded.
     * 
     * @param tab: The tab being loaded.
     * @param itemList: The list of items contained by the tab.
     */
    public static void onCreativeTabDisplayPost (CreativeTabs tab, List itemList) {
        
        MinecraftForge.EVENT_BUS.post(new CreativeTabEvent.Post(tab, itemList));
    }
    
    /**
     * A hook for when a cure item is applied to an entity.
     * 
     * @param entity: The Entity being cured.
     * @param cureItem: The ItemStack being used to cure the entity.
     * @return boolean: Whether or not the event has been canceled.
     */
    public static boolean onPotionsCured (EntityLivingBase entity, ItemStack cureItem) {
        
        return MinecraftForge.EVENT_BUS.post(new PotionCuredEvent(entity, cureItem));
    }
    
    /**
     * A hook into the constructor of Potion. This hook is not publicly available, and is only
     * used to prevent two or more Potions from using the same ID. This should prevent many
     * unintended issues.
     * 
     * @param potion: The Potion that is being constructed.
     */
    public static void onPotionConstructed (Potion potion) {
        
        if (potion != null && Utilities.getPotion(potion.id) != null)
            throw new IllegalArgumentException("Duplicate Potion id! " + potion.getClass().getName() + " and " + Utilities.getPotion(potion.id).getClass().getName() + " Potion ID:" + potion.id);
    }
}