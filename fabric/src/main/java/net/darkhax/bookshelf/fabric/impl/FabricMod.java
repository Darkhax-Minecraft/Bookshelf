package net.darkhax.bookshelf.fabric.impl;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        BookshelfMod.getInstance().init();
    }
}