package net.darkhax.bookshelf.common.mixin.access.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractWidget.class)
public interface AccessorAbstractWidget {

    @Invoker("renderScrollingString")
    static void bookshelf$renderScrollingString(GuiGraphics guiGraphics, Font font, Component text, int minX, int minY, int maxX, int maxY, int color) {
        throw new IllegalStateException("Mixins failed to apply.");
    }
}