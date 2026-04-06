package net.darkhax.bookshelf.common.api.text.unit;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.resources.Identifier;

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

    private final Identifier key;

    Units(String key) {
        this.key = BookshelfMod.id(key);
    }

    @Override
    public Identifier unitKey() {
        return this.key;
    }
}