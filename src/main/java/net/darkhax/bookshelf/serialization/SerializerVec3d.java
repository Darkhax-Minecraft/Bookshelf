package net.darkhax.bookshelf.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;

public class SerializerVec3d implements ISerializer<Vector3d> {
    
    public static final ISerializer<Vector3d> SERIALIZER = new SerializerVec3d();
    
    @Override
    public Vector3d read (JsonElement json) {
        
        final List<Double> values = Serializers.DOUBLE.readList(json);
        
        if (values.size() == 3) {
            
            return new Vector3d(values.get(0), values.get(1), values.get(2));
        }
        
        throw new JsonParseException("Expected 3 elements, had " + values.size() + " instead.");
    }
    
    @Override
    public JsonElement write (Vector3d toWrite) {
        
        return Serializers.DOUBLE.writeList(new ArrayList<>(Arrays.asList(toWrite.x, toWrite.y, toWrite.z)));
    }
    
    @Override
    public Vector3d read (PacketBuffer buffer) {
        
        final List<Double> values = Serializers.DOUBLE.readList(buffer);
        
        if (values.size() == 3) {
            
            return new Vector3d(values.get(0), values.get(1), values.get(2));
        }
        
        throw new IllegalStateException("Expected 3 elements, had " + values.size() + " instead.");
    }
    
    @Override
    public void write (PacketBuffer buffer, Vector3d toWrite) {
        
        Serializers.DOUBLE.writeList(buffer, new ArrayList<>(Arrays.asList(toWrite.x, toWrite.y, toWrite.z)));
    }
}