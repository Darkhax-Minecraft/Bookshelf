package net.darkhax.bookshelf.forge;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeMod {

    public ForgeMod() {
        BookshelfMod.getInstance().init();
    }
}