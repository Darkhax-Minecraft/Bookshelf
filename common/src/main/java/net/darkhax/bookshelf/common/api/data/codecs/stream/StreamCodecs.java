package net.darkhax.bookshelf.common.api.data.codecs.stream;

import io.netty.buffer.ByteBuf;
import net.darkhax.bookshelf.common.impl.data.ingredient.FalseIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class StreamCodecs {

    public static final StreamCodec<RegistryFriendlyByteBuf, String> STRING = StreamCodec.of(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> ITEM_STACK_LIST = list(ItemStack.STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Ingredient> INGREDIENT_NON_EMPTY = StreamCodec.of(
            Ingredient.CONTENTS_STREAM_CODEC,
            buf -> {
                final Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                return ingredient.isEmpty() ? FalseIngredient.INSTANCE.get() : ingredient;
            }
    );

    public static <T extends Enum<T>> StreamCodec<RegistryFriendlyByteBuf, T> enumerable(Class<T> enumClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(enumClass));
    }

    public static <T extends Enum<T>> StreamCodec<RegistryFriendlyByteBuf, EnumSet<T>> enumSet(Class<T> enumClass) {
        return StreamCodec.of((b, v) -> b.writeEnumSet(v, enumClass), buf -> buf.readEnumSet(enumClass));
    }

    public static <B extends ByteBuf, V> StreamCodec<B, List<V>> list(StreamCodec<B, V> baseCodec) {
        return StreamCodec.of(
                (buf, val) -> {
                    buf.writeInt(val.size());
                    for (V entry : val) {
                        baseCodec.encode(buf, entry);
                    }
                },
                buf -> {
                    final int size = buf.readInt();
                    final List<V> list = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        list.add(baseCodec.decode(buf));
                    }
                    return list;
                }
        );
    }
}