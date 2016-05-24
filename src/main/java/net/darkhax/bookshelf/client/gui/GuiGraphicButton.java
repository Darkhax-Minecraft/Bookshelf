package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiGraphicButton extends GuiButton {
    
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
    public void drawButton (Minecraft mc, int posX, int posY) {
        
        super.drawButton(mc, posX, posY);
        mc.getTextureManager().bindTexture(this.buttonImage);
        drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0f, 0f, 20, 20, 20f, 20f);
    }
}