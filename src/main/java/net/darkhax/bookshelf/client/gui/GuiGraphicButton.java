/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.gui;

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
     * @param xPosition The X coordinate to position the button at.
     * @param yPosition The Y coordinate to position the button at.
     * @param texture The texture to use for the button. This should be a 20x20 image.
     * @param handler A handler for when the button is pressed.
     */
    public GuiGraphicButton(int xPosition, int yPosition, ResourceLocation texture, IPressable handler) {
        
        super(xPosition, yPosition, 20, 20, "", handler);
        this.buttonImage = texture;
    }
    
    @Override
    public void render (int mouseX, int mouseY, float partialTicks) {
        
        super.render(mouseX, mouseY, partialTicks);
        // TODO fix the rendering
        // Minecraft.getInstance().getTextureManager().bindTexture(this.buttonImage);
        // this.blit(p_blit_1_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_);
        // drawModalRectWithCustomSizedTexture(this.x, this.y, 0f, 0f, 20, 20, 20f, 20f);
    }
}