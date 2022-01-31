package net.darkhax.bookshelf.api;

import net.darkhax.bookshelf.api.util.IClientHelper;

public class ClientServices {

    public static final IClientHelper CLIENT = Services.load(IClientHelper.class);
}
