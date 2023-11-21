package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.mixin.accessors.client.AccessorFontManager;
import net.darkhax.bookshelf.mixin.accessors.client.AccessorMinecraft;
import net.darkhax.bookshelf.mixin.accessors.entity.AccessorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
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

    public static final ResourceLocation FONT_ALT = new ResourceLocation("minecraft", "alt");
    public static final ResourceLocation FONT_DEFAULT = new ResourceLocation("minecraft", "default");
    public static final ResourceLocation FONT_ILLAGER = new ResourceLocation("minecraft", "illageralt");
    public static final ResourceLocation FONT_UNIFORM = new ResourceLocation("minecraft", "uniform");

    public static MutableComponent getFormatedTime(int ticks, boolean includeHover) {

        MutableComponent component = Component.literal(StringUtil.formatTickDuration(ticks));

        if (includeHover) {

            component = setHover(component, Component.translatable("text.bookshelf.ticks", ticks));
        }

        return component;
    }

    public static <T extends Component> T setHover(T component, Component hoverInfo) {

        return setHover(component, HoverEvent.Action.SHOW_TEXT, hoverInfo);
    }

    public static <T extends Component> T setHover(T component, ItemStack hoverInfo) {

        return setHover(component, HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(hoverInfo));
    }

    public static <T extends Component> T setHover(T component, Entity hoverInfo) {

        if (hoverInfo instanceof AccessorEntity accessor) {

            return setHover(component, accessor.bookshelf$createHoverEvent());
        }

        return setHover(component, HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityTooltipInfo(hoverInfo.getType(), hoverInfo.getUUID(), hoverInfo.getName()));
    }

    public static <T extends Component, AT, A extends HoverEvent.Action<AT>> T setHover(T component, A action, AT arg) {

        return setHover(component, new HoverEvent(action, arg));
    }

    public static <T extends Component> T setHover(T component, HoverEvent hoverInfo) {

        if (component instanceof MutableComponent mutable) {

            mutable.withStyle(style -> style.withHoverEvent(hoverInfo));
        }

        return component;
    }

    /**
     * Recursively applies a custom font to a text component and all of it's children components.
     *
     * @param text The text to apply the font to.
     * @param font The ID of the font to apply.
     * @return A modified text component that has had the font applied.
     */
    public static Component applyFont(Component text, ResourceLocation font) {

        if (text.getContents() == ComponentContents.EMPTY) {

            return text;
        }

        if (text instanceof MutableComponent mutable) {

            mutable.withStyle(style -> style.withFont(font));
        }

        text.getSiblings().forEach(sib -> applyFont(sib, font));
        return text;
    }

    public static Set<ResourceLocation> getRegisteredFonts() {

        if (!Services.PLATFORM.isPhysicalClient()) {

            return Collections.emptySet();
        }

        return ((AccessorFontManager) (((AccessorMinecraft) Minecraft.getInstance()).bookshelf$getFontManager())).bookshelf$getFonts().keySet();
    }


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

    @Nullable
    public static MutableComponent lookupTranslation(String key, Object... args) {

        return lookupTranslation(key, (s, o) -> null, args);
    }

    @Nullable
    public static MutableComponent lookupTranslation(String key, MutableComponent fallback, Object... args) {

        return lookupTranslation(key, (s, o) -> fallback, args);
    }

    @Nullable
    public static MutableComponent lookupTranslation(String key, BiFunction<String, Object[], MutableComponent> fallback, Object... args) {

        if (I18n.exists(key)) {

            return Component.translatable(key, args);
        }

        return fallback != null ? fallback.apply(key, args) : null;
    }

    public static MutableComponent setCopyText(MutableComponent component, String copy) {

        return component.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy)));
    }

    public static MutableComponent textWithCopy(String text) {

        return Component.literal(text).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)));
    }

    public static MutableComponent join(Component separator, Component... toJoin) {

        return join(separator, Arrays.stream(toJoin).iterator());
    }

    public static MutableComponent join(Component separator, Collection<Component> toJoin) {

        return join(separator, toJoin.iterator());
    }

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

    public static Set<String> getPossibleMatches(String input, Iterable<String> candidates) {

        final HashSet<String> bestMatches = new HashSet();
        int distance = Integer.MAX_VALUE;

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