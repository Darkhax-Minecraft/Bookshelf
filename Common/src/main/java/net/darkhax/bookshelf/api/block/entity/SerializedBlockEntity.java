package net.darkhax.bookshelf.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An implementation of BlockEntity that will persist custom data and send this data to clients.
 */
public abstract class SerializedBlockEntity extends BlockEntity {

    public SerializedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag data) {

        super.saveAdditional(data);
        this.writeTileData(data);
    }

    @Override
    public void load(CompoundTag data) {

        super.load(data);
        this.readTileData(data);
    }

    @Override
    public CompoundTag getUpdateTag() {

        final CompoundTag updateTag = super.getUpdateTag();
        writeTileData(updateTag);
        return updateTag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this);
    }

    public abstract void writeTileData(CompoundTag data);

    public abstract void readTileData(CompoundTag data);
}