package net.darkhax.bookshelf.api.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public interface IFarmlandTrampleListener {

    /**
     * A neutral event listener that will be invoked when an entity tramples on farmland.
     *
     * @param trampler The entity that trampled the farmland.
     * @param pos      The position of the farmland.
     * @param state    The farmland's blockstate.
     * @return The cancel state of the listener. If true the farmland will not be trampled.
     */
    boolean apply(Entity trampler, BlockPos pos, BlockState state);
}
