package net.darkhax.bookshelf.creativetab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.darkhax.bookshelf.handler.BookshelfHooks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class CreativeTabCached extends CreativeTabs {
    
    /**
     * A List of all items which make use of this creative tab. This list is populated in the
     * same way a normal list is, however rather then generating the list every time the gui is
     * displayed, the entries are cached. This should greatly reduce CPU cycles, especially
     * when the client has many items installed.
     */
    private List<Item> cachedItems = new ArrayList<Item>();
    
    /**
     * Constructs a new CreativeTabEfficient. This implementation of CreativeTab will cache the
     * instances of Items used to generate the ItemList, rather then loop through every Item
     * and Block in the game.
     * 
     * @param lable: A string assigned to your creative tab, used primarily for translating the
     *            name of your tab. It is highly recommended to prefix your label key with your
     *            mod ID to help prevent translation conflicts. Example: "modid.labelName"
     */
    public CreativeTabCached(String lable) {
        
        super(lable);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems (List itemList) {
        
        if (BookshelfHooks.onCreativeTabDisplayPre(this, itemList))
            return;
            
        if (this.cachedItems.isEmpty()) {
            
            Iterator iterator = Item.itemRegistry.iterator();
            
            while (iterator.hasNext()) {
                
                Item item = (Item) iterator.next();
                
                if (item != null)
                    for (CreativeTabs tab : item.getCreativeTabs())
                        if (tab == this)
                            this.cachedItems.add(item);
            }
        }
        
        for (Item item : this.cachedItems)
            if (item != null)
                for (CreativeTabs tab : item.getCreativeTabs())
                    if (tab == this)
                        item.getSubItems(item, this, itemList);
                        
        if (this.func_111225_m() != null)
            this.addEnchantmentBooksToList(itemList, this.func_111225_m());
            
        BookshelfHooks.onCreativeTabDisplayPost(this, itemList);
    }
}