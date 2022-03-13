package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.entity.merchant.trade.VillagerBuys;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.commands.BookshelfCommands;
import net.darkhax.bookshelf.impl.commands.args.FontArgument;
import net.minecraft.world.item.Items;

public class BookshelfContentProvider extends RegistryDataProvider {

    public BookshelfContentProvider() {

        super(Constants.MOD_ID);

        this.commandArguments.add(FontArgument.class, () -> FontArgument.SERIALIZER, "font");
        this.commands.add(BookshelfCommands::new, "commands");
    }
}