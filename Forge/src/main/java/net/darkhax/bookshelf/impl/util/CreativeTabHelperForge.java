package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.util.ICreativeTabHelper;
import net.darkhax.bookshelf.impl.item.CreativeTabBuilderForge;
import net.minecraft.resources.ResourceLocation;

public class CreativeTabHelperForge implements ICreativeTabHelper {

    @Override
    public ICreativeTabBuilder createBuilder(String namespace, String tabName) {

        return new CreativeTabBuilderForge(new ResourceLocation(namespace, tabName));
    }
}
