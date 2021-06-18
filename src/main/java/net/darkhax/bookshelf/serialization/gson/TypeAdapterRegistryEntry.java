package net.darkhax.bookshelf.serialization.gson;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;

/**
 * This type adapter will tell JSON to serialize a registry value using the registry ID rather
 * than trying to construct a new instance of the class.
 *
 * @param <T> The type of object this adapter can serialize.
 */
public class TypeAdapterRegistryEntry<T> extends TypeAdapter<T> {
    
    public static final TypeAdapterRegistryEntry<Block> BLOCK = new TypeAdapterRegistryEntry<>(Registry.BLOCK);
    public static final TypeAdapterRegistryEntry<Item> ITEM = new TypeAdapterRegistryEntry<>(Registry.ITEM);
    public static final TypeAdapterRegistryEntry<BlockEntityType<?>> BLOCK_ENTITY = new TypeAdapterRegistryEntry<>(Registry.BLOCK_ENTITY_TYPE);
    public static final TypeAdapterRegistryEntry<RecipeType<?>> RECIPE_TYPE = new TypeAdapterRegistryEntry<>(Registry.RECIPE_TYPE);
    public static final TypeAdapterRegistryEntry<RecipeSerializer<?>> RECIPE_SERIALIZER = new TypeAdapterRegistryEntry<>(Registry.RECIPE_SERIALIZER);
    public static final TypeAdapterRegistryEntry<EntityType<?>> ENTITY_TYPE = new TypeAdapterRegistryEntry<>(Registry.ENTITY_TYPE);
    public static final TypeAdapterRegistryEntry<PaintingMotive> PAINTING = new TypeAdapterRegistryEntry<>(Registry.PAINTING_MOTIVE);
    public static final TypeAdapterRegistryEntry<Potion> POTION = new TypeAdapterRegistryEntry<>(Registry.POTION);
    public static final TypeAdapterRegistryEntry<Enchantment> ENCHANTMENT = new TypeAdapterRegistryEntry<>(Registry.ENCHANTMENT);
    
    /**
     * Registers the vanilla registry adapters to a GsonBuilder.
     * 
     * @param builder The builder to register the type adapters to.
     * @return The same GsonBuilder passed in.
     */
    public static GsonBuilder registerVanillaTypes (GsonBuilder builder) {
        
        builder.registerTypeAdapter(Block.class, BLOCK);
        builder.registerTypeAdapter(Item.class, ITEM);
        builder.registerTypeAdapter(BlockEntityType.class, BLOCK_ENTITY);
        builder.registerTypeAdapter(RecipeType.class, RECIPE_TYPE);
        builder.registerTypeAdapter(RecipeSerializer.class, RECIPE_SERIALIZER);
        builder.registerTypeAdapter(PaintingMotive.class, PAINTING);
        builder.registerTypeAdapter(Potion.class, POTION);
        builder.registerTypeAdapter(Enchantment.class, ENCHANTMENT);
        return builder;
    }
    
    /**
     * The internal registry used to read/write values.
     */
    private final Registry<T> registry;
    
    public TypeAdapterRegistryEntry(Registry<T> registry) {
        
        this.registry = registry;
    }
    
    @Override
    public void write (JsonWriter out, T value) throws IOException {
        
        out.value(this.registry.getId(value).toString());
    }
    
    @Override
    public T read (JsonReader in) throws IOException {
        
        try {
            
            final Identifier id = new Identifier(in.nextString());
            final T value = this.registry.get(id);
            
            if (value != null) {
                
                return value;
            }
            
            else {
                
                throw new IOException("Could not find value for ID '" + id + "'.");
            }
        }
        
        catch (final InvalidIdentifierException e) {
            
            throw new IOException(e);
        }
    }
}