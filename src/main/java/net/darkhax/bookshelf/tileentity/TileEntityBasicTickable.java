package net.darkhax.bookshelf.tileentity;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.util.ITickable;

public class TileEntityBasicTickable extends TileEntityBasic implements ITickable {

    @Override
    public void update () {

        if (this.isInvalid() || !this.getWorld().isBlockLoaded(this.getPos()) || this.getWorld().isRemote)
            return;

        try {

            this.onEntityUpdate();
        }

        catch (final Exception exception) {

            Constants.LOG.warn("A TileEntity at %s in world %s failed a client update tick!", this.getPos().toString(), this.getWorld().getWorldInfo().getWorldName(), exception);
        }
    }

    /**
     * Handles the TileEntity update ticks. This method will only be called in a safe
     * environment.
     */
    public void onEntityUpdate () {

    }
}