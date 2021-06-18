package net.darkhax.bookshelf.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.Logger;

import com.ibm.icu.text.DecimalFormat;

import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class BasicRegistry<T> {
    
    public static final DecimalFormat DECIMAL_2 = new DecimalFormat("##.##");
    
    private final Logger logger;
    private final String ownerId;
    private final Registry<T> registry;
    private final Map<Identifier, T> values;
    private final Map<Identifier, T> valuesUnmodifiable;
    private final List<BiConsumer<BasicRegistry<T>, T>> registerListeners;
    
    public BasicRegistry(Logger logger, String ownerId, Registry<T> registry) {
        
        this.logger = logger;
        this.ownerId = ownerId;
        this.registry = registry;
        this.values = new LinkedHashMap<>();
        this.valuesUnmodifiable = Collections.unmodifiableMap(this.values);
        this.registerListeners = DefaultedList.of();
    }
    
    public <VT extends T> VT register (VT value, String identifier) {
        
        final Identifier id = new Identifier(this.ownerId, identifier);
        
        if (this.values.containsKey(id)) {
            
            final T existing = this.values.get(id);
            this.logger.error("Attempted to register {} to registry {} using ID {}. This ID already belongs to {}.", value.getClass(), this.registry.getKey().getValue(), id, existing.getClass());
            throw new IllegalArgumentException("The ID " + id.toString() + " has already been internally registered!");
        }
        
        else if (this.registry.containsId(id)) {
            
            final T existing = this.registry.get(id);
            this.logger.error("Attempted to register {} to registry {} using ID {}. This ID already belongs to {}.", value.getClass(), this.registry.getKey().getValue(), id, existing.getClass());
            throw new IllegalArgumentException("The ID " + id.toString() + " has already been externally registered!");
        }
        
        this.values.put(id, value);
        this.registerListeners.forEach(listener -> listener.accept(this, value));
        return value;
    }
    
    public Map<Identifier, T> getValues () {
        
        return this.valuesUnmodifiable;
    }
    
    public void addRegisterListener (BiConsumer<BasicRegistry<T>, T> listener) {
        
        this.registerListeners.add(listener);
    }
    
    public void apply () {
        
        final long startTime = System.nanoTime();
        
        for (final Entry<Identifier, T> entry : this.values.entrySet()) {
            
            this.logger.debug("Registering {} to registry {} with ID \"{}\".", entry.getValue().getClass(), this.registry.getKey().getValue(), entry.getKey());
            Registry.register(this.registry, entry.getKey(), entry.getValue());
        }
        
        final long endTime = System.nanoTime();
        this.logger.info("Registered {} {} entries in {}ms.", this.values.size(), this.registry.getKey().getValue(), DECIMAL_2.format((endTime - startTime) / 1000000f));
    }
}