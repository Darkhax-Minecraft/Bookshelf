package net.darkhax.bookshelf.dataloader.sources;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import org.apache.commons.io.FilenameUtils;

import net.darkhax.bookshelf.dataloader.DataLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ModContainer;

/**
 * This data provider is similar to DataProviderMods, in that it loads files from the various
 * mods that are loaded. The major difference is that this loader will check for a copy of the
 * found file in the config directory of the end users. This source will prefer user defined
 * sources over the mod defined ones when available.
 */
public class DataProviderModsOverridable extends DataProviderMods {

    /**
     * The directory used for user file overrides.
     */
    private final File overrideDir;

    public DataProviderModsOverridable (String modid) {

        this(new File("config/" + modid + "/overrides"), modid);
    }

    public DataProviderModsOverridable (File overrideDir, String ownerId) {

        super(ownerId);
        this.overrideDir = overrideDir;

        if (!this.overrideDir.exists()) {

            this.overrideDir.mkdirs();
        }
    }

    @Override
    protected void processFromPath (DataLoader loader, ModContainer mod, Path root, Path file, String processorType, BiConsumer<ResourceLocation, BufferedReader> processor) {

        final String relative = root.relativize(file).toString();

        if (this.getPathValidator().test(file)) {

            final String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
            final ResourceLocation entryId = new ResourceLocation(mod.getModId(), name);

            // Create a handle for the location of a potential override file.
            final File potentialOverride = new File(this.overrideDir, entryId.getNamespace() + "/" + processorType + "/" + FilenameUtils.getName(relative));

            // If the override exists, use that instead of the one from the bundled data.
            if (potentialOverride.exists()) {

                file = potentialOverride.toPath();
            }

            try (BufferedReader reader = Files.newBufferedReader(file)) {

                processor.accept(entryId, reader);
            }

            catch (final Exception e) {

                loader.getLogger().error("Failed to read file {}. The file was not valid.", file);
                loader.getLogger().catching(e);
            }
        }
    }
}