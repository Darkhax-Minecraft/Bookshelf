package net.darkhax.bookshelf.crafting.block;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockIngredient implements Predicate<BlockState> {

    /**
     * An internal map that holds all registered serializers for block ingredients. See
     * {@link #register(IBlockIngredientSerializer, ResourceLocation)} and
     * {@link #getSerializer(ResourceLocation)} for access and additional information.
     */
    private static final Map<ResourceLocation, IBlockIngredientSerializer<?>> serializers = new ConcurrentHashMap<>();

    /**
     * Registers a new block ingredient serializer. This will allow users to use the serializer
     * in user-facing systems.
     *
     * @param serializer The serializer to register.
     * @param id The ID to register the serializer with.
     */
    public static void register (IBlockIngredientSerializer<?> serializer, ResourceLocation id) {

        serializers.put(id, serializer);
    }

    /**
     * Gets an ingredient serializer by it's ID name.
     *
     * @param id The ID to lookup.
     * @return The serializer that was found. This may be null.
     */
    @Nullable
    public static IBlockIngredientSerializer<?> getSerializer (ResourceLocation id) {

        return serializers.get(id);
    }

    /**
     * Gets an immutable collection of all valid block states for this ingredient. This is
     * primarily used for display purposes.
     *
     * @return A collection of all valid states.
     */
    public abstract Collection<BlockState> getValidStates ();

    /**
     * Gets an array of all the valid block states.
     *
     * @return An array of valid block states.
     */
    public BlockState[] getValidStatesArray () {

        return this.getValidStates().toArray(new BlockState[0]);
    }

    /**
     * Gets the ID of the serializer used to read/write this ingredient.
     *
     * @return The ID of the serializer used to read/write this ingredient.
     */
    public abstract ResourceLocation getSerializeId ();
}