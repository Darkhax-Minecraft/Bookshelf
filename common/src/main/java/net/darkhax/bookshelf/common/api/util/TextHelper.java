package net.darkhax.bookshelf.common.api.util;

import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.annotation.OnlyFor;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.api.text.unit.Units;
import net.darkhax.bookshelf.common.mixin.access.client.AccessorFontManager;
import net.darkhax.bookshelf.common.mixin.access.client.AccessorMinecraft;
import net.darkhax.bookshelf.common.mixin.access.entity.AccessorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextHelper {

    /**
     * Creates translated text from a resource location.
     *
     * @param prefix   The prefix to add at the start of the key.
     * @param suffix   The suffix to add at the end of the key.
     * @param location The resource location to use in the key.
     * @param args     An optional array of arguments to format into the translated text.
     * @return A translated component based on the resource location.
     */
    public static MutableComponent fromResourceLocation(@Nullable String prefix, @Nullable String suffix, ResourceLocation location, Object... args) {
        final StringBuilder builder = new StringBuilder();
        if (prefix != null) {
            builder.append(prefix).append(".");
        }
        builder.append(location.getNamespace()).append(".").append(location.getPath());
        if (suffix != null) {
            builder.append(".").append(suffix);
        }
        return Component.translatable(builder.toString(), args);
    }

    /**
     * Formats a duration of time in ticks into its real world time counterpart. This method should ONLY be used if a
     * world context is not available or if the duration of time is not affected by custom world tick rates.
     *
     * @param ticks The duration of ticks.
     * @return The formatted time duration.
     */
    public static MutableComponent formatDuration(int ticks) {

        return formatDuration(ticks, true, 1f);
    }

    /**
     * Formats a duration of time in ticks into its real world time counterpart.
     *
     * @param ticks The duration of ticks.
     * @param level The world level that the duration is taking place. This is used to account for custom tick rates set
     *              using the game rule.
     * @return The formatted time duration.
     */
    public static MutableComponent formatDuration(int ticks, Level level) {
        return formatDuration(ticks, true, level);
    }

    /**
     * Formats a duration of time in ticks into its real world time counterpart.
     *
     * @param ticks        The duration of ticks.
     * @param includeHover Should the raw tick amount be shown when hovering the text?
     * @param level        The world level that the duration is taking place. This is used to account for custom tick
     *                     rates set using the game rule.
     * @return The formatted time duration.
     */
    public static MutableComponent formatDuration(int ticks, boolean includeHover, Level level) {
        return formatDuration(ticks, includeHover, level.tickRateManager().tickrate());
    }

    /**
     * Formats a duration of time in ticks into its real world time counterpart.
     *
     * @param ticks            The duration of ticks.
     * @param showTicksOnHover Should the raw tick amount be shown when hovering the text?
     * @param tickRate         The tick rate of the current world.
     * @return The formatted time duration.
     */
    public static MutableComponent formatDuration(int ticks, boolean showTicksOnHover, float tickRate) {
        MutableComponent timeText = Component.literal(StringUtil.formatTickDuration(ticks, tickRate));
        if (showTicksOnHover) {
            timeText = Units.TICK.format(ticks);
        }
        return timeText;
    }

    /**
     * Applies hover text to a text component.
     *
     * @param base  The base text component to append.
     * @param hover The text to display while hovering.
     * @return A component instance with the hover event applied.
     */
    public static MutableComponent withHover(Component base, Component hover) {
        return withHover(base, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
    }

    /**
     * Applies hover text based on an entity to a text component.
     *
     * @param base  The base text component to append.
     * @param hover The Entity to display in the hover text.
     * @return A component instance with the hover event applied.
     */
    public static MutableComponent withHover(Component base, Entity hover) {
        return withHover(base, hoverEvent(hover));
    }

    /**
     * Applies hover text based on an item to a text component.
     *
     * @param base  The base text component to append.
     * @param hover The ItemStack to display in the hover text.
     * @return A component instance with the hover event applied.
     */
    public static MutableComponent withHover(Component base, ItemStack hover) {
        return withHover(base, new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(hover)));
    }

    /**
     * Applies a hover event to a text component.
     *
     * @param base  The base text component to append.
     * @param hover The hover event to apply.
     * @return A component instance with the hover event applied.
     */
    public static MutableComponent withHover(Component base, HoverEvent hover) {
        return mutable(base).withStyle(style -> style.withHoverEvent(hover));
    }

    /**
     * Creates a new hover event for an entity.
     *
     * @param entity The entity to create a HoverEvent for.
     * @return When Mixins are available the entity will create its own HoverEvent, otherwise a fallback based on the
     * default implementation will be used.
     */
    public static HoverEvent hoverEvent(Entity entity) {
        if (entity instanceof AccessorEntity access) {
            return access.bookshelf$createHoverEvent();
        }
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityTooltipInfo(entity.getType(), entity.getUUID(), entity.getName()));
    }

    /**
     * Provides mutable access to a component.
     *
     * @param component The component to access.
     * @return If the component is already mutable the same component instance will be returned. Otherwise, a mutable
     * copy of the component will be created.
     */
    public static MutableComponent mutable(Component component) {
        return component instanceof MutableComponent mutable ? mutable : component.copy();
    }

    /**
     * Recursively applies a font to text and all of its subcomponents.
     *
     * @param text The text to apply the font to.
     * @param font The ID of the font to apply.
     * @return The input text with the font applied to its style and the style of its subcomponents.
     */
    public static Component applyFont(Component text, ResourceLocation font) {
        if (text == CommonComponents.EMPTY) {
            return text;
        }
        final MutableComponent modified = mutable(text);
        modified.withStyle(style -> style.withFont(font));
        modified.getSiblings().forEach(sib -> applyFont(sib, font));
        return modified;
    }

    /**
     * Attempts to localize several different translation keys and will return the first one that is available on the
     * client. If no keys are mapped the result will be null.
     *
     * @param id   An ID the format within each key using basic string formatting. The first parameter is the namespace
     *             and the second is the path. For example if a key was "tooltip.{0}.{1}.info" the ID "minecraft:stick"
     *             will produce a final key of "tooltip.minecraft.stick.info".
     * @param keys An array of translation keys to attempt localizing.
     * @return A component for the first translation key that is mapped, or null if none of the keys are mapped.
     */
    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    public static MutableComponent lookupTranslationWithAlias(ResourceLocation id, String... keys) {
        for (String key : keys) {
            final MutableComponent lookupResult = lookupTranslation(key.formatted(id.getNamespace(), id.getPath()));
            if (lookupResult != null) {
                return lookupResult;
            }
        }
        return null;
    }

    /**
     * Attempts to localize several different translation keys and will return the first one that is available on the
     * client. If no keys are mapped the result will be null.
     *
     * @param keys   An array of translation keys to attempt localizing.
     * @param params Arguments that are passed into the translated text.
     * @return A component for the first translation key that is mapped, or null if none of the keys are mapped.
     */
    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    public static MutableComponent lookupTranslationWithAlias(String[] keys, Object... params) {
        for (String key : keys) {
            final MutableComponent lookupResult = lookupTranslation(key, params);
            if (lookupResult != null) {
                return lookupResult;
            }
        }
        return null;
    }

    /**
     * Attempts to localize text. If the translation key is not mapped on the client the component will be null.
     *
     * @param key  The translation key to localize.
     * @param args Arguments that are passed into the translated text.
     * @return If the key can be translated a component will be returned, otherwise null.
     */
    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    public static MutableComponent lookupTranslation(String key, Object... args) {
        return lookupTranslation(key, (s, o) -> null, args);
    }

    /**
     * Attempts to localize text. If the translation key is not mapped on the client the fallback will be used.
     *
     * @param key      The translation key to localize.
     * @param fallback The fallback text to use when the key is unavailable.
     * @param args     Arguments that are passed into the translated text.
     * @return If the key can be translated a component will be returned, otherwise the fallback will be used.
     */
    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    public static MutableComponent lookupTranslation(String key, MutableComponent fallback, Object... args) {
        return lookupTranslation(key, (s, o) -> fallback, args);
    }

    /**
     * Attempts to localize text. If the translation key is not mapped on the client it will try to use the fallback.
     *
     * @param key      The translation key to localize.
     * @param fallback A function that provides fallback text based on the original translation key and arguments. Both
     *                 the function and the result of this function may be null.
     * @param args     Arguments that are passed into the translated text.
     * @return If the key can be translated a component will be returned, otherwise the fallback will be used.
     */
    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    public static MutableComponent lookupTranslation(String key, @Nullable BiFunction<String, Object[], MutableComponent> fallback, Object... args) {
        if (!Services.PLATFORM.isPhysicalClient()) {
            throw new IllegalStateException("Text can not be translated on the server.");
        }
        return I18n.exists(key) ? Component.translatable(key, args) : fallback != null ? fallback.apply(key, args) : null;
    }

    /**
     * Creates a text component that will copy the value to the players clipboard when they click it.
     *
     * @param text The text to display and copy to the clipboard.
     * @return A component that displays text and copies that text to the clipboard when the player clicks on it.
     */
    public static MutableComponent copyText(String text) {
        return setCopyText(Component.literal(text), text);
    }

    /**
     * Adds a click event to a text component that will copy text to the players clipboard when they click on it.
     *
     * @param component The component to attack the click event to.
     * @param copy      The text to be copied to the clipboard.
     * @return A text component that will copy the text when the player clicks on it.
     */
    public static MutableComponent setCopyText(MutableComponent component, String copy) {
        return component.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy)));
    }

    /**
     * Joins several components together using a separator.
     *
     * @param separator The separator to insert between other components.
     * @param toJoin    The components to join together.
     * @return A component containing the joint components.
     */
    public static MutableComponent join(Component separator, Component... toJoin) {
        return join(separator, Arrays.stream(toJoin).iterator());
    }

    /**
     * Joins several components together using a separator.
     *
     * @param separator The separator to insert between other components.
     * @param toJoin    The components to join together.
     * @return A component containing the joint components.
     */
    public static MutableComponent join(Component separator, Collection<Component> toJoin) {
        return join(separator, toJoin.iterator());
    }

    /**
     * Joins several components together using a separator.
     *
     * @param separator The separator to insert between other components.
     * @param toJoin    The components to join together.
     * @return A component containing the joint components.
     */
    public static MutableComponent join(Component separator, Iterator<Component> toJoin) {
        final MutableComponent joined = Component.literal("");
        while (toJoin.hasNext()) {
            joined.append(toJoin.next());
            if (toJoin.hasNext()) {
                joined.append(separator);
            }
        }
        return joined;
    }

    /**
     * Finds a set of possible matches within an iterable group of strings. This can be used to take invalid user input
     * and attempt to find a plausible match using known good values.
     * <p>
     * Possible matches are determined using the Levenshtein distance between the input value and the potential
     * candidates. The Levenshtein distance represents the number of characters that need to be changed in order for the
     * strings to match. For example "abc" to "def" has a difference of three, while "123" to "1234" has a distance of
     * 1.
     *
     * @param input      The input string.
     * @param candidates An iterable group of possible candidates.
     * @return A set of possible matches for the input. This set will include all candidates that have the lowest
     * possible distance. For example if there were 100 candidates and five had a distance of one all five of the lowest
     * distance values will be returned.
     */
    public static Set<String> getPossibleMatches(String input, Iterable<String> candidates) {
        return getPossibleMatches(input, candidates, Integer.MAX_VALUE);
    }

    /**
     * Finds a set of possible matches within an iterable group of strings. This can be used to take invalid user input
     * and attempt to find a plausible match using known good values.
     * <p>
     * Possible matches are determined using the Levenshtein distance between the input value and the potential
     * candidates. The Levenshtein distance represents the number of characters that need to be changed in order for the
     * strings to match. For example "abc" to "def" has a difference of three, while "123" to "1234" has a distance of
     * 1.
     *
     * @param input      The input string.
     * @param candidates An iterable group of possible candidates.
     * @param threshold  The maximum distance allowed for a value to be considered. For example if the threshold is two,
     *                   only entries with a distance of two or less will be considered.
     * @return A set of possible matches for the input. This set will include all candidates that have the lowest
     * possible distance. For example if there were 100 candidates and five had a distance of one all five of the lowest
     * distance values will be returned.
     */
    public static Set<String> getPossibleMatches(String input, Iterable<String> candidates, int threshold) {
        final HashSet<String> bestMatches = new HashSet();
        int distance = threshold;
        for (String candidate : candidates) {
            final int currentDistance = StringUtils.getLevenshteinDistance(input, candidate);
            if (currentDistance < distance) {
                distance = currentDistance;
                bestMatches.clear();
                bestMatches.add(candidate);
            }
            else if (currentDistance == distance) {
                bestMatches.add(candidate);
            }
        }
        return bestMatches;
    }

    /**
     * Formats a collection of values to a string using {@link Object#toString()}. If the collection has more than one
     * value each entry will be separated by commas. Each value will also be quoted.
     *
     * @param collection The collection of values to format.
     * @param <T>        The type of value being formatted.
     * @return The formatted string.
     */
    public static <T> String formatCollection(Collection<T> collection) {
        return formatCollection(collection, entry -> "\"" + entry.toString() + "\"", ", ");
    }

    /**
     * Formats a collection of values to a string. If the collection has more than one value each entry will be
     * separated using the delimiter.
     *
     * @param collection The collection of values to format.
     * @param formatter  A function used to format the value to a string.
     * @param delimiter  A delimiter used to separate values in a list.
     * @param <T>        The type of value being formatted.
     * @return The formatted string.
     */
    public static <T> String formatCollection(Collection<T> collection, Function<T, String> formatter, String delimiter) {
        return collection.size() == 1 ? formatter.apply(collection.stream().findFirst().get()) : collection.stream().map(formatter).collect(Collectors.joining(delimiter));
    }

    @OnlyFor(PhysicalSide.CLIENT)
    public static Set<ResourceLocation> getRegisteredFonts() {
        if (!Services.PLATFORM.isPhysicalClient()) {
            return Collections.emptySet();
        }
        return ((AccessorFontManager) (((AccessorMinecraft) Minecraft.getInstance()).bookshelf$getFontManager())).bookshelf$getFonts().keySet();
    }

    /**
     * Creates a translation key that should map to a display name for the tag.
     * <p>
     * Tags for vanilla registries use the format tag.reg_path.namespace.path and tags for modded registries use the
     * format tag.reg_namespace.reg_path.namespace.path.
     * <p>
     * This is a new standard being pushed by the Fabric API and recipe viewers. While it has not been universally
     * adopted yet, it should be considered best practice to do so moving forward.
     *
     * @param tag The tag to provide a name key for.
     * @return A translation key that should map to a display name.
     */
    public static String getTagName(TagKey<?> tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append("tag.");
        final ResourceLocation regId = tag.registry().location();
        final ResourceLocation tagId = tag.location();
        if (!regId.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            builder.append(regId.getNamespace()).append(".");
        }
        builder.append(regId.getPath().replace("/", ".")).append(".").append(tagId.getNamespace()).append(".").append(tagId.getPath().replace("/", ".").replace(":", "."));
        return builder.toString();
    }
}