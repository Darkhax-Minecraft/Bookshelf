package net.darkhax.bookshelf.potion;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionBase extends Potion {
    
    /**
     * A ResourceLocation that points to an individual icon to be used when rendering the
     * status HUD. This texture must be 18x. If this is not set, the iconMap will be checked
     * instead.
     */
    private ResourceLocation icon;
    
    /**
     * A ResourceLocation that points to a 144x icon map. The icon map contains 64 18x images
     * to be used when rendering the status HUD. If this is not set, and icon is not set, the
     * iconStack will be checked instead.
     */
    private ResourceLocation iconMap;
    
    /**
     * An ItemStack that is used when rendering the status HUD. The image used is taken from
     * the ItemStack's icon. If this is not set, and icon and iconMap are not set, no texture
     * will be rendered.
     */
    private ItemStack iconStack;
    
    /**
     * The index of the icon within a texture map. By default, this index is only used for
     * effects that make use of the iconMap.
     */
    private int statusIconIndex;
    
    /**
     * A public constructor for Potion effects. This constructor is almost exactly the same as
     * the vanilla Potion effect.
     * 
     * @param location: A string based ID for the Potion. This is usually something like
     *            modID:potionName
     * @param isBadEffect: Whether or not the Potion should be considered negative.
     * @param potionColor: A decimal based color value. See {@link Color#getRGB()}.
     */
    public PotionBase(String location, boolean isBadEffect, int potionColor) {
        
        super(new ResourceLocation(location), isBadEffect, potionColor);
    }
    
    /**
     * Retrieves the ResourceLocation used for the individual icon of this Potion effect. Not
     * all Potion effects will use an individual icon.
     * 
     * @return ResourceLocation: The ResourceLocation used by the Potion effect.
     */
    public ResourceLocation getIcon () {
        
        return icon;
    }
    
    /**
     * Sets the ResourceLocation used for this Potion's effect.
     * 
     * @param icon: The ResourceLocation to set for the effect.
     */
    public void setIcon (ResourceLocation icon) {
        
        this.icon = icon;
    }
    
    /**
     * Retrieves the ResourceLocation icon map. The map is 144 by 144 pixels, containing rows
     * of 18 by 18 icon sprites. Not all Potions will use this map.
     * 
     * @return ResourceLocation: The icon map used by this Potion effect.
     */
    public ResourceLocation getIconMap () {
        
        return iconMap;
    }
    
    /**
     * Sets the ResourceLocation map used for this Potion's effect.
     * 
     * @param iconMap: The icon map to be used for this potion effect.
     */
    public void setIconMap (ResourceLocation iconMap) {
        
        this.iconMap = iconMap;
    }
    
    /**
     * Retrieves the ItemStack used for the icon of this Potion effect. Not all Potion effects
     * will use an icon stack.
     * 
     * @return ItemStack: The ItemStack set for this effect's icon.
     */
    public ItemStack getIconStack () {
        
        return iconStack;
    }
    
    /**
     * Sets the ItemStack used for the icon of this Potion effect.
     * 
     * @param iconStack: The new ItemStack to be used for the effect.
     */
    public void setIconStack (ItemStack iconStack) {
        
        this.iconStack = iconStack;
    }
    
    /**
     * Retrieves the index of this effects icon on the icon map. This index is a combination of
     * the x and y position of the icon on the texture map. Not all Potion effects will have an
     * icon index.
     */
    public int getStatusIconIndex () {
        
        return statusIconIndex;
    }
    
    /**
     * Sets the index of this effects icon on the icon map.
     * 
     * @param statusIconIndex: The new index of the icon for this Potion effect.
     */
    public void setStatusIconIndex (int statusIconIndex) {
        
        this.statusIconIndex = statusIconIndex;
    }
}