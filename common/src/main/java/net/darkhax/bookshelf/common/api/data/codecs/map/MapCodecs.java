package net.darkhax.bookshelf.common.api.data.codecs.map;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.util.FunctionHelper;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3fc;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class MapCodecs {

    // JAVA TYPES
    public static final MapCodecHelper<Boolean> BOOLEAN = new MapCodecHelper<>(Codec.BOOL);
    public static final MapCodecHelper<Byte> BYTE = new MapCodecHelper<>(Codec.BYTE);
    public static final MapCodecHelper<Short> SHORT = new MapCodecHelper<>(Codec.SHORT);
    public static final MapCodecHelper<Integer> INT = new MapCodecHelper<>(Codec.INT);
    public static final MapCodecHelper<Float> FLOAT = new MapCodecHelper<>(Codec.FLOAT);
    public static final MapCodecHelper<Long> LONG = new MapCodecHelper<>(Codec.LONG);
    public static final MapCodecHelper<Double> DOUBLE = new MapCodecHelper<>(Codec.DOUBLE);
    public static final MapCodecHelper<String> STRING = new MapCodecHelper<>(Codec.STRING);
    public static final MapCodecHelper<UUID> UUID = new MapCodecHelper<>(UUIDUtil.CODEC);

    // ENUMS
    public static final MapCodecHelper<Rarity> ITEM_RARITY = new MapCodecHelper<>(enumerable(Rarity.class));
    public static final MapCodecHelper<AttributeModifier.Operation> ATTRIBUTE_OPERATION = new MapCodecHelper<>(AttributeModifier.Operation.CODEC);
    public static final MapCodecHelper<Direction> DIRECTION = new MapCodecHelper<>(enumerable(Direction.class));
    public static final MapCodecHelper<Direction.Axis> AXIS = new MapCodecHelper<>(enumerable(Direction.Axis.class));
    public static final MapCodecHelper<Direction.Plane> PLANE = new MapCodecHelper<>(enumerable(Direction.Plane.class));
    public static final MapCodecHelper<MobCategory> MOB_CATEGORY = new MapCodecHelper<>(enumerable(MobCategory.class));
    public static final MapCodecHelper<DyeColor> DYE_COLOR = new MapCodecHelper<>(enumerable(DyeColor.class));
    public static final MapCodecHelper<SoundSource> SOUND_SOURCE = new MapCodecHelper<>(enumerable(SoundSource.class));
    public static final MapCodecHelper<Difficulty> DIFFICULTY = new MapCodecHelper<>(enumerable(Difficulty.class));
    public static final MapCodecHelper<EquipmentSlot> EQUIPMENT_SLOT = new MapCodecHelper<>(enumerable(EquipmentSlot.class));
    public static final MapCodecHelper<Mirror> MIRROR = new MapCodecHelper<>(enumerable(Mirror.class));
    public static final MapCodecHelper<Rotation> ROTATION = new MapCodecHelper<>(enumerable(Rotation.class));

    // MINECRAFT TYPES
    public static final MapCodecHelper<Identifier> RESOURCE_LOCATION = new MapCodecHelper<>(Identifier.CODEC);
    public static final MapCodecHelper<CompoundTag> COMPOUND_TAG = new MapCodecHelper<>(CompoundTag.CODEC);
    public static final MapCodecHelper<ItemStack> ITEM_STACK = new MapCodecHelper<>(ItemStack.CODEC);
    public static final MapCodecHelper<Component> TEXT = new MapCodecHelper<>(ComponentSerialization.CODEC);
    public static final MapCodecHelper<BlockPos> BLOCK_POS = new MapCodecHelper<>(BlockPos.CODEC);
    public static final MapCodecHelper<Ingredient> INGREDIENT = new MapCodecHelper<>(Ingredient.CODEC);
    public static final MapCodecHelper<BlockState> BLOCK_STATE_MAP_CODEC = new MapCodecHelper<>(BlockState.FULL_CODEC);
    public static final MapCodecHelper<AttributeModifier> ATTRIBUTE_MODIFIER = new MapCodecHelper<>(AttributeModifier.CODEC);
    public static final MapCodecHelper<MobEffectInstance> EFFECT_INSTANCE = new MapCodecHelper<>(MobEffectInstance.CODEC);
    public static final MapCodecHelper<Vector3fc> VECTOR_3F = new MapCodecHelper<>(ExtraCodecs.VECTOR3F);

    // Bookshelf Types
    public static final MapCodecHelper<ILoadCondition> LOAD_CONDITION = LoadConditions.CODEC_HELPER;

    /**
     * Creates a Codec that can flexibly read individual values as a list in addition to traditional lists.
     *
     * @param codec The codec for reading an individual value.
     * @param <T>   The type of value handled by the codec.
     * @return A Codec that can flexibly read individual values as a list in addition to traditional lists.
     */
    public static <T> Codec<List<T>> flexibleList(Codec<T> codec) {
        return Codec.either(codec.listOf(), codec).xmap(either -> either.map(Function.identity(), List::of), list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list));
    }

    /**
     * Creates a Codec that can flexibly read both individual values and arrays of values as a set.
     *
     * @param codec The Codec for reading an individual value.
     * @param <T>   The type of value handled by the codec.
     * @return A Codec that can flexibly read both individual values and arrays of values as a set.
     */
    public static <T> Codec<Set<T>> flexibleSet(Codec<T> codec) {
        return flexibleList(codec).xmap(LinkedHashSet::new, ArrayList::new);
    }

    /**
     * Creates a Codec that can flexibly read both individual values and arrays of values as an array.
     *
     * @param codec        The Codec for reading an individual value.
     * @param arrayBuilder A function that creates new arrays of the required type. The function is given the size of
     *                     the list.
     * @param <T>          The type of value handled by the codec.
     * @return A Codec that can flexibly read both individual values and arrays of values as an array.
     */
    public static <T> Codec<T[]> flexibleArray(Codec<T> codec, IntFunction<T[]> arrayBuilder) {
        return flexibleList(codec).xmap(list -> list.toArray(arrayBuilder.apply(list.size())), List::of);
    }

    /**
     * Creates a Codec that will use a fallback value if no other value is specified. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that the fallback value is provided by a supplier.
     *
     * @param codec            The base Codec to use.
     * @param name             The name of the field to read from.
     * @param fallbackSupplier A supplier that produces the default value. You should probably memoize this.
     * @param <T>              The type of value handled by the codec.
     * @return A Codec that will use a fallback value if the field is not specified.
     */
    public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier) {
        return fallback(codec, name, fallbackSupplier, true);
    }

    /**
     * Creates a Codec that handles optional values. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that it keeps the type as an Optional.
     *
     * @param codec         The base Codec to use.
     * @param name          The name of the field to read from.
     * @param fallback      The fallback optional value.
     * @param writesDefault Should the default value be written or left blank?
     * @param <T>           The type of the optional value handled by the coded.
     * @return A Codec that handles optional values.
     */
    public static <T> MapCodec<Optional<T>> optional(Codec<T> codec, String name, Optional<T> fallback, boolean writesDefault) {
        return Codec.optionalField(name, codec, false).xmap(o -> o.isPresent() ? o : fallback, a -> a.isEmpty() || (Objects.equals(a.get(), fallback.orElse(null)) && !writesDefault) ? Optional.empty() : a);
    }

    /**
     * Creates a Codec that can handle nullable values.
     *
     * @param codec     The base Codec to use.
     * @param fieldName The name of the field to read from.
     * @param <T>       The type of value handled by the codec.
     * @return A Codec that handles nullable values.
     */
    public static <T> MapCodec<T> nullable(Codec<T> codec, String fieldName) {
        return Codec.optionalField(fieldName, codec, false).xmap(optional -> optional.orElse(null), Optional::ofNullable);
    }

    /**
     * Creates a Codec that will use a fallback value if no other value is specified. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that the fallback value is provided by a supplier. It also
     * allows you to control if the default value should be written when or left blank.
     *
     * @param codec            The base Codec to use.
     * @param name             The name of the field to read from.
     * @param fallbackSupplier A supplier that produces the default value. You should probably memoize this.
     * @param writesDefault    Should the default value be written or left blank?
     * @param <T>              The type of value handled by the codec.
     * @return A Codec that will use a fallback value if the field is not specified.
     */
    public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier, boolean writesDefault) {
        return Codec.optionalField(name, codec, false).xmap(value -> value.orElse(fallbackSupplier.get()), value -> {
            final T fallback = fallbackSupplier.get();
            return Objects.equals(value, fallback) && !writesDefault ? Optional.empty() : Optional.of(value);
        });
    }

    private static <T extends Enum<T>> Map<String, T> getEnumsByName(Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException("Class " + enumClass.getCanonicalName() + " is not an enum!");
        }
        final Map<String, T> valueMap = new HashMap<>();
        for (T value : enumClass.getEnumConstants()) {
            final String name = value.name();
            if (valueMap.containsKey(name)) {
                BookshelfMod.LOG.error("Duplicate name '{}' found in enum '{}'. Another mod is doing something very wrong. old='{}' new='{}'", name, enumClass.getName(), valueMap.get(name), value);
            }
            valueMap.put(name, value);
        }
        return valueMap;
    }

    /**
     * Creates a Codec that handles enum values by using their enum constant names.
     * <br>
     * If the codec can not read an enum value from the provided name it will try again using an all uppercase version
     * of the input. This allows users to write values in all lowercase which may feel more natural for some users and
     * adds extra flexibility.
     * <br>
     * If the codec can not read any enum value from the provided name it will create an error. The error message will
     * try to help the user by recommending a nearby match and including all possible values.
     *
     * @param enumClass The class of the enum to get values for.
     * @param <T>       The type of the enum.
     * @return A codec that can read and write enum values using their enum constant name.
     */
    public static <T extends Enum<T>> Codec<T> enumerable(Class<T> enumClass) {
        final Map<String, T> enumValues = getEnumsByName(enumClass);
        final Function<String, T> fromString = name -> {
            T value = enumValues.get(name);
            if (value == null) {
                value = enumValues.get(name.toUpperCase(Locale.ROOT));
            }
            return value;
        };
        final UnaryOperator<String> errorMessage = name -> {
            final StringJoiner message = new StringJoiner(" ");
            message.add("Unable to find " + enumClass.getSimpleName() + " entry \"" + name + "\".");
            final Set<String> similarMatches = TextHelper.getPossibleMatches(name, enumValues.keySet(), 2);
            if (!similarMatches.isEmpty()) {
                message.add("Did you mean \"" + similarMatches.stream().findFirst().get() + "\"?");
            }
            message.add("Available Options are " + TextHelper.formatCollection(enumValues.keySet()));
            return message.toString();
        };
        return Codec.STRING.flatXmap(string -> Optionull.mapOrElse(fromString.apply(string), DataResult::success, () -> DataResult.error(() -> errorMessage.apply(string))), object -> DataResult.success(object.name()));
    }

    /**
     * Creates a codec that will produce a set of enum values from an array of String values.
     *
     * @param enumClass The type of enum to create a codec for.
     * @param <T>       The type of the enum.
     * @return The EnumSet codec.
     */
    public static <T extends Enum<T>> Codec<EnumSet<T>> enumSet(Class<T> enumClass) {
        return enumerable(enumClass).listOf().xmap(EnumSet::copyOf, ArrayList::new);
    }

    /**
     * Creates a codec that will try two different codecs, using the first valid codec. Encoding will always use the
     * first codec.
     *
     * @param first  The first codec to try when decoding. This will be the only codec used in encoding.
     * @param second The second codec to try when decoding.
     * @param <T>    The type of codec to create.
     * @return A codec that will try two different codecs.
     */
    public static <T> Codec<T> xor(Codec<T> first, Codec<T> second) {
        return Codec.xor(first, second).xmap(FunctionHelper::unpack, Either::left);
    }
}