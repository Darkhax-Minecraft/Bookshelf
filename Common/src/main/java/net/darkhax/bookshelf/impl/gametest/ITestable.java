package net.darkhax.bookshelf.impl.gametest;

import javax.annotation.Nullable;

public interface ITestable {

    /**
     * Gets a default batch name that will supersede the annotation default when not null.
     *
     * @return The default batch name for all tests in the implementation.
     */
    @Nullable
    default String getDefaultBatch() {

        return null;
    }
}