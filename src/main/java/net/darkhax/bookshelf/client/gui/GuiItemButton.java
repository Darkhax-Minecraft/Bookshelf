/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 * <p>
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;

@OnlyIn(Dist.CLIENT)
public class GuiItemButton extends GuiButtonExt {
    
    /**
     * The ItemStack that is drawn over the button. By default nothing is rendered. This stack
     * can be changed in the constructor.
     */
    private ItemStack renderStack = ItemStack.EMPTY;
    
    /**
     * Constructs a new graphic button. A graphic button is a 20x20 button that uses an image
     * rather than text.
     *
     * @param xPosition The X coordinate to position the button at.
     * @param yPosition The Y coordinate to position the button at.
     * @param renderStack The itemstack to render.
     * @param handler The pressable effect handler. Used to add logic to the button.
     */
    public GuiItemButton(int xPosition, int yPosition, ItemStack renderStack, IPressable handler) {
        
        super(xPosition, yPosition, 20, 20, "", handler);
        this.renderStack = renderStack;
    }
    
    @Override
    public void render (int mouseX, int mouseY, float partialTicks) {
        
        super.render(mouseX, mouseY, partialTicks);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, this.renderStack, this.x + 2, this.y + 2, "");
        minecraft.getItemRenderer().renderItemAndEffectIntoGUI(this.renderStack, this.x + 2, this.y + 2);
        
    }
}