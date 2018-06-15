package net.darkhax.bookshelf.client;

import java.util.Map;
import java.util.TreeMap;

import net.darkhax.bookshelf.BookshelfConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

public class RomanNumerals {

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public final static String toRoman (int number) {

        final int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static void insertRomanNumerals () {

        mapLevels("Enchantment", "enchantment.level.", BookshelfConfig.translateEnchantmentCount);
    }

    private static void mapLevels (String description, String base, int maxLevel) {

        if (maxLevel > 0) {

            final Map<String, String> translations = I18n.i18nLocale.properties;
            final Map<String, String> deprecated = net.minecraft.util.text.translation.I18n.localizedName.languageList;
            final Map<String, String> fallback = net.minecraft.util.text.translation.I18n.fallbackTranslator.languageList;

            final ProgressBar bar = ProgressManager.push("Translating " + description + " Levels", maxLevel - 1);

            for (int lv = 1; lv < maxLevel; lv++) {

                final String roman = toRoman(lv);
                final String key = base + lv;
                bar.step(lv + " = " + roman);

                translations.put(key, roman);
                deprecated.put(key, roman);
                fallback.put(key, roman);
            }

            ProgressManager.pop(bar);
        }
    }
}