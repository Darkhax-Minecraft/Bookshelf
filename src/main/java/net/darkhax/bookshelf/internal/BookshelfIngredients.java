package net.darkhax.bookshelf.internal;

import net.darkhax.bookshelf.crafting.item.IngredientPredicate;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class BookshelfIngredients {
    
    public static final IIngredientSerializer<?> ANY_HOE = IngredientPredicate.create(BookshelfIngredients::isHoe);
    private static final Tag<Item> TAG_HOES = itemTag("hoes");
    private static final Tag<Item> TAG_TOOL_HOES = itemTag("tools/hoes");
    
    private static boolean isHoe (Item item) {
        
        return item instanceof HoeItem || TAG_HOES.contains(item) || TAG_TOOL_HOES.contains(item);
    }
    
    public static final IIngredientSerializer<?> ANY_PICKAXE = IngredientPredicate.create(BookshelfIngredients::isPickaxe);
    private static final Tag<Item> TAG_PICKAXES = itemTag("pickaxes");
    private static final Tag<Item> TAG_TOOL_PICKAXES = itemTag("tools/pickaxes");
    
    private static boolean isPickaxe (Item item) {
        
        return item instanceof PickaxeItem || TAG_PICKAXES.contains(item) || TAG_TOOL_PICKAXES.contains(item);
    }
    
    public static final IIngredientSerializer<?> ANY_AXE = IngredientPredicate.create(BookshelfIngredients::isAxe);
    private static final Tag<Item> TAG_AXES = itemTag("axes");
    private static final Tag<Item> TAG_TOOL_AXES = itemTag("tools/axes");
    
    private static boolean isAxe (Item item) {
        
        return item instanceof AxeItem || TAG_AXES.contains(item) || TAG_TOOL_AXES.contains(item);
    }
    
    public static final IIngredientSerializer<?> ANY_SHOVEL = IngredientPredicate.create(BookshelfIngredients::isShovel);
    private static final Tag<Item> TAG_SHOVELS = itemTag("shovels");
    private static final Tag<Item> TAG_TOOL_SHOVELS = itemTag("tools/shovels");
    
    private static boolean isShovel (Item item) {
        
        return item instanceof ShovelItem || TAG_SHOVELS.contains(item) || TAG_TOOL_SHOVELS.contains(item);
    }
    
    public static final IIngredientSerializer<?> ANY_SWORD = IngredientPredicate.create(BookshelfIngredients::isSword);
    private static final Tag<Item> TAG_SWORDS = itemTag("swords");
    private static final Tag<Item> TAG_TOOL_SWORDS = itemTag("tools/swords");
    
    private static boolean isSword (Item item) {
        
        return item instanceof SwordItem || TAG_SWORDS.contains(item) || TAG_TOOL_SWORDS.contains(item);
    }
    
    private static Tag<Item> itemTag (String path) {
        
        return new ItemTags.Wrapper(new ResourceLocation("forge", path));
    }
}