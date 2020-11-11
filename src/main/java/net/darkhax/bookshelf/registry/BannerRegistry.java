package net.darkhax.bookshelf.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

public class BannerRegistry {
    
    private final String ownerId;
    private final Logger logger;
    private final ForgeRegistryHelper<Item> itemRegistry;
    
    private final Map<ResourceLocation, BannerPattern> patterns;
    private final Map<ResourceLocation, Item> patternItems;
    private final Map<BannerPattern, Item> itemsByPattern;
    
    public BannerRegistry(String ownerId, Logger logger, ForgeRegistryHelper<Item> itemRegistry) {
        
        this.ownerId = ownerId;
        this.logger = logger;
        this.itemRegistry = itemRegistry;
        this.patterns = new HashMap<>();
        this.patternItems = new HashMap<>();
        this.itemsByPattern = new HashMap<>();
    }
    
    @Nullable
    public Item getStencilItem (BannerPattern pattern) {
        
        return this.itemsByPattern.get(pattern);
    }
    
    public void initialize (IEventBus bus) {
        
        if (!this.patterns.isEmpty()) {
            
            this.logger.info("Registered {} banner patterns.", this.patterns.size());
        }
    }
    
    public BannerPattern registerGenericPattern (String id) {
        
        return this.registerPattern(id, false);
    }
    
    public BannerPattern registerItemPattern (String id) {
        
        return this.registerItemPattern(id, Rarity.UNCOMMON);
    }
    
    public BannerPattern registerItemPattern (String id, Rarity rarity) {
        
        final BannerPattern pattern = this.registerPattern(id, true);
        final Item item = this.itemRegistry.register(new BannerPatternItem(pattern, new Item.Properties().maxStackSize(1).rarity(rarity)), id);
        this.patternItems.put(new ResourceLocation(this.ownerId, id), item);
        this.itemsByPattern.put(pattern, item);
        return pattern;
    }
    
    public BannerPattern registerPattern (String id, boolean hasItem) {
        
        final ResourceLocation regId = new ResourceLocation(this.ownerId, id);
        final String snakeName = regId.toString().replace(':', '_');
        final BannerPattern pattern = BannerPattern.create(snakeName.toUpperCase(), snakeName, snakeName, hasItem);
        this.patterns.put(regId, pattern);
        return pattern;
    }
    
    public Collection<Item> getStencilItems () {
        
        return this.patternItems.values();
    }
    
    public Collection<BannerPattern> getPatterns () {
        
        return this.patterns.values();
    }
}