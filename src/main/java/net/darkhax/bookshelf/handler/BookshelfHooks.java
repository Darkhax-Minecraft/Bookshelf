package net.darkhax.bookshelf.handler;

import java.util.List;

import net.darkhax.bookshelf.event.CreativeTabEvent;
import net.darkhax.bookshelf.event.ItemEnchantedEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    
    public static boolean onCreativeTabDisplayPre (CreativeTabs tab, List itemList) {
        
        return MinecraftForge.EVENT_BUS.post(new CreativeTabEvent.Pre(tab, itemList));
    }
    
    public static void onCreativeTabDisplayPost (CreativeTabs tab, List itemList) {
        
        MinecraftForge.EVENT_BUS.post(new CreativeTabEvent.Post(tab, itemList));
    }
}