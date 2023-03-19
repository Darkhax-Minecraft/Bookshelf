package net.darkhax.bookshelf.impl.event;

import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class FabricItemAttributeEvent implements IItemAttributeEvent {

    private final ItemStack stack;
    private final EquipmentSlot slotType;
    private final Multimap<Attribute, AttributeModifier> modifiers;

    public FabricItemAttributeEvent(ItemStack stack, EquipmentSlot slotType, Multimap<Attribute, AttributeModifier> originalModifiers) {

        this.stack = stack;
        this.slotType = slotType;
        this.modifiers = originalModifiers;
    }

    @Override
    public boolean addModifier(Attribute attribute, AttributeModifier modifier) {
        return this.modifiers.put(attribute, modifier);
    }

    @Override
    public boolean removeModifier(Attribute attribute, AttributeModifier modifier) {
        return this.modifiers.remove(attribute, modifier);
    }

    @Override
    public Collection<AttributeModifier> removeAttribute(Attribute attribute) {
        return this.modifiers.removeAll(attribute);
    }

    @Override
    public void clearModifiers() {
        this.modifiers.clear();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.slotType;
    }

    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }
}