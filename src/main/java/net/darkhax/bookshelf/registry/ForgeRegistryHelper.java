package net.darkhax.bookshelf.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * A helper and wrapper for interacting with Forge registries and their entries.
 *
 * @param <T> The type of value held by the registry.
 */
public class ForgeRegistryHelper<T extends IForgeRegistryEntry<T>> {
    
    /**
     * A logger instance used for outputting info and debug info.
     */
    private final Logger logger;
    
    /**
     * The ID of the mod which owns this registry helper.
     */
    private final String ownerId;
    
    /**
     * A hard reference to the type represented by the generic.
     */
    private final Class<T> type;
    
    /**
     * A collection of suppliers which generate values at the last possible moment.
     */
    private final NonNullList<Supplier<T>> valueSuppliers;
    
    /**
     * A collection of registered values. Only accessible after
     * {@link #injectRegistryEntries(Register)} has ran and {@link #registered} is true.
     */
    private final NonNullList<T> values;
    
    /**
     * A collection of listeners which fire off when a new value is registered. Ignores
     * supplier entries.
     */
    private final List<BiConsumer<ForgeRegistryHelper<T>, T>> registerListeners;
    
    /**
     * A collection of listeners which fire off after values have been generated and injected
     * into the registry.
     */
    private final List<BiConsumer<ForgeRegistryHelper<T>, IForgeRegistry<T>>> registryListeners;
    
    /**
     * Whether or not the registry has been initialized. New values can not be added after the
     * registry has been initialized.
     */
    private boolean initialized = false;
    
    /**
     * Whether or not the registry has been registered yet.
     */
    private boolean registered = false;
    
    public ForgeRegistryHelper(Logger logger, String ownerId, Class<T> type) {
        
        this.logger = logger;
        this.ownerId = ownerId;
        this.type = type;
        this.valueSuppliers = NonNullList.create();
        this.values = NonNullList.create();
        this.registerListeners = NonNullList.create();
        this.registryListeners = NonNullList.create();
    }
    
    /**
     * Initializes the registry helper with the event bus. This should only be invoked after
     * all the values have been registered.
     *
     * @param bus The event bus to register with.
     * @return Whether or not the event bus initialized.
     */
    public boolean initialize (IEventBus bus) {
        
        this.initialized = true;
        
        if (!this.valueSuppliers.isEmpty()) {
            
            bus.addGenericListener(this.type, this::injectRegistryEntries);
            this.logger.debug("Successfully initialized {} registry helper with {} entries.", this.type.getSimpleName(), this.valueSuppliers.size());
            return true;
        }
        
        else {
            
            this.logger.debug("Skipping initialization of {} registry helper. No values detected.", this.type.getSimpleName());
            return false;
        }
    }
    
    /**
     * Registers a value and applies a registry name.
     *
     * @param value The value to register.
     * @param identifier The identifier to use. Namespace is taken from {@link #ownerId}.
     * @return The registered value.
     */
    public T register (T value, String identifier) {
        
        value.setRegistryName(new ResourceLocation(this.ownerId, identifier));
        return this.register(value);
    }
    
    /**
     * Registers a value. Values must have a registry name applied beforehand.
     *
     * @param value The value to register.
     * @return The registered value.
     */
    public T register (T value) {
        
        if (value.getRegistryName() == null) {
            
            throw new IllegalStateException("Attempted to register " + this.type.getSimpleName() + " without registry name: " + value.toString());
        }
        
        this.register( () -> value);
        this.registerListeners.forEach(listener -> listener.accept(this, value));
        return value;
    }
    
    /**
     * Registers a value supplier which will be resolved during the inject stage. This method
     * bypasses some of the safety checks and {@link #registerListeners} so extra caution
     * should be used.
     *
     * @param value The value supplier to register.
     */
    public void register (Supplier<T> value) {
        
        if (this.hasInitialized()) {
            
            throw new IllegalStateException("The " + this.type.getSimpleName() + " registry helper has already been initialized. New values can not be added.");
        }
        
        this.valueSuppliers.add(value);
    }
    
    /**
     * Gets all the registered values. Can only be used after values have been injected.
     *
     * @return A collection of registered values.
     */
    public Collection<T> getValues () {
        
        if (!this.hasRegistered()) {
            
            throw new IllegalStateException("A mod tried to access the values of a registry before they had been registered!");
        }
        
        return Collections.unmodifiableCollection(this.values);
    }
    
    /**
     * Checks if the registry helper has been initialized. You can not add entries to an
     * initialized registry helper.
     *
     * @return Whether or not the registry helper has been initialized.
     */
    public boolean hasInitialized () {
        
        return this.initialized;
    }
    
    /**
     * Checks if the registry helper has been registered yet.
     *
     * @return Whether or not this registry has already been registered.
     */
    public boolean hasRegistered () {
        
        return this.registered;
    }
    
    public void addRegisterListener (BiConsumer<ForgeRegistryHelper<T>, T> listener) {
        
        this.registerListeners.add(listener);
    }
    
    public void addRegistryListener (BiConsumer<ForgeRegistryHelper<T>, IForgeRegistry<T>> listener) {
        
        this.registryListeners.add(listener);
    }
    
    /**
     * Handles injecting registry values into the forge registry. Will also invoke the
     * {@link #registryListeners}.
     *
     * @param event The forge register event.
     */
    private void injectRegistryEntries (Register<T> event) {
        
        this.values.clear();
        this.registered = false;
        
        final IForgeRegistry<T> registry = event.getRegistry();
        final long startTime = System.nanoTime();
        
        for (final Supplier<T> supplier : this.valueSuppliers) {
            
            final T value = supplier.get();
            
            if (value != null) {
                
                registry.register(value);
                this.logger.debug("Registered {} entry \"{}\" as type {}.", this.type.getSimpleName(), value.getRegistryName(), value.getClass().getName());
                this.values.add(value);
            }
        }
        
        final long endTime = System.nanoTime();
        
        this.registryListeners.forEach(listener -> listener.accept(this, registry));
        
        this.logger.info("Registered {} {} entries in {}ms.", this.values.size(), this.type.getSimpleName(), MathsUtils.DECIMAL_2.format((endTime - startTime) / 1000000f));
        
        this.registered = true;
    }
}