package net.darkhax.bookshelf.lib;

import java.awt.Color;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class ColorObject {
    
    private float red = 1.0f;
    private float green = 1.0f;
    private float blue = 1.0f;
    private float alpha = 1.0f;
    
    /**
     * Constructs a new ColorObject that is pure white and fully visible.
     */
    public ColorObject() {
        
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    /**
     * Constructs a new ColorObject from a ByteBuf.
     * 
     * @param buf The ByteBuf to pull the data from.
     */
    public ColorObject(ByteBuf buf) {
        
        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }
    
    /**
     * Constructs a new ColorObject from an AWT Color.
     * 
     * @param color The AWT Color to pull the RGB from.
     */
    public ColorObject(Color color) {
        
        this(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Constructs a new ColorObject with completely random values for RGBA.
     *
     * @param doAlpha If true, the alpha value will also be randomized, by default it is 1.
     */
    public ColorObject(boolean doAlpha) {
        
        this(getRandomColor(), getRandomColor(), getRandomColor(), doAlpha ? getRandomColor() : 1.0f);
    }
    
    /**
     * Constructs a new ColorObject from a NBTTagCompound.
     *
     * @param tag An NBTTagCompound which should have color data written to it.
     */
    public ColorObject(NBTTagCompound tag) {
        
        this.setRed(tag.hasKey("red") ? tag.getFloat("red") : 1.0f);
        this.setGreen(tag.hasKey("green") ? tag.getFloat("green") : 1.0f);
        this.setBlue(tag.hasKey("blue") ? tag.getFloat("blue") : 1.0f);
        this.setAlpha(tag.hasKey("alpha") ? tag.getFloat("alpha") : 1.0f);
    }
    
    /**
     * Converts a decimal color integer like the one produced in getIntFromColor back into a
     * ColorObject.
     *
     * @param rgb The decimal value which represents all of the color data.
     */
    public ColorObject(int rgb) {
        
        this((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
    }
    
    /**
     * Creates a new ColorObject using integers which will be converted back into floats. The
     * alpha value is automatically set to 1. (0-255)
     *
     * @param red The amount of red that makes up this color.
     * @param green The amount of green that makes up this color.
     * @param blue The amount of blue that makes up this color.
     */
    public ColorObject(int red, int green, int blue) {
        
        this((float) red / 255, (float) green / 255, (float) blue / 255, 1.0f);
    }
    
    /**
     * Creates a new ColorObject using integers which will be converted back into floats.
     * (0-255)
     *
     * @param red The amount of red that makes up this color.
     * @param green The amount of green that makes up this color.
     * @param blue The amount of blue that makes up this color.
     * @param alpha The transparency for this color. 0 is fully transparent, while 100 is
     *        completely solid.
     */
    public ColorObject(int red, int green, int blue, int alpha) {
        
        this((float) red / 255, (float) green / 255, (float) blue / 255, (float) alpha / 100);
    }
    
    /**
     * Creates a new ColorObject using the standard RGB color values. Alpha is set to 1.
     *
     * @param red The amount of red that makes up this color.
     * @param green The amount of green that makes up this color.
     * @param blue The amount of blue that makes up this color.
     */
    public ColorObject(float red, float green, float blue) {
        
        this(red, green, blue, 1.0f);
    }
    
    /**
     * Creates a new ColorObject using the standard RGBA color values.
     *
     * @param red The amount of that makes up this color.
     * @param green The amount of that makes up this color.
     * @param blue The amount of that makes up this color.
     * @param alpha The transparency of this color object. 0 is completely see through, 1 is
     *        completely solid.
     */
    public ColorObject(float red, float green, float blue, float alpha) {
        
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    /**
     * Sets the red value for this color object. If the value provided is not appropriate, it
     * will be corrected.
     *
     * @param amount The amount of red to set the value at. 0.0f -> 1.0f
     */
    public void setRed (float amount) {
        
        this.red = amount < 0.0f ? 0.0f : amount > 1.0f ? 1.0f : amount;
    }
    
    /**
     * Retrieves the red value from this color object. If the value stored is not appropriate,
     * it will be corrected.
     *
     * @return float The value of the red color.
     */
    public float getRed () {
        
        return this.red < 0.0f ? 0.0f : this.red > 1.0f ? 1.0f : this.red;
    }
    
    /**
     * Sets the green value for this color object. If the value provided is not appropriate, it
     * will be corrected.
     *
     * @param amount The amount of green to set the value at. 0.0f -> 1.0f
     */
    public void setGreen (float amount) {
        
        this.green = amount < 0.0f ? 0.0f : amount > 1.0f ? 1.0f : amount;
    }
    
    /**
     * Retrieves the green value from this color object. If the value stored is not
     * appropriate, it will be corrected.
     *
     * @return float The value of the green color.
     */
    public float getGreen () {
        
        return this.green < 0.0f ? 0.0f : this.green > 1.0f ? 1.0f : this.green;
    }
    
    /**
     * Sets the blue value for this color object. If the value provided is not appropriate, it
     * will be corrected.
     *
     * @param amount The amount of blue to set the value at. 0.0f -> 1.0f
     */
    public void setBlue (float amount) {
        
        this.blue = amount < 0.0f ? 0.0f : amount > 1.0f ? 1.0f : amount;
    }
    
    /**
     * Retrieves the blue value from this color object. If the value stored is not appropriate,
     * it will be corrected.
     *
     * @return float The value of the blue color.
     */
    public float getBlue () {
        
        return this.blue < 0.0f ? 0.0f : this.blue > 1.0f ? 1.0f : this.blue;
    }
    
    /**
     * Sets the transparency value for this color object. If the value provided is not
     * appropriate, it will be corrected.
     *
     * @param amount The amount of alpha to set the value at. 0.0f -> 1.0f
     */
    public void setAlpha (float amount) {
        
        this.alpha = amount < 0.0f ? 0.0f : amount > 1.0f ? 1.0f : amount;
    }
    
    /**
     * Retrieves the transparency value from this color object. If the value stored is not
     * appropriate, it will be corrected.
     *
     * @return float The value of the transparency color.
     */
    public float getAlpha () {
        
        return this.alpha < 0.0f ? 0.0f : this.alpha > 1.0f ? 1.0f : this.alpha;
    }
    
    /**
     * Creates a new NBTTagCompound from a ColorObject.
     *
     * @return NBTTagCompound A NBTTagCompound containing the RGBA of the ColorObject.
     */
    public NBTTagCompound getTagFromColor () {
        
        return this.writeToTag(new NBTTagCompound());
    }
    
    /**
     * Converts a ColorObject to a decimal color value. This is most notably used by Mojang for
     * item overlay colors and font rendering colors.
     *
     * @return int An Integer which represents all of the color data.
     */
    public int getIntFromColor () {
        
        int rgb = (int) (this.red * 255);
        rgb = (rgb << 8) + (int) (this.green * 255);
        rgb = (rgb << 8) + (int) (this.blue * 255);
        return rgb;
    }
    
    /**
     * Creates a random float value which represents a color.
     *
     * @return float A random float between 0 and 1.
     */
    public static float getRandomColor () {
        
        return Constants.RANDOM.nextFloat();
    }
    
    /**
     * A simple method used to check if a ColorObject is generic. A generic ColorObject is
     * considered a ColorObject that represents White.
     *
     * @return boolean If the ColorObject represents pure white, this method will return true.
     */
    public boolean isGenericWhite () {
        
        return this.isGeneric(1.0f);
    }
    
    /**
     * A method which can be used to check if a ColorObject is generic. A Generic ColorObject,
     * as defined in this method, is a ColorObject which has the same value for R, G and B.
     *
     * @param color The value of the color being checked for the RGB.
     * @return boolean If true, the ColorObject will be considered generic.
     */
    public boolean isGeneric (float color) {
        
        return this.red >= color && this.blue >= color && this.green >= color;
    }
    
    /**
     * Creates a copy of the provided ColorObject, useful when you don't want to mess up
     * existing instances.
     *
     * @return ColorObject A clone of the provided ColorObject;
     */
    public ColorObject copy () {
        
        final ColorObject clone = new ColorObject(false);
        clone.red = this.red;
        clone.green = this.green;
        clone.blue = this.blue;
        clone.alpha = this.alpha;
        
        return clone;
    }
    
    /**
     * Writes the data contained within the ColorObject to the provided NBTTagCompound.
     *
     * @param tag An NBTTagCompound used to write data to. If null is provided, one will be
     *        generated for you.
     * @return NBTTagCompound An NBTTagCompound instance which contains the color data. Can be
     *         used with the NBTTagCompound based constructor to recreate a ColorObject.
     */
    public NBTTagCompound writeToTag (NBTTagCompound tag) {
        
        if (tag == null)
            tag = new NBTTagCompound();
            
        tag.setFloat("red", this.red);
        tag.setFloat("green", this.green);
        tag.setFloat("blue", this.blue);
        tag.setFloat("alpha", this.alpha);
        return tag;
    }
    
    /**
     * A basic wrapper method that handles the creation of new ItemStacks. This method will
     * re-apply the nbt tag compound after it has been set. This prevents null tags from
     * slipping through.
     *
     * @param stack The ItemStack you wish to write to.
     * @return ItemStack The instance of ItemStack that was used.
     */
    public ItemStack writeToItemStack (ItemStack stack) {
        
        stack.setTagCompound(this.writeToTag(stack.getTagCompound()));
        return stack;
    }
    
    /**
     * Writes the ColorObject to a ByteBuf. Useful when sending data through a packet, as it is
     * more compact then a tag compound.
     * 
     * @param buf The ByteBuf to write the ColorObject into.
     */
    public void writeToBuffer (ByteBuf buf) {
        
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.alpha);
    }
    
    @Override
    public String toString () {
        
        String output = TextFormatting.RED + "" + (int) (this.red * 255) + " " + TextFormatting.GREEN + (int) (this.getGreen() * 255) + " " + TextFormatting.BLUE + (int) (this.blue * 255);
        
        if (this.alpha < 1.0f)
            output += " " + TextFormatting.GRAY + (int) (100 - this.alpha * 100);
            
        return output;
    }
    
    @Override
    public boolean equals (Object obj) {
        
        if (obj instanceof ColorObject) {
            
            final ColorObject colorObj = (ColorObject) obj;
            return colorObj.red == this.red && colorObj.green == this.green && colorObj.blue == this.blue && colorObj.alpha == this.alpha;
        }
        
        return false;
    }
}