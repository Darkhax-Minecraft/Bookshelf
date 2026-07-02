package net.darkhax.bookshelf.common.api.data.entity;

import com.mojang.serialization.Codec;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.codecs.stream.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.EnumSet;

public final class EquipmentSlotPredicate {

    public static final Codec<EquipmentSlotPredicate> CODEC = MapCodecs.enumSet(EquipmentSlotGroup.class).xmap(EquipmentSlotPredicate::new, p -> p.groups);
    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentSlotPredicate> STREAM = StreamCodecs.enumSet(EquipmentSlotGroup.class).map(EquipmentSlotPredicate::new, p -> p.groups);

    private final EnumSet<EquipmentSlotGroup> groups;
    private final EnumSet<EquipmentSlot> slots;

    public EquipmentSlotPredicate(EnumSet<EquipmentSlotGroup> groups) {
        this.groups = EnumSet.copyOf(groups);
        this.slots = findMatchingSlots(this.groups);
    }

    public boolean testSlot(EquipmentSlot slot) {
        return slots.contains(slot);
    }

    public boolean testGroup(EquipmentSlotGroup group) {
        return groups.contains(group);
    }

    private static EnumSet<EquipmentSlot> findMatchingSlots(EnumSet<EquipmentSlotGroup> groups) {
        final EnumSet<EquipmentSlot> matchingSlots = EnumSet.noneOf(EquipmentSlot.class);
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            for (EquipmentSlotGroup group : groups) {
                if (group.test(slotType)) {
                    matchingSlots.add(slotType);
                    break;
                }
            }
        }
        return matchingSlots;
    }
}
