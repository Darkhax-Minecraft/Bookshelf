package net.darkhax.bookshelf.api.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CurseEnchantment extends BaseEnchantment {

    public CurseEnchantment(EnchantmentCategory category, EquipmentSlot[] slots) {

        this(Rarity.VERY_RARE, category, slots);
    }

    public CurseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {

        super(rarity, category, slots);
    }

    @Override
    public int getMinCost(int level) {

        return 25;
    }

    @Override
    public int getMaxCost(int level) {

        return 50;
    }

    @Override
    public int getMaxLevel() {

        return 1;
    }

    @Override
    public boolean isTreasureOnly() {

        return true;
    }

    @Override
    public boolean isCurse() {

        return true;
    }
}