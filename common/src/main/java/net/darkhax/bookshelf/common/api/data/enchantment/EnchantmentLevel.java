package net.darkhax.bookshelf.common.api.data.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.ToIntBiFunction;

/**
 * Calculates enchantment levels using different methods.
 */
public enum EnchantmentLevel {

    /**
     * Returns the highest level among all matching enchantments.
     */
    HIGHEST((tag, enchantments) -> {
        int level = 0;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(tag) && entry.getIntValue() > level) {
                level = entry.getIntValue();
            }
        }
        return level;
    }),

    /**
     * Returns the lowest level among all matching enchantments.
     */
    LOWEST((tag, enchantments) -> {
        int level = Integer.MAX_VALUE;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(tag) && entry.getIntValue() < level) {
                level = entry.getIntValue();
            }
        }
        return level;
    }),

    /**
     * Returns the level of the first matching enchantment.
     */
    FIRST((tag, enchantments) -> {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(tag)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }),

    /**
     * Returns the combined level of all matching enchantments.
     */
    CUMULATIVE((tag, enchantments) -> {
        int level = 0;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(tag)) {
                level += entry.getIntValue();
            }
        }
        return level;
    });

    private final ToIntBiFunction<TagKey<Enchantment>, ItemEnchantments> func;

    EnchantmentLevel(ToIntBiFunction<TagKey<Enchantment>, ItemEnchantments> func) {
        this.func = func;
    }

    /**
     * Gets the level of matching enchantments based on the calculation type.
     *
     * @param enchType A tag of the enchantments to match on.
     * @param stack    The item to test.
     * @return The level based on the calculation type.
     */
    public int get(TagKey<Enchantment> enchType, ItemStack stack) {
        return (!stack.isEmpty() && stack.isEnchanted()) ? this.func.applyAsInt(enchType, stack.getEnchantments()) : 0;
    }
}