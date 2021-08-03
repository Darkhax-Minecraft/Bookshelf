package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class SerializerEnchantmentData implements ISerializer<EnchantmentInstance> {

    public static final ISerializer<EnchantmentInstance> SERIALIZER = new SerializerEnchantmentData();

    private SerializerEnchantmentData () {

    }

    @Override
    public EnchantmentInstance read (JsonElement json) {

        if (json.isJsonObject()) {

            final JsonObject obj = json.getAsJsonObject();
            final Enchantment enchant = Serializers.ENCHANTMENT.read(obj.get("enchantment"));
            final int level = GsonHelper.getAsInt(obj, "level");
            return new EnchantmentInstance(enchant, level);
        }

        throw new JsonParseException("Expected enchantment data to be a JSON object.");
    }

    @Override
    public JsonElement write (EnchantmentInstance toWrite) {

        final JsonObject json = new JsonObject();
        json.add("enchantment", Serializers.ENCHANTMENT.write(toWrite.enchantment));
        json.addProperty("level", toWrite.level);
        return json;
    }

    @Override
    public EnchantmentInstance read (FriendlyByteBuf buffer) {

        final Enchantment enchant = Serializers.ENCHANTMENT.read(buffer);
        final int level = buffer.readInt();
        return new EnchantmentInstance(enchant, level);
    }

    @Override
    public void write (FriendlyByteBuf buffer, EnchantmentInstance toWrite) {

        Serializers.ENCHANTMENT.write(buffer, toWrite.enchantment);
        buffer.writeInt(toWrite.level);
    }

    @Override
    public Tag writeNBT (EnchantmentInstance toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.put("enchantment", Serializers.ENCHANTMENT.writeNBT(toWrite.enchantment));
        tag.put("level", Serializers.INT.writeNBT(toWrite.level));
        return tag;
    }

    @Override
    public EnchantmentInstance read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            final CompoundTag tag = (CompoundTag) nbt;
            final Enchantment ench = Serializers.ENCHANTMENT.read(tag.get("enchantment"));
            final int level = Serializers.INT.read(tag.get("level"));
            return new EnchantmentInstance(ench, level);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}