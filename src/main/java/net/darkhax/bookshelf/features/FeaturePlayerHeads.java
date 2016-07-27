package net.darkhax.bookshelf.features;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.creativetab.CreativeTabSkulls;
import net.darkhax.bookshelf.handler.SupporterHandler;
import net.darkhax.bookshelf.handler.SupporterHandler.SupporterData;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.darkhax.bookshelf.lib.util.SkullUtils;
import net.darkhax.bookshelf.lib.util.SkullUtils.MHFAccount;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class FeaturePlayerHeads extends Feature {
    
    private boolean enabled = true;
    
    @Override
    public void onPreInit () {
        
        if (this.enabled)
            new CreativeTabSkulls();
    }
    
    @Override
    public void setupConfig (Configuration config) {
        
        this.enabled = config.getBoolean("Enabled", "Player Heads", true, "While enabled, variations of the vanilla mob heads will be added in the form of a new creative tab.");
    }
    
    /**
     * The creative tab to put the player heads in.
     */
    public static class CreativeTabSkulls extends CreativeTabs {
        
        /**
         * The ItemStack to display for the creative tab.
         */
        private static ItemStack displayStack = null;
        
        /**
         * A cache of all the player skull ItemStacks so they don't need to be recreated.
         */
        private static List<ItemStack> cache = new ArrayList<ItemStack>();
        
        public CreativeTabSkulls() {
            
            super("bookshelfheads");
            
            this.setBackgroundImageName("item_search.png");
            displayStack = SkullUtils.createSkull("Darkhax");
            
            for (final SupporterData data : SupporterHandler.getSupporters())
                if (data.wantsHead()) {
                    
                    final ItemStack stack = SkullUtils.createSkull(PlayerUtils.getPlayerNameFromUUID(data.getPlayerID()));
                    ItemStackUtils.setLore(stack, new String[] { data.getFormat() + data.getLocalizedType() });
                    cache.add(stack);
                }
                
            for (final MHFAccount mhf : MHFAccount.values())
                cache.add(SkullUtils.createSkull(mhf));
        }
        
        @Override
        public Item getTabIconItem () {
            
            return Items.SKULL;
        }
        
        @Override
        public ItemStack getIconItemStack () {
            
            return displayStack;
        }
        
        @Override
        public boolean hasSearchBar () {
            
            return true;
        }
        
        @Override
        public void displayAllRelevantItems (List<ItemStack> itemList) {
            
            itemList.addAll(cache);
        }
    }
}