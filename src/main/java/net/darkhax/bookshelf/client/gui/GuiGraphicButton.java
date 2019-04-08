/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;

@OnlyIn(Dist.CLIENT)
public class GuiGraphicButton extends GuiButtonExt {
    
    /**
     * The image to draw on the button. By default this image is a random file that probably
     * doesn't exist. This texture can be changed in the constructor.
     */
    private ResourceLocation buttonImage = new ResourceLocation("textures/gui/widgets.png");
    
    /**
     * Constructs a new graphic button. A graphic button is a 20x20 button that uses an image
     * rather than text.
     *
     * @param buttonID The ID to set for the button. This ID is specific to the GUI instance.
     * @param xPosition The X coordinate to position the button at.
     * @param yPosition The Y coordinate to position the button at.
     * @param texture The texture to use for the button. This should be a 20x20 image.
     */
    public GuiGraphicButton(int buttonID, int xPosition, int yPosition, ResourceLocation texture) {
        
        super(buttonID, xPosition, yPosition, 20, 20, "");
        this.buttonImage = texture;
    }
    
    @Override
    public void render (int mouseX, int mouseY, float partialTicks) {
        
        super.render(mouseX, mouseY, partialTicks);
        Minecraft.getInstance().getTextureManager().bindTexture(this.buttonImage);
        drawModalRectWithCustomSizedTexture(this.x, this.y, 0f, 0f, 20, 20, 20f, 20f);
    }
}