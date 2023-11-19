package net.darkhax.bookshelf.api.data.sound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Sound implements ISound {

    public static Codec<Sound> CODEC_SIMPLE = BuiltInRegistries.SOUND_EVENT.byNameCodec().xmap(Sound::new, Sound::getSound);
    public static Codec<Sound> CODEC_OBJECT = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound").forGetter(Sound::getSound), BookshelfCodecs.SOUND_SOURCE.optionalFieldOf("category", SoundSource.MASTER).forGetter(Sound::getCategory), Codec.FLOAT.optionalFieldOf("pitch", 1f).forGetter(Sound::getPitch), Codec.FLOAT.optionalFieldOf("volume", 1f).forGetter(Sound::getVolume)).apply(instance, Sound::new));
    public static Codec<Sound> CODEC = ExtraCodecs.withAlternative(CODEC_SIMPLE, CODEC_OBJECT);

    private final SoundEvent sound;
    private final SoundSource category;
    private final float pitch;
    private final float volume;

    public Sound(SoundEvent sound) {

        this(sound, SoundSource.MASTER, 1f, 1f);
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

    public SoundEvent getSound() {

        return sound;
    }

    public SoundSource getCategory() {

        return category;
    }

    public float getPitch() {

        return pitch;
    }

    public float getVolume() {

        return volume;
    }

    public static Sound fromByteBuf(FriendlyByteBuf buffer) {

        final SoundEvent sound = Serializers.SOUND_EVENT.fromByteBuf(buffer);
        final SoundSource category = Serializers.SOUND_CATEGORY.fromByteBuf(buffer);
        final float pitch = Serializers.FLOAT.fromByteBuf(buffer);
        final float volume = Serializers.FLOAT.fromByteBuf(buffer);
        return new Sound(sound, category, pitch, volume);
    }

    public static void toByteBuf(FriendlyByteBuf buffer, Sound toWrite) {

        Serializers.SOUND_EVENT.toByteBuf(buffer, toWrite.sound);
        Serializers.SOUND_CATEGORY.toByteBuf(buffer, toWrite.category);
        Serializers.FLOAT.toByteBuf(buffer, toWrite.pitch);
        Serializers.FLOAT.toByteBuf(buffer, toWrite.volume);
    }
}