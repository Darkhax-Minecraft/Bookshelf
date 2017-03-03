package net.darkhax.bookshelf.lib.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.util.text.TextFormatting;

public class TextUtils {

    /**
     * A map for getting a ChatFormat to be quickly retrieved based on its character value.
     */
    private static final Map<Character, ChatFormat> CHARACTER_TO_FORMAT = new HashMap<>();

    /**
     * A map for getting a ChatFormat quickly by using its name.
     */
    private static final Map<String, ChatFormat> NAME_TO_FORMAT = new HashMap<>();

    /**
     * The prefix character used for formatting codes.
     */
    public static final char FORMAT_PREFIX = '\u00a7';

    /**
     * Pattern for identifying format codes.
     */
    public static final Pattern FORMAT_PATTERN = Pattern.compile("(?i)" + String.valueOf(FORMAT_PREFIX) + "[0-9A-FK-OR]");

    /**
     * This method will take a string and break it down into multiple lines based on a provided
     * line length. The separate strings are then added to the list provided. This method is
     * useful for adding a long description to an item tool tip and having it wrap. This method
     * is similar to wrap in Apache WordUtils however it uses a List making it easier to use
     * when working with Minecraft.
     *
     * @param string: The string being split into multiple lines. It's recommended to use
     *        StatCollector.translateToLocal() for this so multiple languages will be
     *        supported.
     * @param lnLength: The ideal size for each line of text.
     * @param wrapLongWords: If true the ideal size will be exact, potentially splitting words
     *        on the end of each line.
     * @param list: A list to add each line of text to. An good example of such list would be
     *        the list of tooltips on an item.
     * @return List: The same List instance provided however the string provided will be
     *         wrapped to the ideal line length and then added.
     */
    public static List<String> wrapStringToList (String string, int lnLength, boolean wrapLongWords, List<String> list) {

        final String lines[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);
        list.addAll(Arrays.asList(lines));
        return list;
    }

    /**
     * This method will take a string and break it down into multiple lines based on a provided
     * line length. The separate strings are then added to the list provided. This method is
     * useful for adding a long description to an item tool tip and having it wrap. This method
     * is similar to wrap in Apache WordUtils however it uses a List making it easier to use
     * when working with Minecraft.
     *
     * @param string: The string being split into multiple lines. It's recommended to use
     *        StatCollector.translateToLocal() for this so multiple languages will be
     *        supported.
     * @param lnLength: The ideal size for each line of text.
     * @param wrapLongWords: If true the ideal size will be exact, potentially splitting words
     *        on the end of each line.
     * @param format: A list to add each line of text to. An good example of such list would be
     *        the list of tooltips on an item.
     * @param color: An TextFormatting to apply to all lines added to the list.
     * @return List: The same List instance provided however the string provided will be
     *         wrapped to the ideal line length and then added.
     */
    public static List<String> wrapStringToListWithFormat (String string, int lnLength, boolean wrapLongWords, List<String> list, TextFormatting format) {

        final String lines[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);

        for (final String line : lines) {
            list.add(format + line);
        }

        return list;
    }

    /**
     * Strips all formatting from a piece of text.
     *
     * @param text The text to strip formatting from.
     * @return The text with all formatting stripped.
     */
    public static String stripFormatting (String text) {

        return text == null ? null : FORMAT_PATTERN.matcher(text).replaceAll("");
    }

    /**
     * Gets a ChatFormat by its formatting character.
     *
     * @param character The character to look for.
     * @return The ChatFormat that was found. May be null.
     */
    public static ChatFormat getValueByCharacter (char character) {

        return CHARACTER_TO_FORMAT.get(character);
    }

    /**
     * Gets a ChatFormat by its lowercase name.
     *
     * @param name The name to look for.
     * @return The ChatFormat that was found, can be null if nothing was found.
     */
    public static ChatFormat getValueByName (String name) {

        return NAME_TO_FORMAT.get(name.toLowerCase());
    }

    /**
     * Adds a format code to a string. Will add the resent format character to the end.
     *
     * @param string The string to format.
     * @param format The format to apply.
     * @return The input string with the new format codes.
     */
    public static String formatString (String string, ChatFormat format) {

        return format + string + ChatFormat.RESET;
    }

    public enum ChatFormat {

        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        OBFUSCATED('k', true),
        BOLD('l', true),
        STRIKETHROUGH('m', true),
        UNDERLINE('n', true),
        ITALIC('o', true),
        RESET('r');

        /**
         * The format code used by the format.
         */
        private final char formatCode;

        /**
         * Whether or not the format is for styling.
         */
        private final boolean isStyle;

        /**
         * The text used to apply this formatting to client side text.
         */
        private final String formatText;

        /**
         * Constructs a color format for a color.
         *
         * @param character The character code used for the format.
         */
        private ChatFormat (char character) {

            this(character, false);
        }

        /**
         * Constructs a styling format.
         *
         * @param character The character code used for the format.
         * @param isStyled Whether or not the this is a styling.
         */
        private ChatFormat (char character, boolean isStyled) {

            this.formatCode = character;
            this.isStyle = isStyled;
            this.formatText = "\u00a7" + character;
        }

        /**
         * Gets the character used by the formatting.
         *
         * @return The character used by the formatting.
         */
        public char getFormattingCode () {

            return this.formatCode;
        }

        /**
         * Checks if the format is styled.
         *
         * @return Whether or not the format is styled.
         */
        public boolean isStyled () {

            return this.isStyle;
        }

        /**
         * Checks if this is a color or not. A color is not styled or reset.
         *
         * @return Whether or not the chat format is a color.
         */
        public boolean isColor () {

            return !this.isStyle && this != RESET;
        }

        /**
         * Gets the name of the chat format as a lowercase string.
         *
         * @return An all lowercase name for the formatting.
         */
        public String getName () {

            return this.name().toLowerCase();
        }

        @Override
        public String toString () {

            return this.formatText;
        }
    }
}