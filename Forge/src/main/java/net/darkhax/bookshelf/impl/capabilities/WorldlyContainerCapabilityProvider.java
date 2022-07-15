package net.darkhax.bookshelf.impl.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.EnumMap;

public class WorldlyContainerCapabilityProvider implements ICapabilityProvider {

    private final EnumMap<Direction, LazyOptional<?>> sidedCaps = new EnumMap<>(Direction.class);

    public WorldlyContainerCapabilityProvider(WorldlyContainer container) {

        for (Direction side : Direction.values()) {

            sidedCaps.put(side, createSidedCaps(container, side));
        }
    }

    @Override
    public <R> LazyOptional<R> getCapability(Capability<R> requested, @Nullable Direction side) {

        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(requested, sidedCaps.get(side).cast());
    }

    private static LazyOptional<IItemHandler> createSidedCaps(WorldlyContainer container, @Nullable Direction side) {

        if (container != null && side != null) {

            final int[] exposedSlots = container.getSlotsForFace(side);

            if (exposedSlots != null && exposedSlots.length > 0) {

                return LazyOptional.of(() -> new SidedInvWrapper(container, side));
            }
        }

        return LazyOptional.empty();
    }
}