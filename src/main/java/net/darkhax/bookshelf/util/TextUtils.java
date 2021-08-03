/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public final class TextUtils {

    /**
     * Applies a font to a text component and all of it's sibling components.
     *
     * @param text The component to apply the font to.
     * @param font The font to apply.
     * @return The text component with he font applied.
     */
    public static Component applyFont (Component text, ResourceLocation font) {

        if (text instanceof MutableComponent) {

            ((MutableComponent) text).setStyle(text.getStyle().withFont(font));
        }

        text.getSiblings().forEach(sib -> applyFont(sib, font));
        return text;
    }

    /**
     * Joins multiple text components together using a separator.
     *
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin An array of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static MutableComponent join (Component separator, Component... toJoin) {

        return join(separator, Arrays.stream(toJoin).iterator());
    }

    /**
     * Joins multiple text components together using a separator.
     *
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin A collection of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static MutableComponent join (Component separator, Collection<Component> toJoin) {

        return join(separator, toJoin.iterator());
    }

    /**
     * Joins multiple text components together using a separator.
     *
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin An iterator of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static MutableComponent join (Component separator, Iterator<Component> toJoin) {

        final TextComponent joined = new TextComponent("");

        while (toJoin.hasNext()) {

            joined.append(toJoin.next());

            if (toJoin.hasNext()) {

                joined.append(separator);
            }
        }

        return joined;
    }
}