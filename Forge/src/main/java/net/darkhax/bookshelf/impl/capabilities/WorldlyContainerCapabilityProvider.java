package net.darkhax.bookshelf.impl.capabilities;

import net.darkhax.bookshelf.api.function.CachedSupplier;
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

    private final CachedSupplier<EnumMap<Direction, LazyOptional<IItemHandler>>> sidedCaps;

    public WorldlyContainerCapabilityProvider(WorldlyContainer container) {

        sidedCaps = CachedSupplier.cache(() -> {

            final EnumMap<Direction, LazyOptional<IItemHandler>> caps = new EnumMap<>(Direction.class);

            for (Direction side : Direction.values()) {

                caps.put(side, container.getSlotsForFace(side).length > 0 ? LazyOptional.of(() -> new SidedInvWrapper(container, side)) : LazyOptional.empty());
            }

            return caps;
        });
    }

    @Override
    public <R> LazyOptional<R> getCapability(Capability<R> requested, @Nullable Direction side) {

        return side != null ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(requested, sidedCaps.get().get(side).cast()) : LazyOptional.empty();
    }
}