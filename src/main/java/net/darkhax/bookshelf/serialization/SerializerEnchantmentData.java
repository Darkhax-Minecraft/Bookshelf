package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerEnchantmentData implements ISerializer<EnchantmentData> {

    public static final ISerializer<EnchantmentData> SERIALIZER = new SerializerEnchantmentData();

    private SerializerEnchantmentData () {

    }

    @Override
    public EnchantmentData read (JsonElement json) {

        if (json.isJsonObject()) {

            final JsonObject obj = json.getAsJsonObject();
            final Enchantment enchant = Serializers.ENCHANTMENT.read(obj.get("enchantment"));
            final int level = JSONUtils.getAsInt(obj, "level");
            return new EnchantmentData(enchant, level);
        }

        throw new JsonParseException("Expected enchantment data to be a JSON object.");
    }

    @Override
    public JsonElement write (EnchantmentData toWrite) {

        final JsonObject json = new JsonObject();
        json.add("enchantment", Serializers.ENCHANTMENT.write(toWrite.enchantment));
        json.addProperty("level", toWrite.level);
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
        buffer.writeInt(toWrite.level);
    }

    @Override
    public INBT writeNBT (EnchantmentData toWrite) {

        final CompoundNBT tag = new CompoundNBT();
        tag.put("enchantment", Serializers.ENCHANTMENT.writeNBT(toWrite.enchantment));
        tag.put("level", Serializers.INT.writeNBT(toWrite.level));
        return tag;
    }

    @Override
    public EnchantmentData read (INBT nbt) {

        if (nbt instanceof CompoundNBT) {

            final CompoundNBT tag = (CompoundNBT) nbt;
            final Enchantment ench = Serializers.ENCHANTMENT.read(tag.get("enchantment"));
            final int level = Serializers.INT.read(tag.get("level"));
            return new EnchantmentData(ench, level);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}