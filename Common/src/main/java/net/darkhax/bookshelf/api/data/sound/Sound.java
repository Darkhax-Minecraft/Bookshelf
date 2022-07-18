package net.darkhax.bookshelf.api.data.sound;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.api.serialization.ISerializer;
import net.darkhax.bookshelf.api.serialization.NBTParseException;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Sound implements ISound {

    private final SoundEvent sound;
    private final SoundSource category;
    private final float pitch;
    private final float volume;

    @Override
    public boolean equals(Object other) {

        if (this == other) {

            return true;
        }

        if (other == null || this.getClass() != other.getClass()) {

            return false;
        }

        Sound sound1 = (Sound) other;
        return Float.compare(sound1.pitch, pitch) == 0 && Float.compare(sound1.volume, volume) == 0 && sound.equals(sound1.sound) && category == sound1.category;
    }

    @Override
    public int hashCode() {
        
        return Objects.hash(sound, category, pitch, volume);
    }

    public Sound(SoundEvent sound, SoundSource category, float pitch, float volume) {

        this.sound = sound;
        this.category = category;
        this.pitch = pitch;
        this.volume = volume;
    }

    @Override
    public void playSoundAt(Level level, @Nullable Player originator, double posX, double posY, double posZ) {

        level.playSound(originator, posX, posY, posZ, sound, this.category, this.volume, this.pitch);
    }

    public static class Serializer implements ISerializer<Sound> {

        @Override
        public Sound fromJSON(JsonElement json) {

            if (json instanceof JsonObject obj) {

                final SoundEvent sound = Serializers.SOUND_EVENT.fromJSON(obj, "sound");
                final SoundSource category = Serializers.SOUND_CATEGORY.fromJSON(obj, "category", SoundSource.MASTER);
                final float pitch = Serializers.FLOAT.fromJSON(obj, "pitch", 1f);
                final float volume = Serializers.FLOAT.fromJSON(obj, "volume", 1f);

                if (pitch < 0 || pitch > 1) {

                    throw new JsonParseException("Pitch must be between 0 and 1. Read " + pitch);
                }

                if (volume < 0 || volume > 1) {

                    throw new JsonParseException("Volume must be between 0 and 1. Read " + volume);
                }

                return new Sound(sound, category, pitch, volume);
            }

            final SoundEvent sound = Serializers.SOUND_EVENT.fromJSON(json);
            return new Sound(sound, SoundSource.MASTER, 1f, 1f);
        }

        @Override
        public JsonElement toJSON(Sound toWrite) {

            final JsonObject obj = new JsonObject();
            obj.add("sound", Serializers.SOUND_EVENT.toJSON(toWrite.sound));
            obj.add("category", Serializers.SOUND_CATEGORY.toJSON(toWrite.category));
            obj.add("pitch", Serializers.FLOAT.toJSON(toWrite.pitch));
            obj.add("volume", Serializers.FLOAT.toJSON(toWrite.volume));
            return obj;
        }

        @Override
        public Sound fromByteBuf(FriendlyByteBuf buffer) {

            final SoundEvent sound = Serializers.SOUND_EVENT.fromByteBuf(buffer);
            final SoundSource category = Serializers.SOUND_CATEGORY.fromByteBuf(buffer);
            final float pitch = Serializers.FLOAT.fromByteBuf(buffer);
            final float volume = Serializers.FLOAT.fromByteBuf(buffer);
            return new Sound(sound, category, pitch, volume);
        }

        @Override
        public void toByteBuf(FriendlyByteBuf buffer, Sound toWrite) {

            Serializers.SOUND_EVENT.toByteBuf(buffer, toWrite.sound);
            Serializers.SOUND_CATEGORY.toByteBuf(buffer, toWrite.category);
            Serializers.FLOAT.toByteBuf(buffer, toWrite.pitch);
            Serializers.FLOAT.toByteBuf(buffer, toWrite.volume);
        }

        @Override
        public Tag toNBT(Sound toWrite) {

            final CompoundTag tag = new CompoundTag();
            tag.put("sound", Serializers.SOUND_EVENT.toNBT(toWrite.sound));
            tag.put("category", Serializers.SOUND_CATEGORY.toNBT(toWrite.category));
            tag.put("pitch", Serializers.FLOAT.toNBT(toWrite.pitch));
            tag.put("volume", Serializers.FLOAT.toNBT(toWrite.volume));
            return tag;
        }

        @Override
        public Sound fromNBT(Tag nbt) {

            if (nbt instanceof CompoundTag tag) {

                final SoundEvent sound = Serializers.SOUND_EVENT.fromNBT(tag, "sound");
                final SoundSource category = Serializers.SOUND_CATEGORY.fromNBT(tag, "category", SoundSource.MASTER);
                final float pitch = Serializers.FLOAT.fromNBT(tag, "pitch", 1f);
                final float volume = Serializers.FLOAT.fromNBT(tag, "volume", 1f);

                if (pitch < 0 || pitch > 1) {

                    throw new NBTParseException("Pitch must be between 0 and 1. Read " + pitch);
                }

                if (volume < 0 || volume > 1) {

                    throw new NBTParseException("Volume must be between 0 and 1. Read " + volume);
                }

                return new Sound(sound, category, pitch, volume);
            }

            final SoundEvent sound = Serializers.SOUND_EVENT.fromNBT(nbt);
            return new Sound(sound, SoundSource.MASTER, 1f, 1f);
        }
    }
}