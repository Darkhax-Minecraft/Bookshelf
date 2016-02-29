package net.darkhax.bookshelf.lib.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyObject<T> implements IUnlistedProperty<T> {
    
    /**
     * The name of the property.
     */
    private final String name;
    
    /**
     * The type of class held by it.
     */
    private final Class<T> type;
    
    /**
     * A generic Unlisted Property which can be used to theoretically hold any Object. This
     * property does not make an attempt to determine whether or not the generic object stored
     * is valid. The value stored by the property is equal to the object's toString value. For
     * more complex properties, use a specific property type, or make your own.
     * 
     * @param name: The name to use for this property.
     * @param type: The class type of this property.
     */
    public PropertyObject(String name, Class<T> type) {
        
        this.name = name;
        this.type = type;
    }
    
    @Override
    public String getName () {
        
        return name;
    }
    
    @Override
    public Class<T> getType () {
        
        return type;
    }
    
    @Override
    public boolean isValid (T object) {
        
        return true;
    }
    
    @Override
    public String valueToString (T object) {
        
        return object.toString();
    }
}