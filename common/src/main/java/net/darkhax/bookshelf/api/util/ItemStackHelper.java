package net.darkhax.bookshelf.api.util;

import com.google.common.math.DoubleMath;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Arrays;
import java.util.Collection;
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

        return areStacksEquivalent(first, second, false);
    }

    public static boolean areStacksContentsEquivalent(ItemStack first, ItemStack second) {

        return areStacksEquivalent(first, second, true);
    }

    public static boolean areStacksEquivalent(ItemStack first, ItemStack second, boolean ignoreTags) {

        return ((first == null && second == null) || first.isEmpty() == second.isEmpty()) || (first.getCount() == second.getCount() && first.getItem() == second.getItem() && (ignoreTags || areTagsEquivalent(first.getTag(), second.getTag())));
    }

    public static boolean areTagsEquivalent(Tag a, Tag b) {

        if (!Objects.equals(a, b)) {

            if (a instanceof NumericTag numericA && b instanceof NumericTag numericB) {

                return areNumericTagsEquivalent(numericA, numericB);
            }

            else if (a instanceof CollectionTag<?> collectionA && b instanceof CollectionTag<?> collectionB) {

                return areCollectionTagsEquivalent(collectionA, collectionB);
            }

            else if (a instanceof CompoundTag compoundA && b instanceof CompoundTag compoundB) {

                return areCompoundTagsEquivalent(compoundA, compoundB);
            }
        }

        return true;
    }

    public static boolean areCompoundTagsEquivalent(CompoundTag a, CompoundTag b) {

        if (a.size() != b.size()) {

            return false;
        }

        for (String key : a.getAllKeys()) {

            if (!areTagsEquivalent(a.getCompound(key), b.getCompound(key))) {

                return false;
            }
        }

        return true;
    }

    public static boolean areCollectionTagsEquivalent(CollectionTag<?> a, CollectionTag<?> b) {

        if (a.size() != b.size()) {

            return false;
        }

        for (int index = 0; index < a.size(); index++) {

            if (!areTagsEquivalent(a.get(index), b.get(index))) {

                return false;
            }
        }

        return true;
    }

    public static boolean areNumericTagsEquivalent(NumericTag a, NumericTag b) {

        final boolean decimalA = isDecimal(a);
        final boolean decimalB = isDecimal(b);

        // Checks that both are integers or decimals.
        if (decimalA == decimalB) {

            // Checks if both values are decimals which are floats or doubles.
            if (decimalA) {

                return Mth.equal(a.getAsDouble(), b.getAsDouble());
            }

            // Compare integers as longs because it handles all other value cases.
            return a.getAsLong() == b.getAsLong();
        }

        // When A is a decimal and B is an integer, check if A represents an integer.
        else if (decimalA) {

            return DoubleMath.isMathematicalInteger(a.getAsDouble()) && (int) a.getAsDouble() == b.getAsInt();
        }

        // When B is a decimal and A is an integer, check if B represents an integer.
        return DoubleMath.isMathematicalInteger(b.getAsDouble()) && (int) b.getAsDouble() == a.getAsInt();
    }

    public static boolean isDecimal(NumericTag tag) {

        return tag instanceof FloatTag || tag instanceof DoubleTag;
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