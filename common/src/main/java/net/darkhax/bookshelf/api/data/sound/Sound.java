package net.darkhax.bookshelf.api.data.sound;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.data.bytebuf.BookshelfByteBufs;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public final class Sound implements ISound {

    public static Codec<Sound> CODEC_SIMPLE = BuiltInRegistries.SOUND_EVENT.byNameCodec().xmap(Sound::new, Sound::getSound);
    public static Codec<Sound> CODEC_OBJECT = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound").forGetter(Sound::getSound),
            BookshelfCodecs.SOUND_SOURCE.get("category", Sound::getCategory, SoundSource.MASTER),
            Codec.FLOAT.optionalFieldOf("pitch", 1f).forGetter(Sound::getPitch),
            Codec.FLOAT.optionalFieldOf("volume", 1f).forGetter(Sound::getVolume)
    ).apply(instance, Sound::new));
    public static Codec<Sound> CODEC = ExtraCodecs.either(CODEC_SIMPLE, CODEC_OBJECT).xmap(either -> either.map(Function.identity(), Function.identity()), value -> value.isSimple() ? Either.left(value) : Either.right(value));

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

    private boolean isSimple() {

        return this.category == SoundSource.MASTER && this.pitch == 1f && this.volume == 1f;
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

    @Override
    public String toString() {
        return "Sound{" + "sound=" + sound.getLocation() + ", category=" + category + ", pitch=" + pitch + ", volume=" + volume + '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (!(o instanceof Sound sound1)) {

            return false;
        }

        return Float.compare(getPitch(), sound1.getPitch()) == 0 && Float.compare(getVolume(), sound1.getVolume()) == 0 && Objects.equals(getSound(), sound1.getSound()) && getCategory() == sound1.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSound(), getCategory(), getPitch(), getVolume());
    }

    public static Sound fromByteBuf(FriendlyByteBuf buffer) {

        final SoundEvent sound = BookshelfByteBufs.SOUND_EVENT.read(buffer);
        final SoundSource category = BookshelfByteBufs.SOUND_SOURCE.read(buffer);
        final float pitch = BookshelfByteBufs.FLOAT.read(buffer);
        final float volume = BookshelfByteBufs.FLOAT.read(buffer);
        return new Sound(sound, category, pitch, volume);
    }

    public static void toByteBuf(FriendlyByteBuf buffer, Sound toWrite) {

        BookshelfByteBufs.SOUND_EVENT.write(buffer, toWrite.sound);
        BookshelfByteBufs.SOUND_SOURCE.write(buffer, toWrite.category);
        BookshelfByteBufs.FLOAT.write(buffer, toWrite.pitch);
        BookshelfByteBufs.FLOAT.write(buffer, toWrite.volume);
    }
}