package net.darkhax.bookshelf.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.bookshelf.serialization.gson.TypeAdapterRegistryEntry;
import net.fabricmc.loader.api.FabricLoader;

/**
 * This class is used to read and write arbitrary objects as config files.
 *
 * @param <T> The type of object being read/written by the handler.
 */
public class ConfigHandler<T> {
    
    /**
     * The default GSON instance used to serialize config files. This is changed using a
     * constructor argument.
     */
    private static final Gson DEFAULT_GSON;
    
    static {
        
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.disableHtmlEscaping();
        TypeAdapterRegistryEntry.registerVanillaTypes(builder);
        DEFAULT_GSON = builder.create();
    }
    
    /**
     * Quickly loads a config file without keeping a reference to the ConfigHandler. This will
     * write the config file to disk if it did not already exist.
     * 
     * @param <T> The config object to read/write.
     * @param logger A logger to print errors to.
     * @param name The file name for the config file.
     * @param defaultConfig The default config instance.
     * @return The loaded config value.
     */
    public static <T> T loadConfig (Logger logger, String name, T defaultConfig) {
        
        return new ConfigHandler<>(logger, name, defaultConfig).read();
    }
    
    /**
     * A logger used to output warnings and errors from reading/writing the config.
     */
    private final Logger logger;
    
    /**
     * The location of the config file.
     */
    private final File file;
    
    /**
     * The default instance of the config.
     */
    private final T defaultConfig;
    
    /**
     * The Gson instance used to read/write the config file.
     */
    private final Gson gson;
    
    public ConfigHandler(Logger logger, String name, T defaultConfig) {
        
        this(logger, defaultConfig, FabricLoader.getInstance().getConfigDir().resolve(name + ".json").toFile(), DEFAULT_GSON);
    }
    
    public ConfigHandler(Logger logger, T defaultConfig, File file, Gson gson) {
        
        this.logger = logger;
        this.file = file;
        this.defaultConfig = defaultConfig;
        this.gson = gson;
    }
    
    /**
     * Attempts to read the config file. This will create the file if it does not already exist
     * and then save the file to strip outdated or invalid user inputs.
     * 
     * @return The config instance that was read.
     */
    public T read () {
        
        // If the file does not exist write the default config to the file.
        if (!this.file.exists()) {
            
            this.write(this.defaultConfig);
        }
        
        try (FileReader reader = new FileReader(this.file)) {
            
            // Deserialize the config from the file using Gson.
            final Class<T> clazz = (Class<T>) this.defaultConfig.getClass();
            final T result = this.gson.fromJson(reader, clazz);
            
            // Save the newly read result to strip out any outdated or incorrect properties and
            // write in any new properties.
            this.write(result);
            
            return result;
        }
        
        catch (final IOException e) {
            
            this.logger.error("Failed to read config file {}.", this.file.getAbsolutePath(), e);
        }
        
        return null;
    }
    
    /**
     * Writes a config instance to the file.
     * 
     * @param config The config instance to write.
     */
    public void write (T config) {
        
        // If the file doesn't exist generate the parent files.
        if (!this.file.exists()) {
            
            this.file.getParentFile().mkdirs();
        }
        
        // Write the config instance using gson. This will create the JSON file if it does not
        // exist.
        try (FileWriter writer = new FileWriter(this.file, false)) {
            
            this.gson.toJson(config, writer);
        }
        
        catch (final IOException e) {
            
            this.logger.error("Failed to create config file {}.", this.file.getAbsolutePath(), e);
        }
    }
}