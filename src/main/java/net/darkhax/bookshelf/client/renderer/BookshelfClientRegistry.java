package net.darkhax.bookshelf.client.renderer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BookshelfClientRegistry {

    private static Map<Item, IItemRenderer> itemRenderList = new HashMap<>();

    public static void addRenderItem(Item item, IItemRenderer itemRenderer) {

        itemRenderList.put(item, itemRenderer);
    }

    public static void renderPreItem(ItemStack stack) {

        renderItem(stack, EnumState.PRE_RENDER);
    }

    public static void renderItem(ItemStack stack) {

        renderItem(stack, EnumState.RENDER);
    }

    public static void renderPostItem(ItemStack stack) {

        renderItem(stack, EnumState.POST_RENDER);
    }

    public static void renderItem(ItemStack stack, EnumState state) {

        IItemRenderer itemRender = itemRenderList.get(stack.getItem());

        if (itemRender != null)
            itemRender.renderItemstack(stack, state);
    }

    public enum EnumState {
        PRE_RENDER,
        RENDER,
        POST_RENDER
    }
}
