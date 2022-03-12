package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.util.ICreativeTabHelper;
import net.minecraft.resources.ResourceLocation;

public class CreativeTabBuilderFabric implements ICreativeTabHelper {

    @Override
    public ICreativeTabBuilder createBuilder(String namespace, String tabName) {

        return new net.darkhax.bookshelf.impl.item.CreativeTabBuilderFabric(new ResourceLocation(namespace, tabName));
    }
}
