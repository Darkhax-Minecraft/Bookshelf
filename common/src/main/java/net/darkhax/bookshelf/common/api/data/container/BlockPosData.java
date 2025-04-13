package net.darkhax.bookshelf.common.api.data.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

import java.util.Arrays;

/**
 * Allows a block position to be kept in sync using the container system. The server should always construct this
 * directly while the client should use SimpleContainerData with size of 3.
 */
public class BlockPosData implements ContainerData {

    private final int[] pos;
    private final boolean mutable;

    public BlockPosData(BlockPos pos) {
        this(pos, false);
    }

    public BlockPosData(BlockPos pos, boolean mutable) {
        this.pos = new int[]{pos.getX(), pos.getY(), pos.getZ()};
        this.mutable = mutable;
    }

    @Override
    public int get(int slot) {
        return pos[slot];
    }

    @Override
    public void set(int slot, int value) {
        if (this.mutable) {
            pos[slot] = value;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Gets the BlockPos currently held by the container data. This should only be called on the server.
     *
     * @return The BlockPos being held.
     */
    public BlockPos getPos() {
        return new BlockPos(pos[0], pos[1], pos[2]);
    }

    /**
     * Reads a BlockPos from untyped container data. This should be used to read the position on the client which should
     * be using a SimpleContainerData and not a BlockPosData.
     *
     * @param data The data to read from.
     * @return The BlockPos that was ready.
     */
    public static BlockPos readPos(ContainerData data) {
        if (data.getCount() != 3) {
            throw new IllegalStateException("Can not read BlockPos from container data. Expected 3 values, found " + data.getCount() + ". data=" + Arrays.toString(toArray(data)));
        }
        return new BlockPos(data.get(0), data.get(1), data.get(2));
    }

    private static int[] toArray(ContainerData containerData) {
        final int[] data = new int[containerData.getCount()];
        for (int i = 0; i < containerData.getCount(); i++) {
            data[i] = containerData.get(i);
        }
        return data;
    }
}
