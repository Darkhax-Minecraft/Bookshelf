package net.darkhax.bookshelf.world;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

public class DimensionFactoryFunctional extends DimensionFactory {
    
    /**
     * A factory that is used to create new dimension types.
     */
    private final BiFunction<World, DimensionType, ? extends Dimension> factory;
    
    /**
     * A serializer that allows you to write custom data to the dimension serializer.
     */
    @Nullable
    private final BiConsumer<PacketBuffer, Boolean> serializer;
    
    /**
     * A deserializer that allows you to read custom data to the dimension serializer.
     */
    @Nullable
    private final BiConsumer<PacketBuffer, Boolean> deserializer;
    
    /**
     * A supplier that determines whether or not the dimension has skylight.
     */
    @Nullable
    private final BooleanSupplier skylightSupplier;
    
    /**
     * A supplier for the default configuration data.
     */
    @Nullable
    private final Supplier<PacketBuffer> dataSupplier;
    
    public DimensionFactoryFunctional(BiFunction<World, DimensionType, ? extends Dimension> factory, boolean hasSkyLight) {
        
        this(factory, null, null, () -> hasSkyLight, null);
    }
    
    public DimensionFactoryFunctional(BiFunction<World, DimensionType, ? extends Dimension> factory, @Nullable BiConsumer<PacketBuffer, Boolean> serializer, @Nullable BiConsumer<PacketBuffer, Boolean> deserializer, @Nullable BooleanSupplier skylightSupplier, @Nullable Supplier<PacketBuffer> dataSupplier) {
        
        this.factory = factory;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.skylightSupplier = skylightSupplier;
        this.dataSupplier = dataSupplier;
    }
    
    @Override
    public BiFunction<World, DimensionType, ? extends Dimension> getFactory () {
        
        return this.factory;
    }
    
    @Override
    public void write (PacketBuffer buffer, boolean network) {
        
        if (this.serializer != null) {
            
            this.serializer.accept(buffer, network);
        }
    }
    
    @Override
    public void read (PacketBuffer buffer, boolean network) {
        
        if (this.deserializer != null) {
            
            this.deserializer.accept(buffer, network);
        }
    }
    
    @Override
    public boolean hasSkylight () {
        
        return this.skylightSupplier != null && this.skylightSupplier.getAsBoolean();
    }
    
    @Override
    public PacketBuffer getDefaultData () {
        
        return this.dataSupplier != null ? this.dataSupplier.get() : null;
    }
}