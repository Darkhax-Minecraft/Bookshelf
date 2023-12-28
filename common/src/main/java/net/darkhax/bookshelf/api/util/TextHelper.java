package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.mixin.accessors.client.AccessorFontManager;
import net.darkhax.bookshelf.mixin.accessors.client.AccessorMinecraft;
import net.darkhax.bookshelf.mixin.accessors.entity.AccessorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TextHelper {

    /**
     * A constant reference to the ID of the default Minecraft font. This font is assumed when no font is specified.
     */
    public static final ResourceLocation FONT_DEFAULT = new ResourceLocation("minecraft", "default");

    /**
     * A constant reference to the ID of the alt font. This font displays the Standard Galactic Alphabet and is used by
     * the enchanting table and enchantment system.
     */
    public static final ResourceLocation FONT_ALT = new ResourceLocation("minecraft", "alt");

    /**
     * A constant reference to the ID of the illager font. This font is associated with the illagers but is not used in
     * the vanilla game. This font originally appeared in the spin-off game Minecraft Dungeons.
     */
    public static final ResourceLocation FONT_ILLAGER = new ResourceLocation("minecraft", "illageralt");

    /**
     * A constant reference to the ID of the uniform font. This is a more traditional and unstylized font.
     */
    public static final ResourceLocation FONT_UNIFORM = new ResourceLocation("minecraft", "uniform");

    /**
     * Creates a component that displays a duration of ticks in HH:MM:SS format. Hovering over the component will
     * display a tooltip with the amount of ticks.
     *
     * @param ticks The duration of ticks to format.
     * @return A component that displays the duration of ticks.
     */
    public static MutableComponent getFormattedTime(int ticks) {

        return getFormattedTime(ticks, true);
    }

    public static MutableComponent getFormattedTime(int ticks, boolean includeHover) {

        return getFormattedTime(ticks, includeHover, 1f);
    }

    public static MutableComponent getFormattedTime(int ticks, boolean includeHover, Level level) {

        return getFormattedTime(ticks, includeHover, level.tickRateManager().tickrate());
    }

    /**
     * Creates a component that displays a duration of ticks in HH:MM:SS format.
     *
     * @param ticks        The duration of ticks to format.
     * @param includeHover Should the component show a tooltip with the raw tick time when the mouse hovers over it?
     * @return A component that displays the duration of ticks.
     */
    public static MutableComponent getFormattedTime(int ticks, boolean includeHover, float tickRate) {

        MutableComponent component = Component.literal(StringUtil.formatTickDuration(ticks, tickRate));

        if (includeHover) {

            component = setHover(component, Component.translatable("text.bookshelf.ticks", ticks));
        }

        return component;
    }

    /**
     * Attaches a component to another as a hover component. The hover component will be displayed in a tooltip that
     * appears when hovering over the component.
     *
     * @param component The base component to attach a hover component to.
     * @param hoverInfo The component to display when the base component is hovered over.
     * @return A mutable instance of the component with the hover info attached.
     */
    public static MutableComponent setHover(Component component, Component hoverInfo) {

        return setHover(component, HoverEvent.Action.SHOW_TEXT, hoverInfo);
    }

    /**
     * Attaches a hover event that displays information about an item. This will use the default stack instance.
     *
     * @param component The component to attach the hover event to.
     * @param hoverInfo The ItemStack to derive hover info from.
     * @return A mutable instance of the component with the hover event attached.
     */
    public static MutableComponent setHover(Component component, Item hoverInfo) {

        return setHover(component, hoverInfo.getDefaultInstance());
    }

    /**
     * Attaches a hover event that displays information about an ItemStack.
     *
     * @param component The component to attach the hover event to.
     * @param hoverInfo The ItemStack to derive hover info from.
     * @return A mutable instance of the component with the hover event attached.
     */
    public static MutableComponent setHover(Component component, ItemStack hoverInfo) {

        return setHover(component, HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(hoverInfo));
    }

    /**
     * Attaches a hover event that displays information about an entity.
     *
     * @param component The component to attach the hover event to.
     * @param hoverInfo The entity to derive hover info from.
     * @return A mutable instance of the component with the hover event attached.
     */
    public static MutableComponent setHover(Component component, Entity hoverInfo) {

        if (hoverInfo instanceof AccessorEntity accessor) {

            return setHover(component, accessor.bookshelf$createHoverEvent());
        }

        return setHover(component, HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityTooltipInfo(hoverInfo.getType(), hoverInfo.getUUID(), hoverInfo.getName()));
    }

    /**
     * Attaches a hover event to a component.
     *
     * @param component The component to attach the hover event to.
     * @param action    The type of action to perform on hover.
     * @param value     The value for the action.
     * @param <AV>      The type of the actions value.
     * @param <AT>      The type of the action.
     * @return A mutable instance of the component with the hover event attached.
     */
    public static <AV, AT extends HoverEvent.Action<AV>> MutableComponent setHover(Component component, AT action, AV value) {

        return setHover(component, new HoverEvent(action, value));
    }

    /**
     * Attaches a hover event to a component.
     *
     * @param component The component to attach the hover event ot.
     * @param hoverInfo The hover event to append.
     * @return A mutable instance of the component with the hover event attached.
     */
    public static MutableComponent setHover(Component component, HoverEvent hoverInfo) {

        return mutable(component).withStyle(style -> style.withHoverEvent(hoverInfo));
    }

    /**
     * Gets a mutable instance of a component. If the component is not mutable a mutable copy will be created.
     * Components that are already mutable will just be passed through.
     *
     * @param component The component to get a mutable instance of.
     * @return A mutable instance of the component.
     */
    public static MutableComponent mutable(Component component) {

        return component instanceof MutableComponent mutable ? mutable : component.copy();
    }

    /**
     * Recursively applies a custom font to a text component and all of it's children components.
     *
     * @param text The text to apply the font to.
     * @param font The ID of the font to apply.
     * @return A modified text component that has had the font applied.
     */
    public static Component applyFont(Component text, ResourceLocation font) {

        if (text == CommonComponents.EMPTY) {

            return text;
        }

        MutableComponent modified = text.copy();
        modified.withStyle(style -> style.withFont(font));
        modified.getSiblings().forEach(sib -> applyFont(sib, font));
        return modified;
    }

    /**
     * Gathers a set of font IDs from the game. Fonts are only registered on the client so the set will be empty or
     * crash if accessed on the server.
     *
     * @return A set of available font IDs.
     */
    public static Set<ResourceLocation> getRegisteredFonts() {

        if (!Services.PLATFORM.isPhysicalClient()) {

            return Collections.emptySet();
        }

        return ((AccessorFontManager) (((AccessorMinecraft) Minecraft.getInstance()).bookshelf$getFontManager())).bookshelf$getFonts().keySet();
    }

    /**
     * Attempts to localize several translation keys. If the first key does not have a matching value it will go on to
     * try the next.
     *
     * @param id   An ID to include within each key using basic string formatting. The first parameter is the namespace
     *             and the second is the value. For example if a key was "tooltip.{0}.{1}.info", passing in
     *             minecraft:stick will produce tooltip.minecraft.stick.info as the key.
     * @param keys An array of potential keys to use.
     * @return If the key has a valid localization it will be returned. When no matches are found the value will be
     * null.
     */
    @Nullable
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
     * Attempts to localize several translation keys, only returning the first key that can be localized. If no keys can
     * be localized the result will be null.
     *
     * @param keys   An array of keys to attempt to localize.
     * @param params An array of arguments to include when formatting the translated text.
     * @return If any of the keys can be localized a mutable component will be returned. Otherwise, the result will be
     * null.
     */
    @Nullable
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
     * Attempts to localize the text. If the text can not be localized the result will be null.
     *
     * @param key  The key to localize.
     * @param args An array of arguments to include when formatting the translated text.
     * @return If the key can be resolved a mutable component will be returned. Otherwise, the value will be null.
     */
    @Nullable
    public static MutableComponent lookupTranslation(String key, Object... args) {

        return lookupTranslation(key, (s, o) -> null, args);
    }

    /**
     * Attempts to localize the text. If the text can not be localized the fallback will be used.
     *
     * @param key      The key to localize.
     * @param fallback A fallback component to use when the key can not be localized.
     * @param args     An array of arguments to include when formatting the translated text.
     * @return If the key can be resolved a mutable component will be returned. Otherwise, the fallback value will be
     * used.
     */
    @Nullable
    public static MutableComponent lookupTranslation(String key, MutableComponent fallback, Object... args) {

        return lookupTranslation(key, (s, o) -> fallback, args);
    }

    /**
     * Attempts to localize text, if the text can not be localized the fallback will be used.
     *
     * @param key      The key to localize.
     * @param fallback A fallback function that will take the localization key and arguments and produce a new
     *                 component. If this function is not provided the fallback result will just be null.
     * @param args     An array of arguments to include when formatting the translated text.
     * @return If the translation can be resolved a mutable component will be returned, otherwise the value will be
     * null.
     */
    @Nullable
    public static MutableComponent lookupTranslation(String key, @Nullable BiFunction<String, Object[], MutableComponent> fallback, Object... args) {

        if (I18n.exists(key)) {

            return Component.translatable(key, args);
        }

        return fallback != null ? fallback.apply(key, args) : null;
    }

    /**
     * Creates a component that will copy the value to the players clipboard when they click it.
     *
     * @param text The text to display and copy.
     * @return A mutable component that will copy the text when you click it.
     */
    public static MutableComponent textWithCopy(String text) {

        return setCopyText(Component.literal(text), text);
    }

    /**
     * Adds a click event to a component that will copy the provided text to the players clipboard when they click it.
     *
     * @param component The component to attach a click event to.
     * @param copy      The text to copy to the clipboard.
     * @return A mutable component that will copy the text when you click it.
     */
    public static MutableComponent setCopyText(MutableComponent component, String copy) {

        return component.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy)));
    }

    /**
     * Joins multiple components together using a separator component.
     *
     * @param separator The separator component to insert between other components.
     * @param toJoin    An array of components to join.
     * @return A mutable component containing the joint components.
     */
    public static MutableComponent join(Component separator, Component... toJoin) {

        return join(separator, Arrays.stream(toJoin).iterator());
    }

    /**
     * Joins multiple components together using a separator component.
     *
     * @param separator The separator component to insert between other components.
     * @param toJoin    A collection of components to join.
     * @return A mutable component containing the joint components.
     */
    public static MutableComponent join(Component separator, Collection<Component> toJoin) {

        return join(separator, toJoin.iterator());
    }

    /**
     * Joins multiple components together using a separator component.
     *
     * @param separator The separator component to insert between other components.
     * @param toJoin    An iterable group of components to join.
     * @return A mutable component containing the joint components.
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
}