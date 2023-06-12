package net.darkhax.bookshelf.api.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.BiConsumer;

public class BaseEnchantment extends Enchantment {

    private final EquipmentSlot[] slots;

    public BaseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {

        super(rarity, category, slots);
        this.slots = slots;
    }

    /**
     * Allows the enchantment to apply attribute modifiers to an enchanted item. This may be called many times with
     * different item/slot combinations, especially in cases like tooltips. In most cases you should override
     * {@link #applyModifiers(int, BiConsumer)} instead, as it has boilerplate checks for the correct slot type built
     * in.
     *
     * @param stack The enchanted ItemStack.
     * @param level The level of the enchantment. This will always be at least 1.
     * @param slot  The slot being tested. This is not necessarily the slot the item is currently in, for example
     *              tooltips will test all slots.
     * @param adder A function to add your new modifiers.
     */
    public void applyModifiers(ItemStack stack, int level, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> adder) {

        if (isValidSlot(slot) && LivingEntity.getEquipmentSlotForItem(stack) == slot) {
            this.applyModifiers(level, adder);
        }
    }

    /**
     * Allows the enchantment to apply attribute modifiers to an enchanted item. This will only be applied if the item
     * is in a valid slot for this type of enchantment.
     *
     * @param level The level of the enchantment. This will always be at least 1.
     * @param adder A function to add your new modifiers.
     */
    public void applyModifiers(int level, BiConsumer<Attribute, AttributeModifier> adder) {

        // No default operation. Override to add your own!
    }

    public boolean isValidSlot(EquipmentSlot slot) {

        for (final EquipmentSlot validSlot : this.slots) {

            if (validSlot == slot) {

                return true;
            }
        }

        return false;
    }
}
