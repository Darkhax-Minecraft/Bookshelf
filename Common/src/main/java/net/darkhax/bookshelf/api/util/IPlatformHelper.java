package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.PhysicalSide;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;

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
     * Attempts to resolve the name of a mod using the associated mod ID. If no mod is not available the provided ID
     * will be treated as the name.
     *
     * @param modId The ID of the mod to lookup.
     * @return The name of the mod associated with the given mod ID. If no mod is not available the provided ID will be
     * treated as the name.
     */
    @Nullable
    String getModName(String modId);

    /**
     * Attempts to resolve the name of a mod using the associated mod ID. If no mod is not available the provided ID
     * will be treated as the name.
     *
     * @param modId The ID of the mod to lookup.
     * @return The name of the mod associated with the given mod ID. If no mod is not available the provided ID will be
     * treated as the name.
     */
    @Nullable
    default MutableComponent getModNameComponent(String modId) {

        final String modName = getModName(modId);
        return modName != null ? new TextComponent(modName) : null;
    }

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
}