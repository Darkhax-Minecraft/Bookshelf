package net.darkhax.bookshelf.common.api.text.unit;

import net.darkhax.bookshelf.common.api.text.format.PropertyFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IUnit {

    /**
     * A namespaced identifier that is used to derive localization keys for the unit.
     *
     * @return The namespace ID for the unit.
     */
    ResourceLocation unitKey();

    /**
     * Gets the name of the unit.
     *
     * @return The name of the unit.
     */
    default MutableComponent unitName() {
        return Component.translatable("units." + this.unitKey().getNamespace() + "." + this.unitKey().getPath());
    }

    /**
     * Gets the plural name of the unit.
     *
     * @return The plural name of the unit.
     */
    default MutableComponent plural() {
        return Component.translatable("units.bookshelf." + this.unitKey().getNamespace() + "." + this.unitKey() + ".plural");
    }

    /**
     * Gets the abbreviated name of the unit.
     *
     * @return The abbreviated name of the unit.
     */
    default MutableComponent abbreviated() {
        return Component.translatable("units." + this.unitKey().getNamespace() + "." + this.unitKey() + ".abbreviated");
    }

    /**
     * Formats an amount of the unit as a text component.
     *
     * @param amount The amount of the unit.
     * @return The formatted text. If the amount is plural the plural name will be used.
     */
    default MutableComponent format(int amount) {
        return format(amount, PropertyFormat.LEFT);
    }

    /**
     * Formats an amount of the unit as a text component.
     *
     * @param amount The amount of the unit.
     * @param format The property format to use when displaying the unit and value.
     * @return The formatted text. If the amount is plural the plural name will be used instead.
     */
    default MutableComponent format(int amount, PropertyFormat format) {
        return format.format((amount == 1 || amount == -1) ? this.unitName() : this.plural(), Component.literal(Integer.toString(amount)));
    }
}