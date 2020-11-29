package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.crafting.block.BlockIngredient;
import net.darkhax.bookshelf.crafting.block.IBlockIngredientSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public final class BlockIngredientSerializer implements ISerializer<BlockIngredient> {
    
    public static final ISerializer<BlockIngredient> SERIALIZER = new BlockIngredientSerializer();
    
    private BlockIngredientSerializer() {
        
    }
    
    @Override
    public BlockIngredient read (JsonElement json) {
        
        final JsonObject obj = json.getAsJsonObject();
        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(obj.get("type"));
        return BlockIngredient.getSerializer(id).read(obj);
    }
    
    @Override
    public BlockIngredient read (PacketBuffer buf) {
        
        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(buf);
        return BlockIngredient.getSerializer(id).read(buf);
    }
    
    @Override
    public JsonElement write (BlockIngredient ingredient) {
        
        final JsonElement element = this.writeUnsafe(ingredient);
        
        if (element.isJsonObject()) {
            
            final JsonObject obj = element.getAsJsonObject();
            obj.add("type", Serializers.RESOURCE_LOCATION.write(ingredient.getId()));
        }
        
        else {
            
            throw new IllegalStateException("Ingredient serializer " + ingredient.getId() + " returned " + element.getClass().getSimpleName() + " but expected " + JsonObject.class.getSimpleName() + ". This is a mod issue that needs to be fixed.");
        }
        
        return element;
    }
    
    @Override
    public void write (PacketBuffer buf, BlockIngredient ingredient) {
        
        Serializers.RESOURCE_LOCATION.write(buf, ingredient.getId());
        this.writeUnsafe(buf, ingredient);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends BlockIngredient> JsonElement writeUnsafe (T ingredient) {
        
        final IBlockIngredientSerializer<T> serializer = (IBlockIngredientSerializer<T>) BlockIngredient.getSerializer(ingredient.getId());
        return serializer.write(ingredient);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends BlockIngredient> void writeUnsafe (PacketBuffer buf, T ingredient) {
        
        final IBlockIngredientSerializer<T> serializer = (IBlockIngredientSerializer<T>) BlockIngredient.getSerializer(ingredient.getId());
        serializer.write(buf, ingredient);
    }
}