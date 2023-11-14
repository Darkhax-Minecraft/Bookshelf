package net.darkhax.bookshelf.impl.event;

import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.Collection;

public class NeoForgeItemAttributeEvent implements IItemAttributeEvent {

    private final ItemAttributeModifierEvent internal;

    public NeoForgeItemAttributeEvent(ItemAttributeModifierEvent event) {

        this.internal = event;
    }

    @Override
    public boolean addModifier(Attribute attribute, AttributeModifier modifier) {
        return this.internal.addModifier(attribute, modifier);
    }

    @Override
    public boolean removeModifier(Attribute attribute, AttributeModifier modifier) {
        return this.internal.removeModifier(attribute, modifier);
    }

    @Override
    public Collection<AttributeModifier> removeAttribute(Attribute attribute) {
        return this.internal.removeAttribute(attribute);
    }

    @Override
    public void clearModifiers() {
        this.internal.clearModifiers();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.internal.getSlotType();
    }

    @Override
    public ItemStack getItemStack() {
        return this.internal.getItemStack();
    }
}