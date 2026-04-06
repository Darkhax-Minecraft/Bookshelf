package net.darkhax.bookshelf.common.api.data.codecs.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A CodecHelper wraps a Codec to provide a large amount of helpers and utilities to make working with the Codec easier
 * and more flexible.
 *
 * @param <T> The type handled by the codec helper.
 */
public class MapCodecHelper<T> {

    /**
     * The root codec that powers all the helpers and utilities offered by the CodecHelper.
     */
    private final Codec<T> elementCodec;

    /**
     * A function for creating an array of the codecs type. This allows us to create clean arrays for our codecs without
     * relying on sketchy code.
     */
    private final IntFunction<T[]> arrayBuilder;

    @SafeVarargs
    public MapCodecHelper(Codec<T> elementCodec, T... vargs) {
        if (vargs.length > 0) {
            throw new IllegalArgumentException("The arrayBuilder must be empty!");
        }
        this.elementCodec = elementCodec;
        this.arrayBuilder = size -> (T[]) Array.newInstance(vargs.getClass().getComponentType(), size);
    }

    /**
     * Gets a codec that can read and write single instances of the element.
     *
     * @return A Codec that can read and write single instances of the element.
     */
    public Codec<T> get() {
        return this.elementCodec;
    }

    /**
     * A helper for defining a field of this type in a RecordCodecBuilder.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodecBuilder.
     * @return A RecordCodecBuilder that represents a field.
     */
    public <O> RecordCodecBuilder<O, T> get(String fieldName, Function<O, T> getter) {
        return this.get().fieldOf(fieldName).forGetter(getter);
    }

    /**
     * A helper for defining a field of this type in a RecordCodecBuilder. If the field is not present the fallback
     * value will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodecBuilder.
     * @return A RecordCodecBuilder that represents a field of the helpers type with a fallback value.
     */
    public <O> RecordCodecBuilder<O, T> get(String fieldName, Function<O, T> getter, T fallback) {
        return this.get().optionalFieldOf(fieldName, fallback).forGetter(getter);
    }

    /**
     * Gets a codec that can read and write an array. For the sake of convenience single elements are treated as an
     * array of one.
     *
     * @return A Codec that can read and write an array.
     */
    public Codec<T[]> arrayCodec() {
        return MapCodecs.flexibleArray(this.get(), this.arrayBuilder);
    }

    /**
     * A helper for defining a field for an array of this type in a RecordCodecBuilder. For the sake of convenience
     * single elements are treated as an array of one.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for an array.
     */
    public <O> RecordCodecBuilder<O, T[]> arrayCodec(String fieldName, Function<O, T[]> getter) {
        return this.arrayCodec().fieldOf(fieldName).forGetter(getter);
    }

    /**
     * A helper for defining a field for an array of this type in a RecordCodecBuilder. For the sake of convenience
     * single elements are treated as an array of one. If the field is not present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for an array.
     */
    public <O> RecordCodecBuilder<O, T[]> arrayCodec(String fieldName, Function<O, T[]> getter, T... fallback) {
        return this.arrayCodec().optionalFieldOf(fieldName, fallback).forGetter(getter);
    }

    /**
     * Gets a codec that can read and write a list. For the sake of convenience single elements are treated as a list of
     * one.
     *
     * @return A Codec that can read and write a list.
     */
    public Codec<List<T>> listCodec() {
        return MapCodecs.flexibleList(this.get());
    }

    /**
     * A helper for defining a field for a list of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a list of one.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a list.
     */
    public <O> RecordCodecBuilder<O, List<T>> listCodec(String fieldName, Function<O, List<T>> getter) {
        return this.listCodec().fieldOf(fieldName).forGetter(getter);
    }

    /**
     * A helper for defining a field for a list of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a list of one. If the field is not present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a list.
     */
    public <O> RecordCodecBuilder<O, List<T>> listCodec(String fieldName, Function<O, List<T>> getter, List<T> fallback) {
        return this.listCodec().optionalFieldOf(fieldName, fallback).forGetter(getter);
    }

    /**
     * A helper for defining a field for a list of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a list of one. If the field is not present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a list.
     */
    public <O> RecordCodecBuilder<O, List<T>> listCodec(String fieldName, Function<O, List<T>> getter, T... fallback) {
        return this.listCodec().optionalFieldOf(fieldName, List.of(fallback)).forGetter(getter);
    }

    /**
     * Gets a codec that can read and write a set. For the sake of convenience single elements are treated as a list of
     * one.
     *
     * @return A Codec that can read and write a set.
     */
    public Codec<Set<T>> setCodec() {
        return MapCodecs.flexibleSet(this.get());
    }

    /**
     * A helper for defining a field for a set of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a set of one.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a set.
     */
    public <O> RecordCodecBuilder<O, Set<T>> setCodec(String fieldName, Function<O, Set<T>> getter) {
        return this.setCodec().fieldOf(fieldName).forGetter(getter);
    }

    /**
     * A helper for defining a field for a set of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a set of one. If the field is not present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a set.
     */
    public <O> RecordCodecBuilder<O, Set<T>> setCodec(String fieldName, Function<O, Set<T>> getter, Set<T> fallback) {
        return this.setCodec().optionalFieldOf(fieldName, fallback).forGetter(getter);
    }

    /**
     * A helper for defining a field for a set of this type in a RecordCodecBuilder. For the sake of convenience single
     * elements are treated as a set of one. If the field is not present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a set.
     */
    public <O> RecordCodecBuilder<O, Set<T>> setCodec(String fieldName, Function<O, Set<T>> getter, T... fallback) {
        return this.setCodec().optionalFieldOf(fieldName, Set.of(fallback)).forGetter(getter);
    }

    /**
     * Gets a codec that can read and write an optional value.
     *
     * @param fieldName The name of the field to read the value from.
     * @return A Codec that can read and write an optional value.
     */
    public MapCodec<Optional<T>> optionalCodec(String fieldName) {
        return this.get().optionalFieldOf(fieldName);
    }

    /**
     * A helper for defining a field for an optional value of this type in a RecordCodecBuilder.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for an optional value.
     */
    public <O> RecordCodecBuilder<O, Optional<T>> optionalCodec(String fieldName, Function<O, Optional<T>> getter) {
        return this.get().optionalFieldOf(fieldName).forGetter(getter);
    }

    /**
     * A helper for defining a field for an optional value of this type in a RecordCodecBuilder. If the field is not
     * present the fallback will be used.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param fallback  The fallback value to use when the field is not present.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for an optional value.
     */
    public <O> RecordCodecBuilder<O, Optional<T>> optionalCodec(String fieldName, Function<O, Optional<T>> getter, Optional<T> fallback) {
        return MapCodecs.optional(this.get(), fieldName, fallback, true).forGetter(getter);
    }

    /**
     * Gets a codec that can read and write nullable values.
     *
     * @param fieldName The name of the field to read the value from.
     * @return A Codec that can read and write nullable values.
     */
    public MapCodec<T> nullableCodec(String fieldName) {
        return MapCodecs.nullable(this.get(), fieldName);
    }

    /**
     * A helper for defining a field for a nullable value of this type in a RecordCodecBuilder.
     *
     * @param fieldName The name of the field to read the value from.
     * @param getter    A getter that will read the value from an object of the RecordCodecBuilders type.
     * @param <O>       The type of the RecordCodedBuilder.
     * @return A RecordCodecBuilder that represents a field for a nullable value.
     */
    public <O> RecordCodecBuilder<O, T> nullableCodec(String fieldName, Function<O, T> getter) {
        return this.nullableCodec(fieldName).forGetter(getter);
    }
}