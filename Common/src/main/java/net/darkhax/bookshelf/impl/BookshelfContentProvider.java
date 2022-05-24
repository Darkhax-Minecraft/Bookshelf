package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.commands.BookshelfCommands;
import net.darkhax.bookshelf.impl.commands.args.sss;
import net.darkhax.bookshelf.impl.data.recipes.crafting.ShapedDurabilityRecipe;
import net.darkhax.bookshelf.impl.data.recipes.crafting.ShapelessDurabilityRecipe;

public class BookshelfContentProvider extends RegistryDataProvider {

    public BookshelfContentProvider() {

        super(Constants.MOD_ID);

        // Command Argument Types
        this.commandArguments.add(sss.class, () -> sss.SERIALIZER, "font");

        // Command Builders
        this.commands.add(BookshelfCommands::new, "commands");

        // Recipe Serializers
        this.recipeSerializers.add(() -> ShapedDurabilityRecipe.SERIALIZER, "shaped_durability");
        this.recipeSerializers.add(() -> ShapelessDurabilityRecipe.SERIALIZER, "shapeless_durability");
    }
}