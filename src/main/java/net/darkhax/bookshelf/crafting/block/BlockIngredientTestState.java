package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;

public class BlockIngredientTestState extends BlockIngredient {
    
    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "test_state");
    public static final Serializer SERIALIZER = new Serializer();
    
    private final List<BlockState> validStates;
    private final Map<Property<?>, Object> props;
    
    public BlockIngredientTestState(List<BlockState> validStates) {
        
        this(validStates, new HashMap<>());
    }
    
    public BlockIngredientTestState(List<BlockState> validStates, Map<Property<?>, Object> props) {
        
        this.validStates = validStates;
        this.props = props;
    }
    
    @Override
    public boolean test (BlockState t) {
        
        return this.validStates.contains(t);
    }
    
    @Override
    public Collection<BlockState> getValidStates () {
        
        return this.validStates;
    }
    
    @Override
    public ResourceLocation getSerializeId () {
        
        return ID;
    }
    
    static class Serializer implements IBlockIngredientSerializer<BlockIngredientTestState> {
        
        @Override
        public BlockIngredientTestState read (JsonElement json) {
            
            final Block block = Serializers.BLOCK.read(json.getAsJsonObject(), "block");
            final JsonObject properties = json.getAsJsonObject().getAsJsonObject("properties");
            
            final List<BlockState> validStates = new ArrayList<>();
            final Map<Property<?>, Object> expectedProps = new HashMap<>();
            
            for (final Entry<String, JsonElement> propValue : properties.entrySet()) {
                
                final Property<?> property = block.getStateDefinition().getProperty(propValue.getKey());
                
                if (property != null) {
                    
                    final Optional<?> value = property.getValue(propValue.getValue().getAsString());
                    
                    if (value.isPresent()) {
                        
                        expectedProps.put(property, value.get());
                    }
                    
                    else {
                        
                        throw new JsonParseException("Could not resolve prop " + propValue.getKey() + " with value " + propValue.getValue().getAsString() + " for block " + block.getRegistryName());
                    }
                }
                
                else {
                    
                    throw new JsonParseException("Block " + block.getRegistryName() + " has no property " + propValue.getKey());
                }
            }
            
            for (final BlockState state : block.getStateDefinition().getPossibleStates()) {
                
                if (expectedProps.entrySet().stream().allMatch(e -> state.getValue(e.getKey()).equals(e.getValue()))) {
                    
                    validStates.add(state);
                }
            }
            
            return new BlockIngredientTestState(validStates, expectedProps);
        }
        
        @Override
        public JsonElement write (BlockIngredientTestState ingredient) {
            
            final JsonObject obj = new JsonObject();
            obj.add("block", Serializers.BLOCK.write(ingredient.validStates.get(0).getBlock()));
            
            final JsonObject props = new JsonObject();
            obj.add("properties", props);
            
            for (final Entry<Property<?>, Object> propEntry : ingredient.props.entrySet()) {
                
                props.addProperty(propEntry.getKey().getName(), propEntry.getValue().toString());
            }
            
            return obj;
        }
        
        @Override
        public BlockIngredientTestState read (PacketBuffer buf) {
            
            final List<BlockState> states = Serializers.BLOCK_STATE.readList(buf);
            return new BlockIngredientTestState(states);
        }
        
        @Override
        public void write (PacketBuffer buf, BlockIngredientTestState ingredient) {
            
            Serializers.BLOCK_STATE.writeList(buf, ingredient.validStates);
        }
    }
}