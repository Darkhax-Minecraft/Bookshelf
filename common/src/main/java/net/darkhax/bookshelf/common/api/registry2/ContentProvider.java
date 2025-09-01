package net.darkhax.bookshelf.common.api.registry2;

import net.darkhax.bookshelf.common.api.registry2.adapters.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry2.adapters.GameRegistryAdapter;
import net.minecraft.world.item.Item;

/**
 * An interface for adding custom game content such as blocks and items during the appropriate stages of the game's
 * lifecycle.
 * <p>
 * Implementations of this interface are discovered automatically by Bookshelf using the {@link java.util.ServiceLoader}
 * mechanism. To make your provider loadable, add the fully qualified name of your implementation to the file:
 * <pre>
 *     META-INF/services/net.darkhax.bookshelf.common.api.registry2.ContentProvider
 * </pre>
 * An example entry can be found here: TODO
 */
public interface ContentProvider {

    /**
     * Registers new blocks with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineBlocks(BlockRegistryAdapter registry) {
    }

    /**
     * Registers new items with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineItems(GameRegistryAdapter<Item> registry) {
    }

    /**
     * Gets the namespace that all content from the provider should be registered under. This MUST be the same modid
     * that is used by your NeoForge/Fabric mod.
     *
     * @return The namespace to register content with.
     */
    String namespace();

    /**
     * Checks if content from the provider should be loaded or not. All providers will be loaded by default, however
     * custom implementations may have additional requirements.
     * <p>
     * Bookshelf will still classload your provider if this returns false, this only prevents content from being loaded.
     * It is the implementers responsibility to ensure their class can be classloaded safely.
     *
     * @return If content from this provider should be loaded.
     */
    default boolean canLoad() {
        return true;
    }
}