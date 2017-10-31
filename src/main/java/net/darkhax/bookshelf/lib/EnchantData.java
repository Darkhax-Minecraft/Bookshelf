/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;

/**
 * This class serves as a simple wrapper for net.minecraft.enchamtnet.EnchantmentData. The
 * vanilla implementation lacks a {@link #toString()} and {@link #equals(Object)} method, which
 * can be very tedious.
 */
public class EnchantData extends EnchantmentData {

    public EnchantData (EnchantmentData data) {

        this(data.enchantment, data.enchantmentLevel);
    }

    public EnchantData (Enchantment enchantmentObj, int enchLevel) {

        super(enchantmentObj, enchLevel);
    }

    @Override
    public String toString () {

        return "EnchantData [enchantment=" + this.enchantment + ", enchantmentLevel=" + this.enchantmentLevel + "]";
    }

    @Override
    public boolean equals (Object other) {

        if (!(other instanceof EnchantmentData)) {

            final EnchantmentData data = (EnchantmentData) other;
            return this.enchantment == data.enchantment && this.enchantmentLevel == data.enchantmentLevel;
        }

        return false;
    }
}