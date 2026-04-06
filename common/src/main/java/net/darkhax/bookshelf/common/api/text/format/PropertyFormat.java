package net.darkhax.bookshelf.common.api.text.format;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.resources.Identifier;

/**
 * Formats a property string using various separator patterns.
 */
public enum PropertyFormat implements IPropertyFormat {

    /**
     * Formats a property with the separator aligned to the right. Example: "property: value".
     */
    RIGHT("right"),

    /**
     * Formats a property with the separator aligned in the center. Example: "property : value".
     */
    CENTER("center"),

    /**
     * Formats a property with the separator aligned to the left. Example: "property :value".
     */
    LEFT("left"),

    /**
     * Formats a property using a single space as the separator. Example: "property value".
     */
    SPACED("spaced"),

    /**
     * Formats a property without a separator. Example: "propertyvalue".
     */
    NONE("none");

    private final Identifier formatKey;

    PropertyFormat(String key) {
        this.formatKey = BookshelfMod.id(key);
    }

    @Override
    public Identifier formatKey() {
        return this.formatKey;
    }
}
