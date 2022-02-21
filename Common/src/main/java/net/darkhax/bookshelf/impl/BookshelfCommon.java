package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.registry.RegistryHelper;
import net.darkhax.bookshelf.impl.commands.BookshelfCommands;
import net.darkhax.bookshelf.impl.commands.args.FontArgument;
import net.darkhax.bookshelf.impl.fixes.MC151457;
import net.minecraft.util.Tuple;

public class BookshelfCommon {

    public BookshelfCommon() {

        RegistryHelper registry = Services.REGISTRY_HELPER.create(Constants.MOD_ID);
        registry.commandArguments.add(new Tuple<>(FontArgument.class, FontArgument.SERIALIZER), "font");
        registry.commands.add(new BookshelfCommands(), "commands");
        registry.init();

        MC151457.applyFix();
    }
}