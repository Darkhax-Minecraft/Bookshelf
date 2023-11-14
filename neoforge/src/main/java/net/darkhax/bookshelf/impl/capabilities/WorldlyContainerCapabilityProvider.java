package net.darkhax.bookshelf.impl.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class WorldlyContainerCapabilityProvider implements ICapabilityProvider {

    private final WorldlyContainer container;

    public WorldlyContainerCapabilityProvider(WorldlyContainer container) {

        this.container = container;
    }

    @Override
    public <R> LazyOptional<R> getCapability(Capability<R> requested, @Nullable Direction side) {

        if (requested == Capabilities.ITEM_HANDLER) {

            return LazyOptional.of(() -> (side != null) ? new SidedInvWrapper(container, side) : new InvWrapper(container)).cast();
        }

        return LazyOptional.empty();
    }
}