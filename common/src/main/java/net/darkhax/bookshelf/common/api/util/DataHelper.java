package net.darkhax.bookshelf.common.api.util;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class DataHelper {

    public static <T> HolderSet<T> getTagOrEmpty(@Nullable HolderLookup.Provider provider, ResourceKey<Registry<T>> registryKey, TagKey<T> tag) {
        if (provider != null) {
            final Optional<HolderSet.Named<T>> optional = provider.lookupOrThrow(registryKey).get(tag);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return HolderSet.direct();
    }

    /**
     * Creates a sublist of a ListTag. Unlike {@link java.util.AbstractList#subList(int, int)}, the sublist is a new
     * list instance and changes to the original will not be propagated.
     *
     * @param list The list to create a sublist from.
     * @param from The starting index.
     * @param to   The ending index.
     * @return A sublist created from the input list.
     */
    public static ListTag subList(ListTag list, int from, int to) {
        if (list == null) {
            throw new IllegalStateException("The input list must not be null!");
        }
        if (from < 0 || to > list.size() || from > to) {
            throw new IndexOutOfBoundsException("Invalid range! from=" + from + " to=" + to + " size=" + list.size());
        }
        final ListTag subList = new ListTag();
        for (int i = from; i < to; i++) {
            subList.add(list.get(i));
        }
        return subList;
    }

    /**
     * Creates a sublist of an inventory tag based on a predicate on the slot indexes.
     *
     * @param list  The inventory list tag.
     * @param slots A predicate for which item slots should be included in the sublist.
     * @return A sublist created from the input list.
     */
    public static ListTag containerSubList(ListTag list, Predicate<Integer> slots) {
        if (list == null) {
            throw new IllegalStateException("The input list must not be null!");
        }
        final ListTag subList = new ListTag();
        for (Tag tag : list) {
            if (tag instanceof CompoundTag entry && entry.contains("Slot", Tag.TAG_BYTE) && slots.test(entry.getInt("Slot"))) {
                subList.add(tag);
            }
        }
        return subList;
    }

    /**
     * Creates a new stream codec for an optional value.
     *
     * @param streamCodec A codec that can serialize the content type.
     * @param <B>         The type of the byte buffer.
     * @param <V>         The content type of the stream.
     * @return An optional stream codec.
     */
    public static <B extends ByteBuf, V> StreamCodec<B, Optional<V>> optionalStream(StreamCodec<B, V> streamCodec) {
        return StreamCodec.of(
                (buf, val) -> {
                    buf.writeBoolean(val.isPresent());
                    val.ifPresent(v -> streamCodec.encode(buf, v));
                },
                buf -> {
                    if (buf.readBoolean()) {
                        final V val = streamCodec.decode(buf);
                        return Optional.of(val);
                    }
                    return Optional.empty();
                });
    }

    /**
     * Creates a new recipe serializer.
     *
     * @param codec  A codec for JSON/NBT data.
     * @param stream A codec for networking.
     * @param <T>    The type of the recipe.
     * @return A recipe serializer object.
     */
    public static <T extends Recipe<?>> RecipeSerializer<T> recipeSerializer(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> stream) {
        return new RecipeSerializer<>() {
            @NotNull
            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @NotNull
            @Override
            public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
                return stream;
            }
        };
    }
}
