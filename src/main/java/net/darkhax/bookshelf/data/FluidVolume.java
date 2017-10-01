/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.data;

/**
 * Defines some of the common fluid volume values. Based on values from Tinkers Construct, and
 * many other mods which deal with fluid values.
 */
public enum FluidVolume {

    NUGGET(16),
    INGOT(144),
    BOTTLE(333),
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
    FluidVolume (int amount) {

        this.amount = amount;
    }
}