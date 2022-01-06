package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.Services;
import net.fabricmc.api.DedicatedServerModInitializer;

public class BookshelfFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {

        Constants.LOG.info(Services.PLATFORM.getPhysicalSide().isClient());
    }
}