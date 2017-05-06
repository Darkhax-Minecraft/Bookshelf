/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Object for representing enchantments that have a level component to them.
 */
public class EnchantmentObject {

    /**
     * The enchantment held by the Enchantment Object. This can not be changed.
     */
    private final Enchantment enchantment;

    /**
     * The level of the enchantment.
     */
    private int level;

    /**
     * Constructs a new EnchantmentObject from the base requirements.
     *
     * @param enchantment The enchantment to use.
     * @param level The level of the enchantment.
     */
    public EnchantmentObject (Enchantment enchantment, int level) {

        this.enchantment = enchantment;
        this.level = level;
    }

    /**
     * Constructs a new EnchantmentObject from a byte buffer. The first piece of data read is
     * expected to be a UTF8 string that represents an enchantment ID. The second piece of
     * expected thata is an integer which represents the level of the enchantment.
     *
     * @param buf The buffer of bytes to read from.
     */
    public EnchantmentObject (ByteBuf buf) {

        this.enchantment = Enchantment.getEnchantmentByLocation(ByteBufUtils.readUTF8String(buf));
        this.level = buf.readInt();
    }

    /**
     * Gets the enchantment being represented.
     *
     * @return The enchantment being represented.
     */
    public Enchantment getEnchantment () {

        return this.enchantment;
    }

    /**
     * Gets the level of the enchantment effect.
     *
     * @return The level of the effect.
     */
    public int getLevel () {

        return this.level;
    }

    /**
     * Sets the level of the enchantment effect.
     *
     * @param level The new level for the effect.
     */
    public void setLevel (int level) {

        this.level = level;
    }

    /**
     * Writes the stored data to a byte buffer. Data is written in the same way that the
     * ByteBuf constructer expects.
     *
     * @param buf The buffer of bytes to write to.
     */
    public void writeToBuffer (ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, this.enchantment.getRegistryName().toString());
        buf.writeInt(this.level);
    }
}