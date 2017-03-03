package net.darkhax.bookshelf.inventory;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotArmor extends Slot {

    /**
     * The entity to show armor for.
     */
    private final Entity entity;

    /**
     * The type of equipment held by the slot.
     */
    private final EntityEquipmentSlot slotType;

    /**
     * Creates a new armor inventory slot. Typically used for player armor in a GUI.
     *
     * @param entity The entity to show armor for.
     * @param type The type of equipment held by the slot.
     * @param inventory The inventory of the slot.
     * @param index The index of the slot.
     * @param xPosition The x position of the slot.
     * @param yPosition The y position of the slot.
     */
    public SlotArmor (Entity entity, EntityEquipmentSlot type, IInventory inventory, int index, int xPosition, int yPosition) {

        super(inventory, index, xPosition, yPosition);
        this.entity = entity;
        this.slotType = type;
    }

    @Override
    public int getSlotStackLimit () {

        return 1;
    }

    @Override
    public boolean isItemValid (ItemStack stack) {

        return ItemStackUtils.isValidStack(stack) ? stack.getItem().isValidArmor(stack, this.slotType, this.entity) : false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getSlotTexture () {

        return ItemArmor.EMPTY_SLOT_NAMES[this.slotType.getIndex()];
    }
}
