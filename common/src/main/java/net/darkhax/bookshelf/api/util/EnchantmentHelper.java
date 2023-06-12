package net.darkhax.bookshelf.api.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.BiConsumer;

public class EnchantmentHelper {

    public static void processEnchantments(ItemStack stack, BiConsumer<Enchantment, Integer> processor) {

        if (!stack.isEmpty() && stack.isEnchanted()) {

            final ListTag tag = stack.getEnchantmentTags();

            for (int index = 0; index < tag.size(); index++) {

                final CompoundTag enchTag = tag.getCompound(index);
                BuiltInRegistries.ENCHANTMENT.getOptional(net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId(enchTag)).ifPresent(ench -> {
                    processor.accept(ench, net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel(enchTag));
                });
            }
        }
    }
}
