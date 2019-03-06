package net.darkhax.bookshelf.dataloader.sources;

import net.darkhax.bookshelf.dataloader.DataLoader;

/**
 * This interface defines a data provider for a data loader. These are used to load data from
 * various sources of data.
 */
public interface IDataProvider {

    /**
     * Called when the provider should be loaded. The provider is responsible for invoking the
     * processors.
     *
     * @param loader The data loader context.
     */
    void provideDataToProcessors (DataLoader loader);
}