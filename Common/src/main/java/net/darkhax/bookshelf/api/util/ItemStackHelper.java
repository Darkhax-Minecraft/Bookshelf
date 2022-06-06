package net.darkhax.bookshelf.api.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Objects;

/**
 * This class contains static helper methods for working with ItemStacks.
 */
public final class ItemStackHelper {

    /**
     * Calculates the attack damage for an ItemStack when used to attack an unspecified mob. This will include bonus
     * damage sources such as modifiers and enchantments.
     *
     * @param stack The ItemStack to calculate the damage value of.
     * @return The attack damage for an ItemStack with bonus damage modifiers applied.
     */
    public static double getAttackDamage(ItemStack stack) {

        return getAttackDamage(stack, MobType.UNDEFINED);
    }

    /**
     * Calculates the attack damage for an ItemStack when used to attack a specific entity. This will include bonus
     * damage sources such as attribute modifiers and enchantments.
     *
     * @param stack  The ItemStack to calculate the damage value of.
     * @param target The target entity being attacked.
     * @return The attack damage for an ItemStack with bonus damage modifiers applied.
     */
    public static double getAttackDamage(ItemStack stack, Entity target) {

        return getAttackDamage(stack, (target instanceof LivingEntity living) ? living.getMobType() : MobType.UNDEFINED);
    }

    /**
     * Calculates the attack damage for an ItemStack after accounting for bonus damage modifiers such as attribute
     * modifiers and enchantments.
     *
     * @param stack      The ItemStack to calculate the damage value of.
     * @param targetType The type of mob being attacked. Used for some enchantment calculations.
     * @return The attack damage for an ItemStack with bonus damage modifiers applied.
     */
    public static double getAttackDamage(ItemStack stack, MobType targetType) {

        final double damage = AttributeHelper.getAttackDamage(stack);
        final double bonusEnchantmentDamage = EnchantmentHelper.getDamageBonus(stack, targetType);

        return damage + bonusEnchantmentDamage;
    }

    public static ItemStack[] getTabItems(CreativeModeTab tab) {

        final NonNullList<ItemStack> stacks = NonNullList.create();
        CreativeModeTab.TAB_COMBAT.fillItemList(stacks);
        return stacks.toArray(ItemStack[]::new);
    }

    public static boolean areStacksEquivalent(ItemStack first, ItemStack second) {

        return first.isEmpty() == second.isEmpty() && first.getCount() == second.getCount() && Objects.equals(first.getTag(), second.getTag());
    }
}