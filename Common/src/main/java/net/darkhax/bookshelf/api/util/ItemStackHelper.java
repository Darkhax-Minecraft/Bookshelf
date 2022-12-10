package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.item.ItemStackBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Arrays;
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

        return tab.getDisplayItems().toArray(ItemStack[]::new);
    }

    public static boolean areStacksEquivalent(ItemStack first, ItemStack second) {

        return first.isEmpty() == second.isEmpty() && first.getCount() == second.getCount() && Objects.equals(first.getTag(), second.getTag());
    }

    public static void setLore(ItemStack stack, Component... lines) {

        final CompoundTag displayTag = stack.getOrCreateTagElement("display");
        final ListTag loreList = new ListTag();
        Arrays.stream(lines).forEach(line -> loreList.add(StringTag.valueOf(Component.Serializer.toJson(line))));
        displayTag.put("Lore", loreList);
    }

    public static void appendLore(ItemStack stack, Component... lines) {

        final CompoundTag displayTag = stack.getOrCreateTagElement("display");
        final ListTag loreList = displayTag.contains("Lore") ? displayTag.getList("Lore", Tag.TAG_STRING) : new ListTag();
        Arrays.stream(lines).forEach(line -> loreList.add(StringTag.valueOf(Component.Serializer.toJson(line))));
        displayTag.put("Lore", loreList);
    }
}