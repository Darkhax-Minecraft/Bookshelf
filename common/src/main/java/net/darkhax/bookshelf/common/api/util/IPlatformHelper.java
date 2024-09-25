package net.darkhax.bookshelf.common.api.util;

import net.darkhax.bookshelf.common.api.ModEntry;
import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

/**
 * The PlatformHelper provides useful context and information about the platform the game is running on.
 */
public interface IPlatformHelper {

    /**
     * Gets the working directory path of the game directory.
     *
     * @return The working directory path of the game directory.
     */
    Path getGamePath();

    /**
     * Gets the working directory of the game as a File.
     *
     * @return The working directory of the game.
     */
    default File getGameDirectory() {
        return this.getGamePath().toFile();
    }

    /**
     * Gets the specified configuration path for the game.
     *
     * @return The specified configuration path for the game.
     */
    Path getConfigPath();

    /**
     * Gets the specified configuration directory as a file reference.
     *
     * @return The specified configuration path for the game.
     */
    default File getConfigDirectory() {
        return this.getConfigPath().toFile();
    }

    /**
     * Gets the primary path that the current loader will load mods from.
     *
     * @return The currently specified mods path.
     */
    Path getModsPath();

    /**
     * Gets the primary directory that the current loader will load mods from.
     *
     * @return The currently specified mods directory.
     */
    default File getModsDirectory() {
        return this.getModsPath().toFile();
    }

    /**
     * Checks if a given mod is loaded.
     *
     * @param modId The mod id to search for.
     * @return True when the specified mod id has been loaded.
     */
    boolean isModLoaded(String modId);

    /**
     * Checks if the mod is running in a development environment.
     *
     * @return True when the mod is running in a developer environment.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the physical environment that the code is running on.
     *
     * @return The physical environment that the code is running on.
     */
    PhysicalSide getPhysicalSide();

    /**
     * Checks if the code is running on a physical client.
     *
     * @return Returns true when the code is running on a physical client.
     */
    default boolean isPhysicalClient() {
        return this.getPhysicalSide().isClient();
    }

    /**
     * Gets a set of every loaded modId.
     * @return
     */
    Set<ModEntry> getLoadedMods();

    /**
     * Checks if the mod is currently running in an environment with game tests enabled.
     *
     * @return Are game tests currently enabled?
     */
    boolean isTestingEnvironment();

    /**
     * Gets the name of the platform.
     *
     * @return The name of the platform.
     */
    String getName();

    @Deprecated
    <T extends AbstractContainerMenu> void unsafeRegisterMenu(ResourceLocation id, MenuRegister.ClientMenuFactory<T> clientFactory);
}