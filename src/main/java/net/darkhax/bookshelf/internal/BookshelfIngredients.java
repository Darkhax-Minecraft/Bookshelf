package net.darkhax.bookshelf.internal;

import net.darkhax.bookshelf.crafting.item.IngredientPredicate;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class BookshelfIngredients {
    
    public static final IIngredientSerializer<?> ANY_HOE = IngredientPredicate.create(BookshelfIngredients::isHoe);

    private static boolean isHoe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof HoeItem;
    }
    
    public static final IIngredientSerializer<?> ANY_PICKAXE = IngredientPredicate.create(BookshelfIngredients::isPickaxe);

    private static boolean isPickaxe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof PickaxeItem;
    }
    
    public static final IIngredientSerializer<?> ANY_AXE = IngredientPredicate.create(BookshelfIngredients::isAxe);

    private static boolean isAxe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof AxeItem;
    }
    
    public static final IIngredientSerializer<?> ANY_SHOVEL = IngredientPredicate.create(BookshelfIngredients::isShovel);

    private static boolean isShovel (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof ShovelItem;
    }
    
    public static final IIngredientSerializer<?> ANY_SWORD = IngredientPredicate.create(BookshelfIngredients::isSword);

    private static boolean isSword (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof SwordItem;
    }
    
    private static ITag<Item> itemTag (String path) {
        return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
    }
}