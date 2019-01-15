/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 * <p>
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public class GuiItemButton extends GuiButton {
    
    /**
     * The ItemStack that is drawn over the button. By default nothing is rendered. This stack
     * can be changed in the constructor.
     */
    private ItemStack renderStack = ItemStack.EMPTY;
    
    /**
     * Constructs a new graphic button. A graphic button is a 20x20 button that uses an image
     * rather than text.
     *
     * @param buttonID The ID to set for the button. This ID is specific to the GUI instance.
     * @param xPosition The X coordinate to position the button at.
     * @param yPosition The Y coordinate to position the button at.
     * @param texture The texture to use for the button. This should be a 20x20 image.
     */
    public GuiItemButton(int buttonID, int xPosition, int yPosition, ItemStack renderStack) {
        
        super(buttonID, xPosition, yPosition, 20, 20, "");
        this.renderStack = renderStack;
    }
    
    @Override
    public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, this.renderStack, this.x + 2, this.y + 2, "");
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.renderStack, this.x + 2, this.y + 2);
        
    }
}