package net.darkhax.bookshelf.api.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

/**
 * This class contains static helper functions for working with entity attributes and calculating attribute values.
 */
public final class AttributeHelper {

    /**
     * Calculates the attack damage attribute value for a given ItemStack.
     *
     * @param stack The ItemStack to calculate values for.
     * @return The attack damage value for the ItemStack.
     */
    public static double getAttackDamage(ItemStack stack) {

        return calculateValue(stack, Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND);
    }

    /**
     * Calculates the attack speed attribute value for a given ItemStack.
     *
     * @param stack The ItemStack to calculate values for.
     * @return The attack speed value for the ItemStack.
     */
    public static double getAttackSpeed(ItemStack stack) {

        return calculateValue(stack, Attributes.ATTACK_SPEED, EquipmentSlot.MAINHAND);
    }

    /**
     * Calculates the attack knockback attribute value for a given ItemStack.
     *
     * @param stack The ItemStack to calculate values for.
     * @return The attack knockback value for the ItemStack.
     */
    public static double getAttackKnockback(ItemStack stack) {

        return calculateValue(stack, Attributes.ATTACK_KNOCKBACK, EquipmentSlot.MAINHAND);
    }

    /**
     * Calculates an attribute value for an ItemStack when used in a given slot.
     *
     * @param stack     The ItemStack to calculate values for.
     * @param attribute The attribute to calculate.
     * @param slot      The equipment slot to calculate for.
     * @return The calculated attribute value.
     */
    public static double calculateValue(ItemStack stack, Attribute attribute, EquipmentSlot slot) {

        return calculateValue(attribute, stack.getAttributeModifiers(slot).get(attribute));
    }

    /**
     * Calculates an attribute value using a collection of attribute modifiers.
     *
     * @param attribute The attribute to calculate a value for.
     * @param modifiers A collection of modifiers to process.
     * @return The calculated attribute value.
     */
    public static double calculateValue(Attribute attribute, Collection<AttributeModifier> modifiers) {

        return attribute.sanitizeValue(calculateValue(modifiers, attribute.getDefaultValue()));
    }

    /**
     * Calculates an attribute value using a collection of attribute modifiers.
     *
     * @param modifiers A collection of modifiers to process.
     * @param baseValue The base value to use for the calculation.
     * @return The calculated attribute value.
     */
    public static double calculateValue(Collection<AttributeModifier> modifiers, double baseValue) {

        double baseTotal = baseValue;

        for (AttributeModifier modifier : modifiers) {

            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {

                baseTotal += modifier.getAmount();
            }
        }

        double modifiedValue = baseTotal;

        for (AttributeModifier modifier : modifiers) {

            if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {

                modifiedValue += baseTotal * modifier.getAmount();
            }
        }

        for (AttributeModifier modifier : modifiers) {

            if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {

                modifiedValue *= 1 + modifier.getAmount();
            }
        }

        return modifiedValue;
    }
}
