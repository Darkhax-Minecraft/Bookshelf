package net.darkhax.bookshelf.neoforge.impl;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgeMod {

    public NeoForgeMod(IEventBus eventBus) {
        BookshelfMod.getInstance().init();
    }
}