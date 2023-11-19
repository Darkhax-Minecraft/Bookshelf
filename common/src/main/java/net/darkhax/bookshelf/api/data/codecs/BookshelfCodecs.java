package net.darkhax.bookshelf.api.data.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Optionull;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.lang3.EnumUtils;

import java.util.Locale;
import java.util.function.Function;

public class BookshelfCodecs {

    public static Codec<SoundSource> SOUND_SOURCE = enumCodec(SoundSource.class);

    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> enumClass) {

        final Function<String, T> fromString = name -> {

            T value = EnumUtils.getEnum(enumClass, name);

            if (value == null) {

                value = EnumUtils.getEnum(enumClass, name.toUpperCase(Locale.ROOT));
            }

            return value;
        };

        return Codec.STRING.flatXmap(string -> Optionull.mapOrElse(fromString.apply(string), DataResult::success, () -> DataResult.error(() -> "bad")), object -> DataResult.success(object.name()));
    }
}