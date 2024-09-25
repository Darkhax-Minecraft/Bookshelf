package net.darkhax.bookshelf.common.api.annotation;

import net.darkhax.bookshelf.common.api.PhysicalSide;

/**
 * A visual indicator that a class, field, or method can only be accessed in certain environments. There is no special
 * magic or ASM behind this annotation, it is only a visual indicator to help navigate the code.
 */
public @interface OnlyFor {
    PhysicalSide value();
}