package net.darkhax.bookshelf.block.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import net.minecraft.block.properties.PropertyHelper;

public class PropertyString extends PropertyHelper<String> {
    
    private final Collection<String> possibleValues;
    
    private final Map<String, Integer> valueMap = new HashMap<>();
    
    public PropertyString(String name, String... values) {
        
        super(name, String.class);
        this.possibleValues = new ArrayList<>(Arrays.asList(values));
        
        for (int i = 0; i < values.length; i++) {
            
            this.valueMap.put(values[i], i);
        }
    }
    
    @Override
    public Collection<String> getAllowedValues () {
        
        return this.possibleValues;
    }
    
    @Override
    public Optional<String> parseValue (String value) {
        
        return Optional.of(value);
    }
    
    @Override
    public String getName (String value) {
        
        return value;
    }
    
    public int getMetaData (String string) {
        
        return this.valueMap.get(string);
    }
}