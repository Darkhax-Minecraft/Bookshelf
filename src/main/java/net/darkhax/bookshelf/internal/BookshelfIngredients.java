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
    private static final ITag<Item> TAG_HOES = itemTag("hoes");
    private static final ITag<Item> TAG_TOOL_HOES = itemTag("tools/hoes");
    
    private static boolean isHoe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof HoeItem || TAG_HOES.func_230235_a_(item) || TAG_TOOL_HOES.func_230235_a_(item);
    }
    
    public static final IIngredientSerializer<?> ANY_PICKAXE = IngredientPredicate.create(BookshelfIngredients::isPickaxe);
    private static final ITag<Item> TAG_PICKAXES = itemTag("pickaxes");
    private static final ITag<Item> TAG_TOOL_PICKAXES = itemTag("tools/pickaxes");
    
    private static boolean isPickaxe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof PickaxeItem || TAG_PICKAXES.func_230235_a_(item) || TAG_TOOL_PICKAXES.func_230235_a_(item);
    }
    
    public static final IIngredientSerializer<?> ANY_AXE = IngredientPredicate.create(BookshelfIngredients::isAxe);
    private static final ITag<Item> TAG_AXES = itemTag("axes");
    private static final ITag<Item> TAG_TOOL_AXES = itemTag("tools/axes");
    
    private static boolean isAxe (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof AxeItem || TAG_AXES.func_230235_a_(item) || TAG_TOOL_AXES.func_230235_a_(item);
    }
    
    public static final IIngredientSerializer<?> ANY_SHOVEL = IngredientPredicate.create(BookshelfIngredients::isShovel);
    private static final ITag<Item> TAG_SHOVELS = itemTag("shovels");
    private static final ITag<Item> TAG_TOOL_SHOVELS = itemTag("tools/shovels");
    
    private static boolean isShovel (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof ShovelItem || TAG_SHOVELS.func_230235_a_(item) || TAG_TOOL_SHOVELS.func_230235_a_(item);
    }
    
    public static final IIngredientSerializer<?> ANY_SWORD = IngredientPredicate.create(BookshelfIngredients::isSword);
    private static final ITag<Item> TAG_SWORDS = itemTag("swords");
    private static final ITag<Item> TAG_TOOL_SWORDS = itemTag("tools/swords");
    
    private static boolean isSword (Item item) {
        // TODO MCP-name: func_230235_a_ -> contains
        return item instanceof SwordItem || TAG_SWORDS.func_230235_a_(item) || TAG_TOOL_SWORDS.func_230235_a_(item);
    }
    
    private static ITag<Item> itemTag (String path) {
        return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
    }
}