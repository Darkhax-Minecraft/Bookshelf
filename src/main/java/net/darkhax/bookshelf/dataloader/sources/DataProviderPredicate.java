package net.darkhax.bookshelf.dataloader.sources;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;

/**
 * This data provider includes the built in code for doing predicate checks of the path of
 * loaded files.
 */
public abstract class DataProviderPredicate implements IDataProvider {

    /**
     * A predicate that checks if a file has the .json extension type. This does not guarantee
     * that the file contains valid json data, or that the file conforms to the json standard
     * syntax. This is used as the default predicate for all descendant data sources.
     */
    private static final Predicate<Path> JSON = path -> "json".equals(FilenameUtils.getExtension(path.toString()));

    /**
     * The predicate to use. This will be {@link #JSON} by default.
     */
    private Predicate<Path> pathValidator;

    public DataProviderPredicate () {

        this.pathValidator = JSON;
    }

    /**
     * Gets a predicate that validates a path object.
     *
     * @return The path validation predicate.
     */
    public Predicate<Path> getPathValidator () {

        return this.pathValidator;
    }

    /**
     * Sets the predicate used to validate paths for the source.
     *
     * @param pathValidator The path validation predicate.
     * @return An instance of the source.
     */
    public IDataProvider setPathValidator (Predicate<Path> pathValidator) {

        this.pathValidator = pathValidator;
        return this;
    }
}
