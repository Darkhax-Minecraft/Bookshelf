package net.darkhax.bookshelf.event;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

public class CreativeTabEvent extends Event {
    
    /**
     * The instance of the CreativeTab.
     */
    public CreativeTabs tab;
    
    /**
     * A List of ItemStacks that are being added to the tab.
     */
    public List<ItemStack> itemList;
    
    @Cancelable
    public static class Pre extends CreativeTabEvent {
        
        /**
         * Called before the tab loads all of its contained ItemStacks. This can be used to add
         * or remove ItemStacks from the tab, and can also be canceled to prevent the tab from
         * having any items in it.
         * 
         * @param tab: The instance of the CreativeTab.
         * @param itemList: A list of ItemStack that will be contained by the CreativeTab.
         */
        public Pre(CreativeTabs tab, List<ItemStack> itemList) {
            
            this.tab = tab;
            this.itemList = itemList;
        }
    }
    
    public static class Post extends CreativeTabEvent {
        
        /**
         * Called after the CreativeTab has had a chance to load all of its items. This event
         * can not be canceled, however it can be used to add new items and blocks.
         * 
         * @param tab: The instance of the CreativeTab.
         * @param itemList: A list of ItemStack that will be contained by the CreativeTab.
         */
        public Post(CreativeTabs tab, List<ItemStack> itemList) {
            
            this.tab = tab;
            this.itemList = itemList;
        }
    }
}