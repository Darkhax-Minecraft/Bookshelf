package net.darkhax.bookshelf.api.data.bytebuf;

import net.darkhax.bookshelf.mixin.accessors.util.random.AccessorWeightedRandomList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Logic for serializing data using a byte buffer.
 *
 * @param <T> The type being serialized.
 */
public class ByteBufHelper<T> {

    private final Function<FriendlyByteBuf, T> fromBytes;
    private final BiConsumer<FriendlyByteBuf, T> toBytes;

    /**
     * A function for creating an array of the codecs type. This allows us to create clean arrays for our codecs without
     * relying on sketchy code.
     */
    private final IntFunction<T[]> arrayBuilder;

    public ByteBufHelper(Function<FriendlyByteBuf, T> reader, BiConsumer<FriendlyByteBuf, T> writer, T... vargs) {

        this.fromBytes = reader;
        this.toBytes = writer;
        this.arrayBuilder = size -> (T[]) Array.newInstance(vargs.getClass().getComponentType(), size);
    }

    /**
     * Reads the data from the buffer directly.
     *
     * @param buffer The buffer to read from.
     * @return The value that was read.
     */
    public T read(FriendlyByteBuf buffer) {

        return this.fromBytes.apply(buffer);
    }

    /**
     * Writes the data to the buffer directly.
     *
     * @param buffer  The buffer to write data to.
     * @param toWrite The value to write to the buffer.
     */
    public void write(FriendlyByteBuf buffer, T toWrite) {

        this.toBytes.accept(buffer, toWrite);
    }

    /**
     * Reads an array of values from the buffer.
     *
     * @param buffer        The buffer to read from.
     * @return An array of values read from the buffer.
     */
    public T[] readArray(FriendlyByteBuf buffer) {

        final int size = buffer.readInt();
        final T[] array = this.arrayBuilder.apply(size);

        for (int i = 0; i < size; i++) {

            array[i] = this.read(buffer);
        }

        return array;
    }

    /**
     * Writes an array of values to the buffer.
     *
     * @param buffer The buffer to read from.
     * @param array  The array of values to write.
     */
    public void writeArray(FriendlyByteBuf buffer, T[] array) {

        buffer.writeInt(array.length);

        for (T value : array) {

            this.write(buffer, value);
        }
    }

    /**
     * Reads a collection of values from the buffer.
     *
     * @param buffer             The buffer to read from.
     * @param collectionSupplier A supplier that provides the collection to populate with values. For example
     *                           ArrayList::new.
     * @param <C>                The type of collection to produce.
     * @return A collection of values read from the buffer.
     */
    public <C extends Collection<T>> C readCollection(FriendlyByteBuf buffer, Supplier<C> collectionSupplier) {

        final int size = buffer.readInt();
        final C elements = collectionSupplier.get();

        for (int i = 0; i < size; i++) {

            elements.add(this.read(buffer));
        }

        return elements;
    }

    /**
     * Writes a collection of values to the buffer.
     *
     * @param buffer     The buffer to write data to.
     * @param collection The collection of values to write.
     */
    public void writeCollection(FriendlyByteBuf buffer, Collection<T> collection) {

        buffer.writeInt(collection.size());
        collection.forEach(entry -> this.write(buffer, entry));
    }

    /**
     * Reads a list of values from the buffer.
     *
     * @param buffer The buffer to read from.
     * @return A list of values read from the buffer.
     */
    public List<T> readList(FriendlyByteBuf buffer) {

        return readCollection(buffer, ArrayList::new);
    }

    /**
     * Writes a list of values to the buffer.
     *
     * @param buffer  The buffer to write data to.
     * @param toWrite The list of values to write.
     */
    public void writeList(FriendlyByteBuf buffer, List<T> toWrite) {

        this.writeCollection(buffer, toWrite);
    }

