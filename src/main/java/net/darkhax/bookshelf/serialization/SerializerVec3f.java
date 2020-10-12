package net.darkhax.bookshelf.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3f;

public class SerializerVec3f implements ISerializer<Vector3f> {
    
    public static final ISerializer<Vector3f> SERIALIZER = new SerializerVec3f();
    
    @Override
    public Vector3f read (JsonElement json) {
        
        final List<Float> values = Serializers.FLOAT.readList(json);
        
        if (values.size() == 3) {
            
            return new Vector3f(values.get(0), values.get(1), values.get(2));
        }
        
        throw new JsonParseException("Expected 3 elements, had " + values.size() + " instead.");
    }
    
    @Override
    public JsonElement write (Vector3f toWrite) {
        
        return Serializers.FLOAT.writeList(new ArrayList<>(Arrays.asList(toWrite.getX(), toWrite.getY(), toWrite.getZ())));
    }
    
    @Override
    public Vector3f read (PacketBuffer buffer) {
        
        final List<Float> values = Serializers.FLOAT.readList(buffer);
        
        if (values.size() == 3) {
            
            return new Vector3f(values.get(0), values.get(1), values.get(2));
        }
        
        throw new IllegalStateException("Expected 3 elements, had " + values.size() + " instead.");
    }
    
    @Override
    public void write (PacketBuffer buffer, Vector3f toWrite) {
        
        Serializers.FLOAT.writeList(buffer, new ArrayList<>(Arrays.asList(toWrite.getX(), toWrite.getY(), toWrite.getZ())));
    }
}