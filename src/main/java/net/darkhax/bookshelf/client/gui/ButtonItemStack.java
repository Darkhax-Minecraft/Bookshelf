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
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * A standard button that renders an ItemStack inside of it.
 */
@OnlyIn(Dist.CLIENT)
public class ButtonItemStack extends ExtendedButton {
    
    /**
     * The item stack to render onto the button. If empty no
     */
    private ItemStack renderStack = ItemStack.EMPTY;
    
    public ButtonItemStack(int x, int y, ItemStack renderStack, IPressable handler) {
        
        this(x, y, 20, 20, "", renderStack, handler);
    }
    
    public ButtonItemStack(int x, int y, int width, int height, String text, ItemStack renderStack, IPressable handler) {
        
        super(x, y, width, height, text, handler);
        this.setRenderStack(renderStack);
    }
    
    /**
     * Gets the stack that is rendered inside the button.
     * 
     * @return The stack rendered in the button.
     */
    public ItemStack getRenderStack () {
        
        return this.renderStack;
    }
    
    /**
     * Sets the stack to render in the button.
     * 
     * @param stack The new stack to render inside the button.
     */
    public void setRenderStack (ItemStack stack) {
        
        this.renderStack = stack;
    }
    
    @Override
    public void render (int mouseX, int mouseY, float partialTicks) {
        
        super.render(mouseX, mouseY, partialTicks);
        
        final ItemStack stack = this.getRenderStack();
        
        if (!stack.isEmpty()) {
            
            final Minecraft minecraft = Minecraft.getInstance();
            minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, stack, this.x + 2, this.y + 2, "");
            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(stack, this.x + 2, this.y + 2);
        }
    }
}