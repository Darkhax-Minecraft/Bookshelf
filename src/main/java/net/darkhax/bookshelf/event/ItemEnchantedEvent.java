package net.darkhax.bookshelf.event;

import java.util.List;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.player.PlayerEvent;

public class ItemEnchantedEvent extends PlayerEvent {
    
    /**
     * The ItemStack being enchanted.
     */
    public ItemStack stack;
    
    /**
     * The amount of levels used to enchant the item.
     */
    public int levels;
    
    /**
     * A list of enchantments that are going to be applied to the enchantment.
     */
    public List<EnchantmentData> enchantments;
    
    /**
     * An event that is fired whenever an item is enchanted from an enchantment table. It is
     * possible for this event to be triggered from other places such as the Enchanting Plus
     * enchanting table. This event can not be canceled, however you can remove enchantments
     * from the enchantments list to prevent them from being applied. If the enchantments list
     * is set to null, no experience points will be deducted from the player, and no
     * enchantments will be applied, effectively cancelling the event.
     * 
     * @param player: The EntityPlayer that is trying to enchant the item.
     * @param stack: The ItemStack that is being enchanted.
     * @param levels: The amount of levels that were used to pay for this enchantment.
     * @param enchantments: A List of randomly generated enchantments that are applied to the
     *            item.
     */
    public ItemEnchantedEvent(EntityPlayer player, ItemStack stack, int levels, List<EnchantmentData> enchantments) {
        
        super(player);
        this.stack = stack;
        this.levels = levels;
        this.enchantments = enchantments;
    }
}
