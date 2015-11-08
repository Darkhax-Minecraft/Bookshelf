package net.darkhax.bookshelf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiGraphicButton extends GuiButton {

    ResourceLocation buttonImage = new ResourceLocation("textures/gui/widgets.png");

    public GuiGraphicButton (int buttonID, int xPosition, int yPosition, String resourcePath) {

        super(buttonID, xPosition, yPosition, 20, 20, "");
        this.buttonImage = new ResourceLocation(resourcePath + ".png");
    }

    @Override
    public void drawButton (Minecraft mc, int posX, int posY) {

        super.drawButton(mc, posX, posY);

        if (this.visible) {

            mc.getTextureManager().bindTexture(buttonImage);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTextureModalRectSize(xPosition, yPosition, 0, 0, 20, 20, 20);
        }
    }

    public void drawTextureModalRectSize (int x, int y, int textureX, int textureY, int width, int height, float size) {

        float f = 1 / size;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) this.zLevel, (double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) this.zLevel, (double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) this.zLevel, (double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f));
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel, (double) ((float) (textureX) * f), (double) ((float) (textureY + 0) * f));
        tessellator.draw();
    }

    /**
     * A special method for rendering a square image onto the screen. This method is
     * specifically used by the graphical button class to render an overlay image for the
     * buttons, which are set to 20x20 by default. The purpose of this method is to bypass the
     * arbitrary rule of using 256x images for gui elements, and helps us bypass the need for a
     * sprite sheet because #DownWithSpriteSheets
     *
     * @param x:         The X position to draw the image at.
     * @param y:         The Y position to draw the image at.
     * @param imageSize: The size of the image which is currently being rendered. Since this is
     *                   a square, width and height are the save.
     */
    public void drawTextureSquare (int x, int y, int imageSize) {

        float scaleRatio = 1 / imageSize;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) this.zLevel, (double) 0, height * scaleRatio);
        tessellator.addVertexWithUV((double) (x + imageSize), (double) (y + height), (double) this.zLevel, imageSize * scaleRatio, height * scaleRatio);
        tessellator.addVertexWithUV((double) (x + imageSize), (double) (y + 0), (double) this.zLevel, imageSize * scaleRatio, 0);
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel, 0, 0);
        tessellator.draw();
    }
}