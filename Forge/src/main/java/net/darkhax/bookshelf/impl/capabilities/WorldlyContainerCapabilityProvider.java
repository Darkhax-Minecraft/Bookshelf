package net.darkhax.bookshelf.impl.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class WorldlyContainerCapabilityProvider implements ICapabilityProvider {

    private final WorldlyContainer container;

    public WorldlyContainerCapabilityProvider(WorldlyContainer container) {

        this.container = container;
    }

    @Override
    public <R> LazyOptional<R> getCapability(Capability<R> requested, @Nullable Direction side) {

        if (requested == ForgeCapabilities.ITEM_HANDLER) {

            return LazyOptional.of(() -> (side != null) ? new SidedInvWrapper(container, side) : new InvWrapper(container)).cast();
        }

        return LazyOptional.empty();
    }
}