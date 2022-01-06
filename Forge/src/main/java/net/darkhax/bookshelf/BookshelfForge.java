package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.Services;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class BookshelfForge {

    public BookshelfForge() {

        Constants.LOG.info(Services.REGISTRY_HELPER.create(Constants.MOD_ID).getClass());
    }
}