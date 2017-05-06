/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:51:44 (GMT)]
 *
 * Changes
 * - Added JavaDocs
 * - Fixed some mappings
 * - Changed to Bookshelf's formatting
 * - getAction now returns void, and is functional.
 */
package net.darkhax.bookshelf.network;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class TileEntityMessage<T extends TileEntity> extends SerializableMessage {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -8474561253790105901L;

    /**
     * The position of the TileEntity.
     */
    public BlockPos pos;

    /**
     * The TileEntity.
     */
    public transient T tile;

    /**
     * The message context.
     */
    public transient MessageContext context;

    /**
     * Blank constructor required for all messages.
     */
    public TileEntityMessage () {

    }

    /**
     * Basic constructor for a tile entity update message.
     *
     * @param pos The position of the tile entity.
     */
    public TileEntityMessage (BlockPos pos) {

        this.pos = pos;
    }

    @Override
    public final IMessage handleMessage (MessageContext context) {

        this.context = context;
        final World world = context.getServerHandler().player.getEntityWorld();
        final TileEntity tile = world.getTileEntity(this.pos);

        if (tile != null) {
            try {

                final T castTile = (T) tile;
                this.tile = castTile;
                ((WorldServer) world).addScheduledTask( () -> this.getAction());
            }
            catch (final ClassCastException e) {

                Constants.LOG.warn("Could not cast?", e);
            }
        }

        return null;
    }

    public abstract void getAction ();
}