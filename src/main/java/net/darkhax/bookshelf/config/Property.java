package net.darkhax.bookshelf.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;

/**
 * A commented property for use in config files.
 * 
 * @param <T> The type of the property.
 */
public class Property<T> {
    
    /**
     * The value of the property.
     */
    @Expose
    private final T value;
    
    /**
     * A map containing metadata for the property.
     */
    @Expose
    private Map<String, Object> meta;
    
    public Property(T value) {
        
        this.value = value;
    }
    
    /**
     * Includes the default value in the metadata.
     * 
     * @return The property instance.
     */
    public Property<T> defaultValue () {
        
        return this.defaultValue(this.value);
    }
    
    /**
     * Includes the default value in the metadata.
     * 
     * @param value The value to specify as the default value.
     * @return The property instance.
     */
    public Property<T> defaultValue (T value) {
        
        this.getMeta().put("default", value);
        return this;
    }
    
    /**
     * Includes a comment in the metadata for the property. This has support for multiline
     * comments however calling this will replace all previous comments.
     * 
     * @param comments The comment lines to include.
     * @return The property instance.
     */
    public Property<T> comment (String... comments) {
        
        this.getMeta().put("comment", encodeComment(comments));
        return this;
    }
    
    /**
     * Includes an example property in the metadata for the property.
     * 
     * @param example The example value to include.
     * @return The property instance.
     */
    public Property<T> example (T example) {
        
        this.getMeta().put("example", example);
        return this;
    }
    
    /**
     * Includes all values of an enum in the valid values section of the metadata. This can
     * only be used on properties that are holding an enum!
     * 
     * @return The property instance.
     */
    public Property<T> enumValues () {
        
        if (this.value instanceof Enum<?>) {
            
            this.validValues(EnumUtils.getEnumList((Class) this.value.getClass()));
            return this;
        }
        
        throw new IllegalStateException("Expected class '" + this.value.getClass() + "' to be an enum.");
    }
    
    /**
     * Includes all boolean values in the valid values section of the metadata. This can only
     * be used on properties that are holding a boolean.
     * 
     * @return The property instance.
     */
    public Property<T> booleanValues () {
        
        if (this.value instanceof Boolean) {
            
            this.validValues((T[]) new Boolean[] { true, false });
            return this;
        }
        
        throw new IllegalStateException("Expected class '" + this.value.getClass() + "' to be a boolean.");
    }
    
    /**
     * Specifies an array of valid values for this property in the metadata of the property.
     * 
     * @param validValues The range of valid values.
     * @return The property instance.
     */
    public Property<T> validValues (T[] validValues) {
        
        this.getMeta().put("valid_values", validValues);
        return this;
    }
    
    /**
     * Specifies a collection of valid values for this property in the metadata of the
     * property.
     * 
     * @param validValues The range of valid values.
     * @return The property instance.
     */
    public Property<T> validValues (Collection<T> validValues) {
        
        this.getMeta().put("valid_values", validValues);
        return this;
    }
    
    /**
     * Gets the value of the property.
     * 
     * @return The value of the property.
     */
    public T get () {
        
        return this.value;
    }
    
    /**
     * Gets the metadata for the property. This can be used to add additional properties but
     * should be treated as write only.
     * 
     * @return The metadata for the property.
     */
    public Map<String, Object> getMeta () {
        
        if (this.meta == null) {
            
            this.meta = new LinkedHashMap<>();
        }
        
        return this.meta;
    }
    
    @Override
    public String toString () {
        
        return this.value.toString();
    }
    
    /**
     * Encodes the comments into a JsonElement that can be included in the JSON data.
     * 
     * @param comments The comments for the property.
     * @return A JsonElement containing the comments.
     */
    private static JsonElement encodeComment (String[] comments) {
        
        // Handles single line comments.
        if (comments.length == 1) {
            
            // If the comment is greater than 80 characters long split it into a multiline
            // comment.
            if (comments[0].length() > 80) {
                
                final String[] wrapped = WordUtils.wrap(comments[0], 80, "\n", true).split("\n");
                
                if (wrapped.length > 1) {
                    
                    return encodeComment(wrapped);
                }
            }
            
            // Return a primitive string for the comment.
            return new JsonPrimitive(comments[0]);
        }
        
        // If there are multiple comments write them as an array.
        else if (comments.length > 1) {
            
            final JsonArray array = new JsonArray();
            Arrays.stream(comments).forEach(array::add);
            return array;
        }
        
        return null;
    }
}