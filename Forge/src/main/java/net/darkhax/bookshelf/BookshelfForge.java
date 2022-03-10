package net.darkhax.bookshelf;

import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class BookshelfForge {

    public BookshelfForge() {

        new BookshelfCommon();
    }
}