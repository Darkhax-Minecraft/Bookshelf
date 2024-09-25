package net.darkhax.bookshelf.common.api.text.format;

import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IPropertyFormat {

    /**
     * A namespaced identifier that is used to derive localization keys for the format.
     *
     * @return The namespace ID for the format.
     */
    ResourceLocation formatKey();

    /**
     * Formats a property and value using the alignment.
     *
     * @param property The name of the property.
     * @param value    The value of the property.
     * @return A component that represents an aligned property and value.
     */
    default MutableComponent format(Component property, Component value) {
        return TextHelper.fromResourceLocation("format", null, this.formatKey(), property, value);
    }
}
