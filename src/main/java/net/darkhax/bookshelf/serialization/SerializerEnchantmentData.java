package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerEnchantmentData implements ISerializer<EnchantmentData> {
    
    public static final ISerializer<EnchantmentData> SERIALIZER = new SerializerEnchantmentData();
    
    private SerializerEnchantmentData() {
        
    }
    
    @Override
    public EnchantmentData read (JsonElement json) {
        
        if (json.isJsonObject()) {
            
            final JsonObject obj = json.getAsJsonObject();
            final Enchantment enchant = Serializers.ENCHANTMENT.read(obj.get("enchantment"));
            final int level = JSONUtils.getInt(obj, "level");
            return new EnchantmentData(enchant, level);
        }
        
        throw new JsonParseException("Expected enchantment data to be a JSON object.");
    }
    
    @Override
    public JsonElement write (EnchantmentData toWrite) {
        
        final JsonObject json = new JsonObject();
        json.add("enchantment", Serializers.ENCHANTMENT.write(toWrite.enchantment));
        json.addProperty("level", toWrite.enchantmentLevel);
        return json;
    }
    
    @Override
    public EnchantmentData read (PacketBuffer buffer) {
        
        final Enchantment enchant = Serializers.ENCHANTMENT.read(buffer);
        final int level = buffer.readInt();
        return new EnchantmentData(enchant, level);
    }
    
    @Override
    public void write (PacketBuffer buffer, EnchantmentData toWrite) {
        
        Serializers.ENCHANTMENT.write(buffer, toWrite.enchantment);
        buffer.writeInt(toWrite.enchantmentLevel);
    }
}