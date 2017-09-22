/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.data;

/**
 * Represents the different attribute modifiers in vanilla.
 */
public enum AttributeOperation {

    /**
     * Adds an amount directly to the base sum. For example if base is 2 and the
     * value is 3, the result would be 5. (2 + 3 = 5)
     */
    ADDITIVE,

    /**
     * Multiplies the base value with the sum of the specified amount plus one.
     * If multiple modifiers of this type are together, their values are added
     * together. For example if the base is 2 and the value is 3, the result
     * would be 8. (2 * (1 + 3) = 8) If another modifier of the same type is
     * added but it had a value of 4 the new sum would be 16. (2 * (1 + 3 + 4) =
     * 15)
     */
    MULTIPLY,

    /**
     * Similarly to {@link #MULTIPLY} the base value is multiplied by the sum of
     * the specified amount plus one, however each type is multiplied
     * separately. For example, if the base value is 3 and you had two of these
     * modifiers, one with an amount of 2 and the other with an amount of 4 the
     * sum would be 45. (3 * (1 + 2) * (1 + 4) = 45
     */
    MULTIPLY_SEPERATE;
}