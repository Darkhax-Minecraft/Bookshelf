/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;

/**
 * A compilation of utilities and methods for working with banners.
 */
public final class BannerUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private BannerUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Creates a new Banner ItemStack that has all of the patterns in the NBTTagList written to
     * it.
     *
     * @param baseColor The base color for the banner.
     * @param patterns The patterns to apply. This can be null if you want no patterns. See
     *        {@link #createPatternList(BannerLayer...)} for an easy way to make this.
     * @return The ItemStack that was created. All of the data is on the NBT.
     */
    public static ItemStack createBanner (EnumDyeColor baseColor, NBTTagList patterns) {

        return ItemBanner.makeBanner(baseColor, patterns);
    }

    /**
     * Creates an NBTTagList which contains the data for the banner layers passed. Each layer
     * is stored as a String for the ID and an int that represents an EnumDyeColor. The created
     * NBTTagList can be directly used by the Banner TileEntity. Layers are written in the
     * order they are passed.
     *
     * @param layers The layers to write to NBT.
     * @return An NBTTagList that contains all of the banner layers written as tag compound.
     */
    public static NBTTagList createPatternList (BannerLayer... layers) {

        final NBTTagList patterns = new NBTTagList();

        for (final BannerLayer layer : layers) {

            final NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Pattern", layer.pattern.getHashname());
            tag.setInteger("Color", layer.color.getDyeDamage());
            patterns.appendTag(tag);
        }

        return patterns;
    }

    /**
     * Creates a new banner pattern that is not obtainable through vanilla means. An example of
     * such a pattern is the default empty pattern.
     *
     * @param name The name of the banner pattern being created. This is used for the texture
     *        file and unlocalized name. This is also upper cased to use as the Enum value
     *        name.
     * @param id A short ID to represent the banner pattern. It is critical that this value be
     *        unique. Please consider adding the modID into this somehow.
     * @return The pattern that was created.
     */
    public static BannerPattern addBasicPattern (String name, String id) {

        final Class<?>[] paramTypes = { String.class, String.class };
        final Object[] paramValues = { name, id };
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramValues);
    }

    /**
     * Creates a new banner pattern that can only be created by placing a the specified
     * ItemStack into the crafting table as part of the recipe. This pattern can be dyed
     * different colors by adding additional dyes into the recipe.
     *
     * @param name The name of the banner pattern being created. This is used for the texture
     *        file and unlocalized name. This is also upper cased to use as the Enum value
     *        name.
     * @param id A short ID to represent the banner pattern. It is critical that this value be
     *        unique. Please consider adding the modID into this somehow.
     * @param craftingStack The ItemStack to use to create this pattern.
     * @return The pattern that was created.
     */
    public static BannerPattern addCraftingPattern (String name, String id, ItemStack craftingStack) {

        final Class<?>[] paramTypes = { String.class, String.class, ItemStack.class };
        final Object[] paramValues = { name, id, craftingStack };
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramValues);
    }

    /**
     * Creates a new banner pattern that can only be created by placing dyes in the crafting
     * table using a certain arrangement. These recipes only use dye items and can conflict
     * with vanilla. There is roughly 512 possibilities for this.
     *
     * @param name The name of the banner pattern being created. This is used for the texture
     *        file and unlocalized name. This is also upper cased to use as the Enum value
     *        name.
     * @param id A short ID to represent the banner pattern. It is critical that this value be
     *        unique. Please consider adding the modID into this somehow.
     * @param craftingTop A layout for the pattern in the top row of the crafting grid. This is
     *        represented by a string with three characters. A blank space means nothing goes
     *        in that slot, while a # means that a dye would go in that slot. Example: "# #"
     * @param craftingMid A layout for the pattern in the middle row of the crafting grid. This
     *        is represented by a string with three characters. A blank space means nothing
     *        goes in that slot, while a # means that a dye would go in that slot. Example: " #
     *        "
     * @param craftingBot A layout for the pattern in the bottom row of the crafting grid. This
     *        is represented by a string with three characters. A blank space means nothing
     *        goes in that slot, while a # means that a dye would go in that slot. Example:
     *        "###"
     * @return The pattern that was created.
     */
    public static BannerPattern addDyePattern (String name, String id, String craftingTop, String craftingMid, String craftingBot) {

        final Class<?>[] paramTypes = { String.class, String.class, String.class, String.class, String.class };
        final Object[] paramValues = { name, id, craftingTop, craftingMid, craftingBot };
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramValues);
    }

    /**
     * Object that represents a layer in a banner.
     */
    public static class BannerLayer {

        /**
         * The pattern to use for the layer.
         */
        private final BannerPattern pattern;

        /**
         * The color to use for the player.
         */
        private final EnumDyeColor color;

        /**
         * Creates a new layer using the passed pattern and color.
         *
         * @param pattern The pattern for the layer.
         * @param color The color for the layer.
         */
        public BannerLayer (BannerPattern pattern, EnumDyeColor color) {

            this.pattern = pattern;
            this.color = color;
        }

        /**
         * Gets the pattern depicted by the layer.
         *
         * @return The pattern depicted by the layer.
         */
        public BannerPattern getPattern () {

            return this.pattern;
        }

        /**
         * Gets the color of the layer.
         *
         * @return The color of the layer.
         */
        public EnumDyeColor getColor () {

            return this.color;
        }
    }
}