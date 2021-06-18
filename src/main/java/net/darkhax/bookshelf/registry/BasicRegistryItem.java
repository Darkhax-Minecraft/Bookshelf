package net.darkhax.bookshelf.registry;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.registry.Registry;

public class BasicRegistryItem extends BasicRegistry<Item> {
    
    public BasicRegistryItem(Logger logger, String ownerId) {
        
        super(logger, ownerId, Registry.ITEM);
    }
    
    public Item createItem (String id, Settings settings) {
        
        return this.register(new Item(settings), id);
    }
    
    public Item createItem (String id) {
        
        return this.createItem(id, new Settings());
    }
}