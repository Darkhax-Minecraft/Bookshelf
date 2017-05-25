/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.awt.Color;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * This class is an extension of the AWT Color class. It adds several things which make working
 * with color in the MC environment less of a hassle.
 */
public class MCColor extends Color {

    /**
     * A serialization ID.
     */
    private static final long serialVersionUID = -6408679408589215496L;

    /**
     * Constructs an MCColor from an ItemStack. Expects the stack to have already been checked
     * for validity.
     *
     * @param stack The ItemStack to construct a color from.
     */
    public MCColor (@Nonnull ItemStack stack) {

        this(stack.getTagCompound());
    }

    /**
     * Constructs an MCColor from a position in the world. Expects the position to have already
     * been checked for validity.
     *
     * @param world The World.
     * @param pos A position in the world.
     */
    public MCColor (IBlockAccess world, BlockPos pos) {

        this(world.getTileEntity(pos));
    }

    /**
     * Constructs an MCColor from a TileEntity. Expects the TileEntity to have already been
     * checked for validity.
     *
     * @param tile The TileEntity to construct a color from.
     */
    public MCColor (TileEntity tile) {

        this(tile.getTileData());
    }

    /**
     * Constructs an MCColor from an NBTTagCompound. Expects the tag to have already been
     * checked for validity.
     *
     * @param tag The NBTTagCompound to construct a color from.
     */
    public MCColor (@Nonnull NBTTagCompound tag) {

        this(tag.getIntArray("Color"));
    }

    /**
     * Constructs an MCColor from an array of color components. Expects the array to have
     * already been checked for validity.
     *
     * @param colors The array to construct a color from.
     */
    public MCColor (@Nonnull int[] colors) {

        this(colors[0], colors[1], colors[2]);
    }

    /**
     * Constructs an MCColor from raw color components.
     *
     * @param red The red component as an integer from 0 to 255.
     * @param green The green component as an integer from 0 to 255.
     * @param blue The blue component as an integer from 0 to 255.
     */
    public MCColor (int red, int green, int blue) {

        super(red, green, blue);
    }

    /**
     * Constructs an MCColor from the hashcode of a string.
     *
     * @param string The string to get a color for.
     */
    public MCColor (String string) {

        this(string.hashCode());
    }

    /**
     * Constructs an MCColor from a packed RGB integer.
     *
     * @param packed A packed RGB integer.
     */
    public MCColor (int packed) {

        super(packed);
    }

    /**
     * Writes the color object's data to the ItemStack's NBTTagCompound.
     *
     * @param stack The ItemStack to write the color data to.
     */
    public void writeToStack (@Nonnull ItemStack stack) {

        this.writeToNBT(stack.getTagCompound());
    }

    /**
     * Writes the color object's data to the NBTTagCompound.
     *
     * @param tag The NBTTagCompound to write the color data to.
     */
    public void writeToNBT (@Nonnull NBTTagCompound tag) {

        tag.setIntArray("Color", new int[] { this.getRed(), this.getGreen(), this.getBlue() });
    }

    /**
     * Gets the components as an integer array.
     *
     * @return The components as an integer array.
     */
    public int[] getComponents () {

        return new int[] { this.getRed(), this.getGreen(), this.getBlue() };
    }

    public float getRedF () {

        return this.getRed() / 255f;
    }

    public float getGreenF () {

        return this.getGreen() / 255f;
    }

    public float getBlueF () {

        return this.getBlue() / 255f;
    }

    public void setRenderColor () {

        GlStateManager.color(this.getRed(), this.getGreen(), this.getBlue());
    }

    /**
     * Creates a random MCColor.
     *
     * @param rand An instance of Random.
     * @return A random MCColor.
     */
    public static MCColor getRandomColor (@Nonnull Random rand) {

        return new MCColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    /**
     * Checks if an ItemStack is acceptable. For an ItemStack to be acceptable, it must not be
     * null or empty, and must have an NBTTagCompound which is deemed acceptable by
     * {@link #isAcceptable(NBTTagCompound)}.
     *
     * @param stack The ItemStack to check.
     * @return Whether or not the ItemStack was acceptable.
     */
    public static boolean isAcceptable (@Nonnull ItemStack stack) {

        return !stack.isEmpty() && stack.hasTagCompound() && isAcceptable(stack.getTagCompound());
    }

    /**
     * Checks if a tile entity at a given position in the world is acceptable. Check
     * {@link #isAcceptable(TileEntity)} for more info.
     *
     * @param world The world to check in.
     * @param pos The pos to check at.
     * @return Whether or not the TileEntity was acceptable.
     */
    public static boolean isAcceptable (@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {

        return isAcceptable(world.getTileEntity(pos));
    }

    /**
     * Checks if a tile entity is acceptable. For a TileEntity to be acceptable, it must not be
     * null or invalid, and must have an NBTTagCompound which is deemed acceptable by
     * {@link #isAcceptable(NBTTagCompound)}.
     *
     * @param tile The TileEntity to check.
     * @return Whether or not the TileEntity was acceptable.
     */
    public static boolean isAcceptable (@Nonnull TileEntity tile) {

        return tile != null && !tile.isInvalid() && isAcceptable(tile.getTileData());
    }

    /**
     * Checks if a NBTTagCompound is acceptable. For an NBTTagCompound to be acceptable, it
     * must not be null, and must have an integer array named Color with 3 elements.
     *
     * @param tag The NBTTagCompound to check.
     * @return Whether or not the ItemStack was acceptable.
     */
    public static boolean isAcceptable (@Nonnull NBTTagCompound tag) {

        return tag.hasKey("Color") && tag.getIntArray("Color").length == 3;
    }
}