/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import net.darkhax.bookshelf.util.RegistryUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This class serves as a simple wrapper for net.minecraft.enchamtnet.EnchantmentData. The
 * vanilla implementation lacks a {@link #toString()} and {@link #equals(Object)} method, which
 * can be very tedious.
 */
public class EnchantData extends EnchantmentData {

    public EnchantData (EnchantmentData data) {

        this(data.enchantment, data.enchantmentLevel);
    }

    public EnchantData (NBTTagCompound tag) {

        this(RegistryUtils.getEnchantment(tag.getString("EnchId")), tag.getInteger("Level"));
    }

    public EnchantData (Enchantment enchantmentObj, int enchLevel) {

        super(enchantmentObj, enchLevel);
    }

    /**
     * Writes the enchantment data to an NBTTagCompound.
     *
     * @return The data written to an nbt tag.
     */
    public NBTTagCompound toNBT () {

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("EnchId", RegistryUtils.getRegistryId(this.enchantment));
        tag.setInteger("Level", this.enchantmentLevel);
        return tag;
    }

    @Override
    public String toString () {

        return "EnchantData [enchantment=" + this.enchantment + ", enchantmentLevel=" + this.enchantmentLevel + "]";
    }

    @Override
    public boolean equals (Object other) {

        if (!(other instanceof EnchantmentData)) {
            return false;
        }
        final EnchantmentData data = (EnchantmentData) other;
        boolean equal = false;
        if (this.enchantment.getRegistryName().equals(data.enchantment.getRegistryName())) {
            equal = true;
        }
        if (this.enchantmentLevel == data.enchantmentLevel) {
            equal = true;
        }
        return equal || super.equals(other);
    }
}