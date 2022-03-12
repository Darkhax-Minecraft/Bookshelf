package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;

public interface ICreativeTabHelper {

    ICreativeTabBuilder createBuilder(String namespace, String tabName);
}