    /**
     * Reads a set of values from the buffer.
     *
     * @param buffer The buffer to read data from.
     * @return A set of values read from the buffer.
     */
    public Set<T> readSet(FriendlyByteBuf buffer) {

        return this.readCollection(buffer, LinkedHashSet::new);
    }

    /**
     * Writes a set of values to the buffer.
     *
     * @param buffer  The buffer to write data to.
     * @param toWrite The set of values to write.
     */
    public void writeSet(FriendlyByteBuf buffer, Set<T> toWrite) {

        this.writeCollection(buffer, toWrite);
    }

    /**
     * Reads an optional value from the buffer.
     *
     * @param buffer The buffer to read data from.
     * @return The optional value that was read from the buffer.
     */
    public Optional<T> readOptional(FriendlyByteBuf buffer) {

        return buffer.readBoolean() ? Optional.of(this.read(buffer)) : Optional.empty();
    }

    /**
     * Writes an optional value to the buffer.
     *
     * @param buffer   The buffer to write data to.
     * @param optional The optional value to write.
     */
    public void writeOptional(FriendlyByteBuf buffer, Optional<T> optional) {

        final boolean isPresent = optional.isPresent();
        buffer.writeBoolean(isPresent);

        if (isPresent) {

            this.write(buffer, optional.get());
        }
    }

    /**
     * Read a nullable value from the buffer.
     *
     * @param buffer The buffer to read data from.
     * @return The nullable value read from the buffer.
     */
    @Nullable
    public T readNullable(FriendlyByteBuf buffer) {

        return buffer.readBoolean() ? this.read(buffer) : null;
    }

    /**
     * Writes a nullable value to the buffer.
     *
     * @param buffer The buffer to write data to.
     * @param value  The nullable value to write.
     */
    public void writeNullable(FriendlyByteBuf buffer, @Nullable T value) {

        buffer.writeBoolean(value != null);

        if (value != null) {

            this.write(buffer, value);
        }
    }

    /**
     * Read a weighted value from the buffer.
     *
     * @param buffer The buffer to read data from.
     * @return The weighted value read from the buffer.
     */
    public WeightedEntry.Wrapper<T> readWeighted(FriendlyByteBuf buffer) {

        final T value = this.read(buffer);
        final int weight = BookshelfByteBufs.INT.read(buffer);
        return WeightedEntry.wrap(value, weight);
    }

    /**
     * Write a weighted value to the buffer.
     *
     * @param buffer  The buffer to write data to.
     * @param toWrite The weighted value to write to the buffer.
     */
    public void writeWeighted(FriendlyByteBuf buffer, WeightedEntry.Wrapper<T> toWrite) {

        this.write(buffer, toWrite.getData());
        BookshelfByteBufs.INT.write(buffer, toWrite.getWeight().asInt());
    }

    /**
     * Read a weighted random list from the buffer.
     *
     * @param buffer The buffer to read data from.
     * @return The weighted random list that was read from the buffer.
     */
    public SimpleWeightedRandomList<T> readWeightedList(FriendlyByteBuf buffer) {

        final SimpleWeightedRandomList.Builder<T> list = SimpleWeightedRandomList.builder();
        final int size = buffer.readInt();

        for (int i = 0; i < size; i++) {

            final WeightedEntry.Wrapper<T> wrapped = this.readWeighted(buffer);
            list.add(wrapped.getData(), wrapped.getWeight().asInt());
        }

        return list.build();
    }

    /**
     * Write a weighted random list to the buffer.
     *
     * @param buffer The buffer to write data to.
     * @param list   The weighted random list to write to the buffer.
     */
    public void writeWeightedList(FriendlyByteBuf buffer, SimpleWeightedRandomList<T> list) {

        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessor = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) list);
        buffer.writeInt(accessor.bookshelf$getEntries().size());

        for (WeightedEntry.Wrapper<T> entry : accessor.bookshelf$getEntries()) {

            this.writeWeighted(buffer, entry);
        }
    }
}