package net.darkhax.bookshelf.common.api.text.unit;

import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents various units that can be displayed in game.
 */
public enum Units implements IUnit {

    TICK("tick"),
    NANOSECOND("nanosecond"),
    MILLISECOND("millisecond"),
    SECOND("second"),
    MINUTE("minute"),
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year");

    private final ResourceLocation key;

    Units(String key) {
        this.key = Constants.id(key);
    }

    @Override
    public ResourceLocation unitKey() {
        return this.key;
    }
}