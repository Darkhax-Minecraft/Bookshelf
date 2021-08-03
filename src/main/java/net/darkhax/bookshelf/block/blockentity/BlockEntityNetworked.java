/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.blockentity;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants.BlockFlags;

public abstract class BlockEntityNetworked extends BlockEntityBase {

    public BlockEntityNetworked (BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket () {

        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket (Connection net, ClientboundBlockEntityDataPacket packet) {

        super.onDataPacket(net, packet);
        this.readSyncData(packet.getTag());
    }

    @Override
    public CompoundTag getUpdateTag () {

        return this.writeSyncData(super.getUpdateTag());
    }
    
    /**
     * Forces a data sync from the server to the client.
     * @param renderUpdate Whether or not a render update should also happen. This should be used when you want to change the baked block model.
     */
    public void sync (boolean renderUpdate) {
    	
    	// Tells the game to re-render the chunk in addition to updating the block entity.
        if (renderUpdate) {

            this.setChanged();
            final BlockState state = this.getCurrentState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, BlockFlags.DEFAULT_AND_RERENDER);
        }

        // Tells the game to send a tracking packet to all players tracking the block entity.
        else if (this.level instanceof ServerLevel) {

            final Packet<?> packet = this.getUpdatePacket();
            WorldUtils.sendToTracking((ServerLevel) this.level, this.getChunkPos(), packet, false);
        }
    }

    /**
     * Writes a data tag that will be sent to the client. This is used to keep the block entity in sync. This will be read by {@link #readSyncData(CompoundTag)} on the client.
     * @param data The data tag that will be sent to the client. This is initially populated using the super {@link #getUpdateTag()}.
     * @return The data tag to sync with the client.
     */
    @Nullable
    public abstract CompoundTag writeSyncData(@Nullable CompoundTag data);
    
    /**
     * Reads a data tag that was sent from the server. The tag should be assumed to have been written using {@link #writeSyncData(CompoundTag)}.
     * @param data The data that was sent from the server.
     */
    public abstract void readSyncData(CompoundTag data);
}