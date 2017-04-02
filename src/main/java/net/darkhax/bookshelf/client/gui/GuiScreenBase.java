package net.darkhax.bookshelf.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.darkhax.bookshelf.lib.util.TextUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiScreenBase extends GuiScreen {

    /**
     * The assumed height of a line.
     */
    private static final int LINE_HEIGHT = 11;

    /**
     * The tick delay before the tooltip will display.
     */
    private long tooltipDelay = 900;

    /**
     * The maximum width of the tooltip.
     */
    private int tooltipMaxWidth = 45;

    /**
     * Offset of the tooltip to adjust for rendering.
     */
    private final int tooltipXOffset = 10;

    /**
     * Offset of the tooltip to adjust for rendering.
     */
    private final int tooltipYOffset = -2;

    /**
     * The time that the mouse has been over an area with a tooltip.
     */
    private long mouseoverTime = 0;

    /**
     * Tracker for time.
     */
    private long prevSystemTime = -1;

    /**
     * The length of the gui.
     */
    private final int xSize = 177;

    /**
     * The height of the gui.
     */
    private final int ySize = 222;

    /**
     * The starting X coordinate for the gui.
     */
    private int guiLeft;

    /**
     * The starting Y coordinate for the gui.
     */
    private int guiTop;

    @Override
    public void initGui () {

        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen (int mouseX, int mouseY, float f) {

        this.drawGuiContainerBackgroundLayer();
        super.drawScreen(mouseX, mouseY, f);
        this.drawTooltip(mouseX, mouseY);

        GL11.glTranslatef(this.guiLeft, this.guiTop, 0.0F);
        this.drawGuiContainerForegroundLayer();
    }

    /**
     * Used to handle the background layer of the GUI. Override to render the background.
     */
    protected abstract void drawGuiContainerBackgroundLayer ();

    /**
     * Used to handle the foreground layer of the GUI. Override to render the foreground.
     */
    protected abstract void drawGuiContainerForegroundLayer ();

    /**
     * Can be overridden to provide the specific tool tip for any given button.
     *
     * @param buttonId: The integer id for the button that is highlighted.
     */
    protected abstract String getTooltipForButton (int buttonId);

    /**
     * Can be overridden to provide tool tip strings for miscellaneous locations.
     *
     * @param mouseX The X position of the mouse on the screen.
     * @param mouseY The Y position of the mouse on the screen.
     */
    protected abstract String getTooltipForMisc (int mouseX, int mouseY);

    /**
     * Handles the rendering of the button tool tips. This is where we check for if a button is
     * highlighted, and if the button highlighted has a tool tip available. This is also how we
     * check for a delay time.
     *
     * @param mouseX The X position of the mouse on the screen.
     * @param mouseY The Y position of the mouse on the screen.
     */
    private void drawTooltip (int mouseX, int mouseY) {

        int mousedOverButtonId = -1;
        boolean isMiscLocation = false;

        // Loop to search for a correct button.
        for (int i = 0; i < this.buttonList.size(); i++) {

            final GuiButton button = this.buttonList.get(i);

            if (this.isMouseInArea(mouseX, mouseY, button.xPosition, button.yPosition, button.width, button.height)) {

                mousedOverButtonId = button.id;
                break;
            }
        }

        // Search if in a valid Misc location.
        final String miscTooltip = this.getTooltipForMisc(mouseX, mouseY);
        if (miscTooltip != null && !miscTooltip.isEmpty()) {
            isMiscLocation = true;
        }

        // If valid location, start the delay timer.
        if (mousedOverButtonId > -1 || isMiscLocation) {

            final long systemTime = System.currentTimeMillis();

            if (this.prevSystemTime > 0) {
                this.mouseoverTime += systemTime - this.prevSystemTime;
            }

            this.prevSystemTime = systemTime;
        }
        else {
            this.mouseoverTime = 0;
        }

        // Check if enough time has passed, to show a tool tip. If the delay variable is -1 or
        // less, this is disabled.
        if (this.mouseoverTime > this.tooltipDelay && this.tooltipDelay > -1) {

            // Gets the appropriate tool tip string.
            final String tooltip = mousedOverButtonId > -1 ? this.getTooltipForButton(mousedOverButtonId) : isMiscLocation ? miscTooltip : null;

            if (tooltip != null) {
                this.renderTooltip(mouseX, mouseY, tooltip);
            }
        }
    }

    /**
     * Renders the tool tip onto the screen in the default Minecraft look and feel. Uses the
     * position of the mouse, along with the offset to get the rendering x and y. Length of the
     * tool tip is dynamically generated using the length of the tool tip string, and the size
     * of the lines array which is the tool tip string split using the tooltipMaxWidth
     * variable.
     *
     * @param mouseX The X position of the mouse on the screen.
     * @param mouseY The Y position of the mouse on the screen.
     * @param tooltip A string containing the entire tool tip as one line.
     */
    public void renderTooltip (int mouseX, int mouseY, String tooltip) {

        final List<String> tooltipArray = TextUtils.wrapStringToList(tooltip, this.tooltipMaxWidth, false, new ArrayList<String>());

        final int tooltipWidth = this.getTooltipWidth(tooltipArray);
        final int tooltipHeight = this.getTooltipHeight(tooltipArray);

        int tooltipX = mouseX + this.tooltipXOffset;
        int tooltipY = mouseY + this.tooltipYOffset;

        if (tooltipX > this.width - tooltipWidth - 7) {
            tooltipX = this.width - tooltipWidth - 7;
        }

        if (tooltipY > this.height - tooltipHeight - 8) {
            tooltipY = this.height - tooltipHeight - 8;
        }

        // Inside the tool tip box.
        final int innerAlpha = -0xFEFFFF0;
        this.drawGradientRect(tooltipX, tooltipY - 1, tooltipX + tooltipWidth + 6, tooltipY, innerAlpha, innerAlpha);
        this.drawGradientRect(tooltipX, tooltipY + tooltipHeight + 6, tooltipX + tooltipWidth + 6, tooltipY + tooltipHeight + 7, innerAlpha, innerAlpha);
        this.drawGradientRect(tooltipX, tooltipY, tooltipX + tooltipWidth + 6, tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);
        this.drawGradientRect(tooltipX - 1, tooltipY, tooltipX, tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);
        this.drawGradientRect(tooltipX + tooltipWidth + 6, tooltipY, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);

        // The tool tip's padding.
        final int outerAlpha1 = 0x505000FF;
        final int outerAlpha2 = (outerAlpha1 & 0xFEFEFE) >> 1 | outerAlpha1 & -0x1000000;
        this.drawGradientRect(tooltipX, tooltipY + 1, tooltipX + 1, tooltipY + tooltipHeight + 6 - 1, outerAlpha1, outerAlpha2);
        this.drawGradientRect(tooltipX + tooltipWidth + 5, tooltipY + 1, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6 - 1, outerAlpha1, outerAlpha2);
        this.drawGradientRect(tooltipX, tooltipY, tooltipX + tooltipWidth + 3, tooltipY + 1, outerAlpha1, outerAlpha1);
        this.drawGradientRect(tooltipX, tooltipY + tooltipHeight + 5, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6, outerAlpha2, outerAlpha2);

        // The tool tip's text.
        int lineCount = 0;
        for (final String line : tooltipArray) {

            this.mc.fontRenderer.drawStringWithShadow(line, tooltipX + 2f, tooltipY + 2f + (float) lineCount * (float) LINE_HEIGHT, 0xFFFFFF);
            lineCount++;
        }
    }

    /**
     * Get's the width of a tool tip using the longest line width amongst all of the tool tip's
     * lines.
     *
     * @param tooltip A list containing all of the strings inside of a tool tip.
     */
    private int getTooltipWidth (List<String> tooltip) {

        int longestWidth = 0;
        for (final String line : tooltip) {

            final int width = this.mc.fontRenderer.getStringWidth(line);

            if (width > longestWidth) {
                longestWidth = width;
            }
        }

        return longestWidth;
    }

    /**
     * Gets the height of a tool tip using the the amount of lines within the tool tip.
     *
     * @param tooltips A List containing all of the lines for the tool tip.
     */
    private int getTooltipHeight (List<String> tooltips) {

        int tooltipHeight = this.mc.fontRenderer.FONT_HEIGHT - 2;

        if (tooltips.size() > 1) {
            tooltipHeight += (tooltips.size() - 1) * LINE_HEIGHT;
        }

        return tooltipHeight;
    }

    /**
     * A check to see if a given position is within a specified area. Used to check if the
     * mouse is within a specific area.
     *
     * @param mouseX The X position of the mouse.
     * @param mouseY The Y position of the mouse.
     * @param posX The X position of the area on the screen.
     * @param posY The Y position of the area on the screen.
     * @param width The width of the area. Goes out from the posX.
     * @param height The height of the area. Goes up from the posY.
     * @return Wheter or not the mouse is over an area.
     */
    public boolean isMouseInArea (int mouseX, int mouseY, int posX, int posY, int width, int height) {

        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }

    /**
     * Gets the amount of ticks the mouse should hover before the tootlip is displayed.
     *
     * @return The amount of ticks before the tooltip is displayed.
     */
    public long getTooltipDelay () {

        return this.tooltipDelay;
    }

    /**
     * Sets the amount of ticks required before the tooltip will display.
     *
     * @param tooltipDelay The amount of ticks before the tooltip will display.
     */
    public void setTooltipDelay (long tooltipDelay) {

        this.tooltipDelay = tooltipDelay;
    }

    /**
     * Gets the maximum width of the tooltip. Used for splitting length.
     *
     * @return The max tooltip length.
     */
    public int getTooltipMaxWidth () {

        return this.tooltipMaxWidth;
    }

    /**
     * Sets the maximum width of the tooltip.
     *
     * @param tooltipMaxWidth The new maximum width for the tooltip.
     */
    public void setTooltipMaxWidth (int tooltipMaxWidth) {

        this.tooltipMaxWidth = tooltipMaxWidth;
    }
}