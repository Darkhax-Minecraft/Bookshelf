package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.Services;
import net.fabricmc.api.ClientModInitializer;

public class BookshelfFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        Constants.LOG.info(Services.REGISTRY_HELPER.create(Constants.MOD_ID).getClass());
    }
}
