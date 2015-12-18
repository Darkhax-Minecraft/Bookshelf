package net.darkhax.bookshelf.client.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.*;

@SideOnly(Side.CLIENT)
public class DescriptionHandler implements IUsageHandler, ICraftingHandler {
    
    /**
     * A static reference to the game's FontRenderer.
     */
    private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    
    /**
     * The ItemStack being displayed in the DescriptionHandler.
     */
    private ItemStack displayStack;
    
    /**
     * The base color for the text for description text.
     */
    private int textColor = 5592405;
    
    /**
     * The base width of the GUI. The standard NEI gui has a width of 166.
     */
    private int displayWidth = 166;
    
    /**
     * An array containing the text for every single page being rendered.
     */
    private String[] pages;
    
    /**
     * Creates a DescriptionHandler instance for an ItemStack. If the ItemStack has information
     * about it in the Bookshelf Registry, it will be displayed here.
     * 
     * @param stack: The ItemStack used to pull information from. Not required, but
     *            recommended.
     */
    public DescriptionHandler(ItemStack stack) {
        
        if (BookshelfRegistry.doesStackHaveDescription(stack)) {
            
            ArrayList<String> pageList = new ArrayList<String>();
            
            for (String descriptionKey : BookshelfRegistry.getDescriptionKeys(stack))
                pageList.addAll(createPages(StatCollector.translateToLocal(descriptionKey)));
                
            if (!pageList.isEmpty())
                pages = pageList.toArray(new String[pageList.size()]);
                
            this.displayStack = stack.copy();
            this.displayStack.stackSize = 1;
        }
    }
    
    /**
     * Splits a description up into it's separate pages. Each page has 10 lines of text.
     * 
     * @param description: The text to populate the pages with.
     * @return List<String>: A List containing entries for the text to display on every piece
     *         of text.
     */
    private List<String> createPages (String description) {
        
        ArrayList<String> list = new ArrayList();
        List lines = fontRenderer.listFormattedStringToWidth(description, displayWidth - 7);
        
        if (lines.size() < 10)
            list.add(description);
            
        else {
            
            String pageText = "";
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                
                pageText += lines.get(lineIndex) + " ";
                
                if ((lineIndex > 0) && (lineIndex % 10 == 0)) {
                    
                    list.add(pageText.trim());
                    pageText = "";
                }
            }
            
            pageText = pageText.trim();
            
            if (!pageText.isEmpty())
                list.add(pageText);
        }
        
        return list;
    }
    
    @Override
    public void drawBackground (int recipe) {
    
    }
    
    @Override
    public void drawForeground (int recipe) {
        
        List<String> description = fontRenderer.listFormattedStringToWidth(StatCollector.translateToLocal(pages[recipe]), displayWidth - 7);
        
        int lineNumber = 0;
        
        for (String line : description) {
            
            GuiDraw.drawString(line, displayWidth / 2 - GuiDraw.getStringWidth(line) / 2, 18 + (lineNumber * 8), textColor, false);
            lineNumber++;
        }
    }
    
    @Override
    public List<PositionedStack> getIngredientStacks (int recipe) {
        
        return new ArrayList();
    }
    
    @Override
    public List<PositionedStack> getOtherStacks (int recipe) {
        
        return new ArrayList();
    }
    
    @Override
    public IOverlayHandler getOverlayHandler (GuiContainer gui, int recipe) {
        
        return null;
    }
    
    @Override
    public IRecipeOverlayRenderer getOverlayRenderer (GuiContainer gui, int recipe) {
        
        return null;
    }
    
    @Override
    public String getRecipeName () {
        
        return StatCollector.translateToLocal("gui.bookshelf.nei.documentation");
    }
    
    @Override
    public PositionedStack getResultStack (int recipe) {
        
        return new PositionedStack(displayStack, (displayWidth / 2) - 9, 0, false);
    }
    
    @Override
    public List<String> handleItemTooltip (GuiRecipe gui, ItemStack stack, List<String> tooltip, int recipe) {
        
        return tooltip;
    }
    
    @Override
    public List<String> handleTooltip (GuiRecipe gui, List<String> tooltip, int recipe) {
        
        return tooltip;
    }
    
    @Override
    public boolean hasOverlay (GuiContainer gui, Container container, int recipe) {
        
        return false;
    }
    
    @Override
    public boolean keyTyped (GuiRecipe gui, char key, int code, int recipe) {
        
        return false;
    }
    
    @Override
    public boolean mouseClicked (GuiRecipe gui, int button, int recipe) {
        
        return false;
    }
    
    @Override
    public int numRecipes () {
        
        return (ItemStackUtils.isValidStack(displayStack) && pages != null) ? pages.length : 0;
    }
    
    @Override
    public void onUpdate () {
    
    }
    
    @Override
    public int recipiesPerPage () {
        
        return 1;
    }
    
    @Override
    public IUsageHandler getUsageHandler (String inputID, Object... ingredients) {
        
        if (!inputID.equals("item"))
            return this;
            
        for (Object ingredient : ingredients)
            if (ingredient instanceof ItemStack && ItemStackUtils.isValidStack((ItemStack) ingredient) && BookshelfRegistry.doesStackHaveDescription((ItemStack) ingredient))
                return new DescriptionHandler((ItemStack) ingredient);
                
        return this;
    }
    
    @Override
    public ICraftingHandler getRecipeHandler (String outputID, Object... results) {
        
        if (!outputID.equals("item"))
            return this;
            
        for (Object result : results)
            if (result instanceof ItemStack && ItemStackUtils.isValidStack((ItemStack) result) && BookshelfRegistry.doesStackHaveDescription((ItemStack) result))
                return new DescriptionHandler((ItemStack) result);
                
        return this;
    }
}