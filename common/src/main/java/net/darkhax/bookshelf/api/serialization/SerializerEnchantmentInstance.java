package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class SerializerEnchantmentInstance implements ISerializer<EnchantmentInstance> {

    public static final ISerializer<EnchantmentInstance> SERIALIZER = new SerializerEnchantmentInstance();

    private SerializerEnchantmentInstance() {

    }

    @Override
    public EnchantmentInstance fromJSON(JsonElement json) {

        if (json instanceof JsonObject obj) {

            final Enchantment enchantment = Serializers.ENCHANTMENT.fromJSON(obj, "enchantment");
            final int level = Serializers.INT.fromJSON(obj, "level");
            return new EnchantmentInstance(enchantment, level);
        }

        throw new JsonParseException("Expected a JSON object.");
    }

    @Override
    public JsonElement toJSON(EnchantmentInstance toWrite) {

        final JsonObject json = new JsonObject();
        Serializers.ENCHANTMENT.toJSON(json, "enchantment", toWrite.enchantment);
        Serializers.INT.toJSON(json, "level", toWrite.level);
        return json;
    }

    @Override
    public EnchantmentInstance fromByteBuf(FriendlyByteBuf buffer) {

        final Enchantment enchantment = Serializers.ENCHANTMENT.fromByteBuf(buffer);
        final int level = Serializers.INT.fromByteBuf(buffer);
        return new EnchantmentInstance(enchantment, level);
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, EnchantmentInstance toWrite) {

        Serializers.ENCHANTMENT.toByteBuf(buffer, toWrite.enchantment);
        Serializers.INT.toByteBuf(buffer, toWrite.level);
    }

    @Override
    public Tag toNBT(EnchantmentInstance toWrite) {

        final CompoundTag tag = new CompoundTag();
        Serializers.ENCHANTMENT.toNBT(tag, "id", toWrite.enchantment);
        Serializers.INT.toNBT(tag, "lvl", toWrite.level);

        return tag;
    }

    @Override
    public EnchantmentInstance fromNBT(Tag nbt) {

        if (nbt instanceof CompoundTag compound) {

            final Enchantment enchantment = Serializers.ENCHANTMENT.fromNBT(compound, "id");
            final int level = Serializers.INT.fromNBT(compound, "lvl");
            return new EnchantmentInstance(enchantment, level);
        }

        throw new NBTParseException("Expected a compound tag. " + nbt);
    }
}
