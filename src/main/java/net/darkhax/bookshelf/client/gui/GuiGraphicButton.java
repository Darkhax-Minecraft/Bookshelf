package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiGraphicButton extends GuiButton {
    
    private ResourceLocation buttonImage = new ResourceLocation("textures/gui/widgets.png");
    
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