package net.darkhax.bookshelf.api.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BaseEnchantment extends Enchantment {

    private final EquipmentSlot[] slots;

    public BaseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {

        super(rarity, category, slots);
        this.slots = slots;
    }

    public final EquipmentSlot[] getValidSlots() {

        return this.slots;
    }

    public boolean isValidSlot (EquipmentSlot slot) {

        for (final EquipmentSlot validSlot : this.slots) {

            if (validSlot == slot) {

                return true;
            }
        }

        return false;
    }
}
