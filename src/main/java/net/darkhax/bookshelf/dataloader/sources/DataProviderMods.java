package net.darkhax.bookshelf.dataloader.sources;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.io.FilenameUtils;

import net.darkhax.bookshelf.dataloader.DataLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * This data provider allows data files to be loaded from mod containers.
 */
public class DataProviderMods extends DataProviderPredicate {

    /**
     * The owner of the data to provide. This is used to minimize conflicts in the file system
     * structure.
     */
    private final String ownerId;

    public DataProviderMods (String ownerId) {

        this.ownerId = ownerId;
    }

    @Override
    public void provideDataToProcessors (DataLoader loader) {

        // Iterate all the loaded mods
        for (final ModContainer mod : Loader.instance().getActiveModList()) {

            // Create a path resolver instance, which can handle dir/file mod loading.
            try (PathResolver resolver = new PathResolver(mod)) {

                // Iterate the processors
                for (final Entry<String, BiConsumer<ResourceLocation, BufferedReader>> entry : loader.getProcessors().entrySet()) {

                    final String processorType = entry.getKey();

                    // Get the path the data processor applies to.
                    final Path root = resolver.getPath("data/" + mod.getModId() + "/" + this.ownerId + "/" + processorType);

                    // Check to see if the path exists.
                    if (root != null && Files.exists(root)) {

                        // Stream through the files in the directory
                        try (Stream<Path> stream = Files.walk(root)) {

                            stream.forEach(path -> this.processFromPath(loader, mod, root, path, processorType, entry.getValue()));
                        }
                    }
                }
            }

            catch (final IOException e) {

                loader.getLogger().error("Error while loading data from {}.", mod.getModId());
                loader.getLogger().catching(e);
            }
        }
    }

    /**
     * Loads the data from the detected path and passes it off to the processor.
     *
     * @param loader An instance of the data loader context.
     * @param mod The container of the mod being loaded.
     * @param root The root/base path.
     * @param file The path of the file being loaded.
     * @param processorType The processor data type.
     * @param processor The data processor.
     */
    protected void processFromPath (DataLoader loader, ModContainer mod, Path root, Path file, String processorType, BiConsumer<ResourceLocation, BufferedReader> processor) {

        final String relative = root.relativize(file).toString();

        if (this.getPathValidator().test(file)) {

            try (BufferedReader reader = Files.newBufferedReader(file)) {

                final String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                final ResourceLocation entryId = new ResourceLocation(mod.getModId(), name);

                processor.accept(entryId, reader);
            }

            catch (final Exception e) {

                loader.getLogger().error("Failed to read file {}. The file was not valid.", file);
                loader.getLogger().catching(e);
            }
        }
    }

    /**
     * This class is used to handle the nuance of mod sources. Mods are normally loaded from
     * jar files, but in the case of the dev environment the mod is loaded from a directory.
     * This requires some special case handling for directory/folder based mods.
     */
    class PathResolver implements Closeable {

        /**
         * A reference to the source file or directory.
         */
        private final File source;

        /**
         * The file system to resolve for. If this is null, the mod is a directory mod and has
         * no archive.
         */
        @Nullable
        private FileSystem fs;

        public PathResolver (ModContainer mod) throws IOException {

            this.source = mod.getSource();

            if (this.source.isFile()) {

                this.fs = FileSystems.newFileSystem(this.source.toPath(), null);
            }
        }

        /**
         * Resolves a string to a path.
         *
         * @param base The base path string.
         * @return The resolved path.
         */
        public Path getPath (String base) {

            return this.fs != null ? this.fs.getPath("/", base) : this.source.toPath().resolve(base);
        }

        @Override
        public void close () throws IOException {

            if (this.fs != null) {

                this.fs.close();
            }
        }
    }
}