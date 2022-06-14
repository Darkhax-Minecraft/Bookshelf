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

public class SingletonCapabilityProvider<T> implements ICapabilityProvider {

    private final Capability<T> provided;
    private final LazyOptional<T> value;

    public SingletonCapabilityProvider(Capability<T> cap, T value) {

        this.provided = cap;
        this.value = LazyOptional.of(() -> value);
    }

    @Override
    public <R> LazyOptional<R> getCapability(Capability<R> requested, @Nullable Direction side) {

        return this.provided.orEmpty(requested, value);
    }

    public static SingletonCapabilityProvider<IItemHandler> of(WorldlyContainer inventory) {

        return SingletonCapabilityProvider.of(new SidedInvWrapper(inventory, null));
    }

    public static SingletonCapabilityProvider<IItemHandler> of(IItemHandler inventory) {

        return new SingletonCapabilityProvider<>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventory);
    }
}