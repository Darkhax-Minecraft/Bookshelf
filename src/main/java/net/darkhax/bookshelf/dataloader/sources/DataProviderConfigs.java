package net.darkhax.bookshelf.dataloader.sources;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import net.darkhax.bookshelf.dataloader.DataLoader;
import net.minecraft.util.ResourceLocation;

/**
 * This data provider is used to load data in from the config directory. This allows end users
 * to directly create new content which can be used in your data driven system.
 */
public class DataProviderConfigs extends DataProviderPredicate {

    /**
     * The directory for config files to be loaded from.
     */
    private final File configDir;

    public DataProviderConfigs (String modid) {

        this(new File("config/" + modid + "/additions"));
    }

    public DataProviderConfigs (File configDir) {

        this.configDir = configDir;

        // Ensures the config directory exists so users can find it.
        if (!this.configDir.exists()) {

            this.configDir.mkdirs();
        }
    }

    @Override
    public void provideDataToProcessors (DataLoader loader) {

        // Iterate the processors
        for (final Entry<String, BiConsumer<ResourceLocation, BufferedReader>> entry : loader.getProcessors().entrySet()) {

            // Look in the directory that matches the data type of the processor.
            final File dirToLoad = new File(this.configDir, entry.getKey());

            // Ensure the directory is actually a directory.
            if (dirToLoad.isDirectory()) {

                // Iterate the files of the current directory.
                for (final File file : dirToLoad.listFiles()) {

                    // Get the path of the current file, so it can be tested.
                    final Path filePath = file.toPath();

                    // Check if the path is valid.
                    if (this.getPathValidator().test(filePath)) {

                        // Load the file and pass it off to the processor.
                        try (BufferedReader reader = Files.newBufferedReader(filePath)) {

                            // ResourceLocation is null, because we don't have that context
                            // data.
                            entry.getValue().accept(null, reader);
                        }

                        catch (final Exception e) {

                            loader.getLogger().error("Failed to read file {}. The file was not valid.", file);
                            loader.getLogger().catching(e);
                        }
                    }
                }
            }
        }
    }
}