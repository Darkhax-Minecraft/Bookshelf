package net.darkhax.bookshelf.common.api.text.format;

import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.resources.ResourceLocation;

/**
 * Formats a property string using various separator patterns.
 */
public enum PropertyFormat implements IPropertyFormat {

    /**
     * The separator is aligned to the right. Example: "property: value".
     */

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

    private final ResourceLocation formatKey;

    PropertyFormat(String key) {
        this.formatKey = Constants.id(key);
    }

    @Override
    public ResourceLocation formatKey() {
        return this.formatKey;
    }
}
