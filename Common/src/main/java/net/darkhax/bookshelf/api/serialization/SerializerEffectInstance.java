package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.mixin.accessors.effect.AccessorMobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

public class SerializerEffectInstance implements ISerializer<MobEffectInstance> {

    @Override
    public MobEffectInstance fromJSON(JsonElement json) {

        if (json instanceof JsonObject obj) {

            final MobEffect effect = Serializers.MOB_EFFECT.fromJSON(obj, "effect");
            final int duration = Serializers.INT.fromJSON(obj, "duration");
            final int amplifier = Serializers.INT.fromJSON(obj, "amplifier", 0);
            final boolean ambient = Serializers.BOOLEAN.fromJSON(obj, "ambient", false);
            final boolean visible = Serializers.BOOLEAN.fromJSON(obj, "visible", true);
            final boolean showIcon = Serializers.BOOLEAN.fromJSON(obj, "showIcon", true);
            final MobEffectInstance hiddenEffect = Serializers.EFFECT_INSTANCE.fromJSON(obj, "hiddenEffect", (MobEffectInstance) null);

            // TODO Add serializer support for FactorData.
            return new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect, effect.createFactorData());
        }

        throw new JsonParseException("Expected JSON object.");
    }

    @Override
    public JsonElement toJSON(MobEffectInstance toWrite) {

        final JsonObject json = new JsonObject();
        Serializers.MOB_EFFECT.toJSON(json, "effect", toWrite.getEffect());
        Serializers.INT.toJSON(json, "duration", toWrite.getDuration());

        if (toWrite.getAmplifier() != 0) {
            Serializers.INT.toJSON(json, "amplifier", toWrite.getAmplifier());
        }

        if (toWrite.isAmbient()) {
            Serializers.BOOLEAN.toJSON(json, "ambient", toWrite.isAmbient());
        }

        if (!toWrite.isVisible()) {
            Serializers.BOOLEAN.toJSON(json, "visible", toWrite.isVisible());
        }

        if (!toWrite.showIcon()) {
            Serializers.BOOLEAN.toJSON(json, "showIcon", toWrite.showIcon());
        }

        if (((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect() != null) {
            Serializers.EFFECT_INSTANCE.toJSON(json, "hiddenEffect", ((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect());
        }

        return json;
    }

    @Override
    public MobEffectInstance fromByteBuf(FriendlyByteBuf buffer) {

        final MobEffect effect = Serializers.MOB_EFFECT.fromByteBuf(buffer);
        final int duration = Serializers.INT.fromByteBuf(buffer);
        final int amplifier = Serializers.INT.fromByteBuf(buffer);
        final boolean ambient = Serializers.BOOLEAN.fromByteBuf(buffer);
        final boolean visible = Serializers.BOOLEAN.fromByteBuf(buffer);
        final boolean showIcon = Serializers.BOOLEAN.fromByteBuf(buffer);
        final MobEffectInstance hiddenEffect = Serializers.EFFECT_INSTANCE.fromByteBufOptional(buffer).orElse(null);

        // TODO add factor data support.
        return new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect, effect.createFactorData());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, MobEffectInstance toWrite) {

        Serializers.MOB_EFFECT.toByteBuf(buffer, toWrite.getEffect());
        Serializers.INT.toByteBuf(buffer, toWrite.getDuration());
        Serializers.INT.toByteBuf(buffer, toWrite.getAmplifier());
        Serializers.BOOLEAN.toByteBuf(buffer, toWrite.isAmbient());
        Serializers.BOOLEAN.toByteBuf(buffer, toWrite.isVisible());
        Serializers.BOOLEAN.toByteBuf(buffer, toWrite.showIcon());
        Serializers.EFFECT_INSTANCE.toByteBufOptional(buffer, Optional.ofNullable(((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect()));
    }

    @Override
    public Tag toNBT(MobEffectInstance toWrite) {

        final CompoundTag tag = new CompoundTag();
        Serializers.MOB_EFFECT.toNBT(tag, "effect", toWrite.getEffect());
        Serializers.INT.toNBT(tag, "duration", toWrite.getDuration());

        if (toWrite.getAmplifier() != 0) {
            Serializers.INT.toNBT(tag, "amplifier", toWrite.getAmplifier());
        }

        if (toWrite.isAmbient()) {
            Serializers.BOOLEAN.toNBT(tag, "ambient", toWrite.isAmbient());
        }

        if (!toWrite.isVisible()) {
            Serializers.BOOLEAN.toNBT(tag, "visible", toWrite.isVisible());
        }

        if (!toWrite.showIcon()) {
            Serializers.BOOLEAN.toNBT(tag, "showIcon", toWrite.showIcon());
        }

        if (((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect() != null) {
            Serializers.EFFECT_INSTANCE.toNBT(tag, "hiddenEffect", ((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect());
        }
        return tag;
    }

    @Override
    public MobEffectInstance fromNBT(Tag nbt) {

        final CompoundTag tag = Serializers.COMPOUND_TAG.fromNBT(nbt);

        final MobEffect effect = Serializers.MOB_EFFECT.fromNBT(tag, "effect");
        final int duration = Serializers.INT.fromNBT(tag, "duration");
        final int amplifier = Serializers.INT.fromNBT(tag, "amplifier", 0);
        final boolean ambient = Serializers.BOOLEAN.fromNBT(tag, "ambient", false);
        final boolean visible = Serializers.BOOLEAN.fromNBT(tag, "visible", true);
        final boolean showIcon = Serializers.BOOLEAN.fromNBT(tag, "showIcon", true);
        final MobEffectInstance hiddenEffect = Serializers.EFFECT_INSTANCE.fromNBT(tag, "hiddenEffect", null);

        // TODO add factor data.
        return new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect, effect.createFactorData());
    }
}
