package net.darkhax.bookshelf.common.api.text.font;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * Constant references to all built-in Minecraft fonts. These fonts are included in the original game and are not
 * bundled or provided by Bookshelf.
 */
public enum BuiltinFonts implements IFontEntry {

    /**
     * The default pixel font that appears in the game.
     */
    DEFAULT("default"),

    /**
     * A magical font based on the Standard Galactic Alphabet. This font is used by the enchanting table and is
     * associated with the enchantment system.
     */
    ALT("alt"),

    /**
     * A rune font that is used by the Illagers in Minecraft Dungeons.
     */
    ILLAGER("illageralt"),

    /**
     * A plain font that is not stylized.
     */
    UNIFORM("uniform");

    public static final Set<ResourceLocation> FONT_IDS = Set.of(DEFAULT.fontId, ALT.fontId, ILLAGER.fontId, UNIFORM.fontId);
    
    private final ResourceLocation fontId;

    BuiltinFonts(String path) {
        this(ResourceLocation.tryBuild("minecraft", path));
    }

    BuiltinFonts(ResourceLocation fontID) {
        this.fontId = fontID;
    }

    @Override
    public ResourceLocation identifier() {
        return this.fontId;
    }
}