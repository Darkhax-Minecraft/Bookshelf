/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

/**
 * Represents common mb amounts.
 */
public enum Milibucket {

    NUGGET(16),
    INGOT(144),
    Bottle(333),
    BUCKET(1000),
    BLOCK(1296);

    /**
     * The amount of mb which make up this amount.
     */
    public int amount;

    /**
     * A simple enumeration used to list how many milibuckets is in a given measurement.
     *
     * @param amount The amount of milibuckets in the measurement.
     */
    Milibucket (int amount) {

        this.amount = amount;
    }
}
