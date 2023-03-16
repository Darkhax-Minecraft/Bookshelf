package net.darkhax.bookshelf.impl.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;

public class FabricItemAttributeEvent implements IItemAttributeEvent {

    private final ItemStack stack;
    private final EquipmentSlot slotType;
    private final Multimap<Attribute, AttributeModifier> originalModifiers;
    private Multimap<Attribute, AttributeModifier> unmodifiableModifiers;
    @Nullable
    private Multimap<Attribute, AttributeModifier> modifiableModifiers;

    public FabricItemAttributeEvent(ItemStack stack, EquipmentSlot slotType, Multimap<Attribute, AttributeModifier> originalModifiers) {

        this.stack = stack;
        this.slotType = slotType;
        this.unmodifiableModifiers = this.originalModifiers = originalModifiers;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getModifiers() {
        return this.unmodifiableModifiers;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getOriginalModifiers() {
        return this.originalModifiers;
    }

    private Multimap<Attribute, AttributeModifier> getModifiableMap() {
        if(this.modifiableModifiers == null) {
            this.modifiableModifiers = HashMultimap.create(this.originalModifiers);
            this.unmodifiableModifiers = Multimaps.unmodifiableMultimap(this.modifiableModifiers);
        }
        return this.modifiableModifiers;
    }

    @Override
    public boolean addModifier(Attribute attribute, AttributeModifier modifier) {
        return getModifiableMap().put(attribute, modifier);
    }

    @Override
    public boolean removeModifier(Attribute attribute, AttributeModifier modifier) {
        return getModifiableMap().remove(attribute, modifier);
    }

    @Override
    public Collection<AttributeModifier> removeAttribute(Attribute attribute) {
        return getModifiableMap().removeAll(attribute);
    }

    @Override
    public void clearModifiers() {
        getModifiableMap().clear();
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