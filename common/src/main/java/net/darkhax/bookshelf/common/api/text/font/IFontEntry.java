package net.darkhax.bookshelf.common.api.text.font;

import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IFontEntry {

    /**
     * Gets the ID of the font.
     *
     * @return The font ID.
     */
    ResourceLocation identifier();

    /**
     * Gets the localized name of the font.
     *
     * @return The localized name of the font.
     */
    default MutableComponent displayName() {
        return TextHelper.fromResourceLocation("font", null, this.identifier());
    }

    /**
     * Gets a description of the font.
     *
     * @return A description of the font.
     */
    default MutableComponent description() {
        return TextHelper.fromResourceLocation("font", "desc", this.identifier());
    }

    /**
     * Gets some text that can be used as a preview for the font in-game.
     *
     * @return The preview text for the font.
     */
    default MutableComponent preview() {
        return TextHelper.fromResourceLocation("font", "preview", this.identifier());
    }
}