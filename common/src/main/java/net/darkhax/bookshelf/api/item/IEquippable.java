package net.darkhax.bookshelf.api.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IEquippable {

    default EquipmentSlot getEquipmentSlot(ItemStack stack) {

        return EquipmentSlot.MAINHAND;
    }

    @Nullable
    static IEquippable get(ItemStack stack) {

        if (stack.getItem() instanceof IEquippable equippable) {

            return equippable;
        }

        else if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IEquippable equippable) {

            return equippable;
        }

        return null;
    }
}
