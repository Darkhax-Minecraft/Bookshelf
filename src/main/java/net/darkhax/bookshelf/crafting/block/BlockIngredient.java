package net.darkhax.bookshelf.crafting.block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockIngredient implements Predicate<BlockState> {
    
    private static Map<ResourceLocation, IBlockIngredientSerializer<?>> serializers = new HashMap<>();
    
    public static void register (IBlockIngredientSerializer<?> serializer, ResourceLocation id) {
        
        serializers.put(id, serializer);
    }
    
    public static IBlockIngredientSerializer<?> getSerializer (ResourceLocation id) {
        
        return serializers.get(id);
    }
    
    public abstract boolean isEmpty ();
    
    public abstract Collection<BlockState> getValidStates ();
    
    public BlockState[] getValidStatesArray () {
        
        return this.getValidStates().toArray(new BlockState[0]);
    }
    
    public abstract ResourceLocation getId ();
    
    public abstract void invalidate ();
}