package net.darkhax.bookshelf.api.data.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * A partial implementation of {@link net.minecraft.world.item.crafting.Recipe} that is intended for recipes that are
 * not crafted through an inventory or traditional crafting system. This implementation provides default behaviours to
 * ignore the vanilla crafting system.
 * <p>
 * This approach is often preferred over defining your own reload listener as it allows the data to be synced to the
 * client and maintains a load order that is beneficial for implementing support for mods like JEI and CraftTweaker.
 *
 * @param <C> The type of inventory used to craft the recipe. This can usually be ignored.
 */
public abstract class RecipeBaseData<C extends Container> implements Recipe<C> {

    @Override
    public boolean matches(C inventory, Level level) {

        // This is not used by default.
        return false;
    }

    @Override
    public ItemStack assemble(C c, RegistryAccess access) {

        // This is not used by default.
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {

        // This is not used by default.
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {

        // This is not used by default.
        return ItemStack.EMPTY;
    }
}