package net.darkhax.bookshelf.lib;

import java.awt.Color;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class Color4f {

    /**
     * The red value as a 0-1 float.
     */
    private float red;

    /**
     * The green value as a 0-1 float.
     */
    private float green;

    /**
     * The blue value as a 0-1 float.
     */
    private float blue;

    /**
     * The alpha value as a 0-1 float.
     */
    private float alpha;

    /**
     * Creates a color with random values.
     *
     * @param alpha Whether or not a random alpha value should be used. If not, a solid color
     *        will be generated.
     */
    public Color4f (boolean alpha) {

        this(Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat(), alpha ? Constants.RANDOM.nextFloat() : 1f);
    }

    /**
     * Creates a color from data on an ItemStack.
     *
     * @param stack The stack to read data from.
     */
    public Color4f (ItemStack stack) {

        this(stack.getTagCompound());
    }

    /**
     * Creates a color from data on an NBTTagCompound.
     *
     * @param tag The tag to read data from.
     */
    public Color4f (NBTTagCompound tag) {

        this(tag.getFloat("red"), tag.getFloat("green"), tag.getFloat("blue"), tag.hasKey("alpha") ? tag.getInteger("alpha") : 1f);
    }

    /**
     * Creates a color from data being sent through a ByteBuf.
     *
     * @param buf The buffer of bytes to read from.
     */
    public Color4f (ByteBuf buf) {

        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    /**
     * Creates a color from an AWT color.
     *
     * @param color The AWT color to use as a base.
     */
    public Color4f (Color color) {

        this(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    /**
     * Creates a color using 0-255 integer range.
     *
     * @param red The red color value.
     * @param green The green color value.
     * @param blue The blue color value.
     * @param alpha The alpha color value.
     */
    public Color4f (int red, int green, int blue, int alpha) {

        this(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    /**
     * Creates a color using 3 floats. Alpha is assumed to be 1f.
     *
     * @param red The red color value.
     * @param green The green color value.
     * @param blue The blue color value.
     */
    public Color4f (float red, float green, float blue) {

        this(red, green, blue, 1f);
    }

    /**
     * Creates a color using 4 0-1 floats.
     *
     * @param red The red color values.
     * @param green The green color value.
     * @param blue The blue color value.
     * @param alpha The alpha color value.
     */
    public Color4f (float red, float green, float blue, float alpha) {

        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setAlpha(alpha);
    }

    /**
     * Stores the color's data on an ItemStack's NBTTagCompound. If that stack does not have
     * one, one will be provided.
     *
     * @param stack The ItemStack to apply color to.
     */
    public void write (ItemStack stack) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        this.write(stack.getTagCompound());
    }

    /**
     * Stores the color's data on an NBTTagCompound.
     *
     * @param tag The tag to write data to.
     */
    public void write (NBTTagCompound tag) {

        tag.setFloat("red", this.red);
        tag.setFloat("green", this.green);
        tag.setFloat("blue", this.blue);
        tag.setFloat("alpha", this.alpha);
    }

    /**
     * Stores the color's data in a ByteBuf.
     *
     * @param buf The byte buffer to write data to.
     */
    public void write (ByteBuf buf) {

        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.alpha);
    }

    /**
     * Checks if the color is white.
     *
     * @return Whether or not the color is white.
     */
    public boolean isWhite () {

        return this.toAWTColor().equals(Color.WHITE);
    }

    /**
     * Gets an AWT Color version of this color. No alpha is carried over.
     *
     * @return The AWT version of the color.
     */
    public Color toAWTColor () {

        return new Color((int) (this.red * 255f), (int) (this.green * 255f), (int) (this.blue * 255f));
    }

    /**
     * Gets the red color value.
     *
     * @return The red color value.
     */
    public float getRed () {

        return this.red;
    }

    /**
     * Sets the red color value.
     *
     * @param red The red color value.
     */
    public void setRed (float red) {

        this.red = red;
    }

    /**
     * Gets the green color value.
     *
     * @return The green color value.
     */
    public float getGreen () {

        return this.green;
    }

    /**
     * Sets the green color value.
     *
     * @param green The green color value.
     */
    public void setGreen (float green) {

        this.green = green;
    }

    /**
     * Gets the blue color value.
     *
     * @return The blue color value.
     */
    public float getBlue () {

        return this.blue;
    }

    /**
     * Sets the blue color value.
     *
     * @param blue The blue color value.
     */
    public void setBlue (float blue) {

        this.blue = blue;
    }

    /**
     * Gets the alpha color value.
     *
     * @return The alpha color value.
     */
    public float getAlpha () {

        return this.alpha;
    }

    /**
     * Sets the alpha color values.
     *
     * @param alpha The alpha color value.
     */
    public void setAlpha (float alpha) {

        this.alpha = alpha;
    }

    @Override
    public String toString () {

        String output = TextFormatting.RED + "" + (int) (this.red * 255) + " " + TextFormatting.GREEN + (int) (this.getGreen() * 255) + " " + TextFormatting.BLUE + (int) (this.blue * 255);

        if (this.alpha < 1.0f) {
            output += " " + TextFormatting.GRAY + (int) (100 - this.alpha * 100);
        }

        return output;
    }
}
