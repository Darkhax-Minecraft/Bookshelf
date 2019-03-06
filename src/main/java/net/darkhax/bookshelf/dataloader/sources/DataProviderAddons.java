package net.darkhax.bookshelf.dataloader.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import net.darkhax.bookshelf.dataloader.DataLoader;
import net.minecraft.util.ResourceLocation;

/**
 * This data provider is used to load data from addon files. In this context an addon file is a
 * ZIP archive, containing files that can be used by a data driven system. These files are
 * usually provided by end users who want to share their custom creations made with the data
 * driven system.
 */
public class DataProviderAddons extends DataProviderPredicate {

    /**
     * The directory to search for addon files.
     */
    private final File addonsDir;

    public DataProviderAddons (String modid) {

        this(new File("addons/" + modid));
    }

    public DataProviderAddons (File addonsDir) {

        this.addonsDir = addonsDir;

        if (!this.addonsDir.exists()) {

            this.addonsDir.mkdirs();
        }
    }

    /**
     * This method is applied to all the files found from the stream of the addon file. It is
     * used to validate the path of the file, and load it in for the processors.
     *
     * @param loader An instance of the data loader.
     * @param filePath The path of the file being loaded.
     * @param file The file being loaded.
     * @param processor The processor that wants to process this file.
     */
    private void loadFromPath (DataLoader loader, Path filePath, File file, BiConsumer<ResourceLocation, BufferedReader> processor) {

        if (this.getPathValidator().test(filePath)) {

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {

                processor.accept(null, reader);
            }

            catch (final Exception e) {

                loader.getLogger().error("Failed to read file {} from pack {}. The file was not valid.", filePath.toString(), file.getName());
                loader.getLogger().catching(e);
            }
        }
    }

    /**
     * Checks if a File is a valid addon pack file.
     *
     * @param file The file to test.
     * @return Whether or not the file is a valid addon pack.
     */
    private boolean isValidPackFile (File file) {

        return !file.isDirectory() && "zip".equalsIgnoreCase(FilenameUtils.getExtension(file.getPath()));
    }

    @Override
    public void provideDataToProcessors (DataLoader loader) {

        // Make sure the addons directory exists, and is actually a director.
        if (this.addonsDir.exists() && this.addonsDir.isDirectory()) {

            // Iterate the files in the addons directory.
            for (final File file : this.addonsDir.listFiles()) {

                // Make sure the pack is valid.
                if (this.isValidPackFile(file)) {

                    // Create a new FileSystem for the loaded file.
                    try (FileSystem fs = FileSystems.newFileSystem(file.toPath(), null)) {

                        // Iterate the processors, allowing them to read from the filesystem.
                        for (final Entry<String, BiConsumer<ResourceLocation, BufferedReader>> entry : loader.getProcessors().entrySet()) {

                            // Create the base file path for the processor's data type.
                            final Path root = fs.getPath("/", entry.getKey());

                            // Check to see if the path exists.
                            if (Files.exists(root)) {

                                // Stream through the files in the directory
                                try (Stream<Path> stream = Files.walk(root)) {

                                    // Apply the processor to every file in the stream.
                                    stream.forEach(path -> this.loadFromPath(loader, path, file, entry.getValue()));
                                }
                            }
                        }
                    }

                    catch (final IOException e) {

                        loader.getLogger().info("Unable to load from {}", file.getName());
                        loader.getLogger().error(e);
                    }
                }
            }
        }
    }
}
