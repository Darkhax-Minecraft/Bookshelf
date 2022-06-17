package net.darkhax.bookshelf.api.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class RenderHelper {
    
    public static void renderLinesWrapped (PoseStack matrix, int x, int y, FormattedText text, int textWidth) {

        final Font font = Minecraft.getInstance().font;
        renderLinesWrapped(matrix, font, x, y, font.lineHeight, 0, text, textWidth);
    }

    public static void renderLinesWrapped (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, FormattedText text, int textWidth) {

        renderLinesWrapped(matrix, fontRenderer, x, y, spacing, defaultColor, fontRenderer.split(text, textWidth));
    }

    public static void renderLinesWrapped (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, List<FormattedCharSequence> lines) {

        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {

            final FormattedCharSequence lineFragment = lines.get(lineNum);
            fontRenderer.draw(matrix, lineFragment, x, y + lineNum * spacing, defaultColor);
        }
    }

    public static int renderLinesReversed (PoseStack matrix, int x, int y, FormattedText text, int textWidth) {

        final Font font = Minecraft.getInstance().font;
        return renderLinesReversed(matrix, font, x, y, font.lineHeight, 0xffffff, text, textWidth);
    }

    public static int renderLinesReversed (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, FormattedText text, int textWidth) {

        return renderLinesReversed(matrix, fontRenderer, x, y, spacing, defaultColor, fontRenderer.split(text, textWidth));
    }

    public static int renderLinesReversed (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, List<FormattedCharSequence> lines) {

        final int lineCount = lines.size();
        for (int lineNum = lineCount - 1; lineNum >= 0; lineNum--) {

            final FormattedCharSequence lineFragment = lines.get(lineCount - 1 - lineNum);
            fontRenderer.draw(matrix, lineFragment, x, y - (lineNum + 1) * (spacing + 1), defaultColor);
        }

        return lineCount * (spacing + 1);
    }
}
