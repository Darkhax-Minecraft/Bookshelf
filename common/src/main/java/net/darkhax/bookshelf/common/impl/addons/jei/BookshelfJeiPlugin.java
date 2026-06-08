package net.darkhax.bookshelf.common.impl.addons.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.common.platform.IPlatformRecipeHelper;
import mezz.jei.common.platform.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.addons.jei.category.ComponentSmithingCategoryExtension;
import net.darkhax.bookshelf.common.impl.recipe.smithing.ComponentSmithingRecipe;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class BookshelfJeiPlugin implements IModPlugin {

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        final IExtendableSmithingRecipeCategory smithingCategory = registration.getSmithingCategory();
        final IPlatformRecipeHelper recipeHelper = Services.PLATFORM.getRecipeHelper();
        smithingCategory.addExtension(ComponentSmithingRecipe.class, new ComponentSmithingCategoryExtension(recipeHelper));
    }

    @Override
    public Identifier getPluginUid() {
        return BookshelfMod.id("jei_compat");
    }
}
