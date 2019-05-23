package net.darkhax.bookshelf.dataloader;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.dataloader.sources.IDataProvider;
import net.minecraft.util.ResourceLocation;

/**
 * This class provides a tool for loading data into data driven systems. The general idea is
 * that you create a new DataLoader instance, add some data processors to it using
 * {@link #addProcessor(String, BiConsumer)} and then specify some data providers using
 * {@link #addDataProvider(IDataProvider)}. This goal of this system is to allow a mod to load
 * their data from many different sources while also providing flexibility to the end users.
 * This project also aims to minimize the amount of duplicate code needed to process files from
 * different sources.
 */
public class DataLoader {

    /**
     * A list of all the data providers that have been added to the data loader. You can add to
     * this using {@link #addDataProvider(IDataProvider)}.
     */
    private final List<IDataProvider> dataProviders = new ArrayList<>();

    /**
     * A map containing all the processors. The key is the type of data the processor wants to
     * process, and the value is the processor itself.
     *
     * The key is used for searching specific file paths. For example, this may look like
     * data/modid/key when looking for data.
     */
    private final Map<String, BiConsumer<ResourceLocation, BufferedReader>> processors = new HashMap<>();

    /**
     * An instance of a logger that can be used for reporting errors about data loading.
     */
    private final Logger logger;

    public DataLoader (Logger log) {

        this.logger = log;
    }

    /**
     * Adds a data provider to the providers list.
     *
     * @param provider The data provider to add.
     */
    public DataLoader addDataProvider (IDataProvider provider) {

        this.dataProviders.add(provider);
        return this;
    }

    /**
     * Adds a processor to the list of data processors.
     *
     * @param dataType The type of data the processor will handle. This is used as a file path
     *        key to help the data providers find data for the processor. For example it may be
     *        used to search data/modid/{dataType} for files.
     * @param fileProcessor The file processor. The ResourceLocation is provided based on
     *        context from the data provider but it can be null. The BufferedReader is a reader
     *        containing the data for the file.
     */
    public DataLoader addProcessor (String dataType, BiConsumer<ResourceLocation, BufferedReader> fileProcessor) {

        this.processors.put(dataType, fileProcessor);
        return this;
    }

    /**
     * Gets the logger used by this data loader instance.
     *
     * @return The logger specified for this instance.
     */
    public Logger getLogger () {

        return this.logger;
    }

    /**
     * Gets the map of all processors. This should not be used to add/remove processors. Look
     * at {@link #addProcessor(String, BiConsumer)} instead.
     *
     * @return A map of all processors and their data types.
     */
    public Map<String, BiConsumer<ResourceLocation, BufferedReader>> getProcessors () {

        return this.processors;
    }

    /**
     * Loads all the data providers and asks them to provide thata to the processors. This
     * should only be called after you're ready to load everything.
     */
    public void loadData () {

        for (final IDataProvider source : this.dataProviders) {

            source.provideDataToProcessors(this);
        }
    }
}