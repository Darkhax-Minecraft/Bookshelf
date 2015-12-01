package net.darkhax.bookshelf.buff;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.RenderUtils;

public class Buff {
    
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
     * The unlocalized name of this Buff. It is encouraged to prefix this with your modid.
     * example: modid.potionName
     */
    private String potionName;
    
    /**
     * Whether or not this potion is considered to be bad.
     */
    private boolean isBad;
    
    /**
     * The color to use for this effect. This is an RGB integer.
     */
    private int color;
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting the potion to
     * render in the status HUD by using a single 18x texture.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param icon: A ResourceLocation that points to a 18x icon texture that represents this
     *            potion.
     */
    public Buff(String potionName, boolean isBad, int color, ResourceLocation icon) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.icon = icon;
    }
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting the potion to
     * use a texture map when rendering. This option allows for multiple icons to be stored in
     * the same texture. An example texture map that follows the specifications can be found in
     * assets/bookshelf/textures/inventory/example_potion_map.png, which has all of the index
     * values labeled. Feel free to re-use this map in your own project, and replace sprites as
     * you go.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param iconMap: A ResourceLocation that points to a 144x texture map, which contains 64
     *            18x sprites.
     * @param iconIndex: The sprite to use on the map. 0 is the first sprite, and 64 is the
     *            last. The formula used when rendering is X = index % 8 * 18 and Y = index / 8
     *            * 18. The appropriate indexes are also labeled in the example map.
     */
    public Buff(String potionName, boolean isBad, int color, ResourceLocation iconMap, int iconIndex) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.iconMap = iconMap;
        this.setIconIndex(iconIndex % 8, iconIndex / 8);
    }
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting an ItemStack to
     * be used when rendering the potion icon in the status HUD.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param iconStack: An ItemStack that is used to grab an icon, which will be used when
     *            rendering the potion in the status HUD.
     */
    public Buff(String potionName, boolean isBad, int color, ItemStack iconStack) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.iconStack = iconStack;
    }
    
    /**
     * A very basic constructor, which allows for you to expand upon this and make your own
     * Buffs. There is no support for rendering provided by this constructor.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     */
    public Buff(String potionName, boolean isBad, int color) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
    }
    
    /**
     * Sets the statsIconIndex for the effect. This only needs to be done if you plan on using
     * a sprite map.
     * 
     * @param x: The X position of the icon on the map.
     * @param y: The Y position of the icon on the map.
     */
    private void setIconIndex (int x, int y) {
        
        this.statusIconIndex = x + y * 8;
    }
    
    /**
     * A check to see if this Buff should call the onBuffTick ever tick. By default, it is
     * false.
     * 
     * @return boolean: Whether or not this buff should tick every tick.
     */
    public boolean canUpdate () {
        
        return false;
    }
    
    /**
     * This method is called every time an inflicted entity updates, provided that canUpdate is
     * true. This will also be called when the buff is initially applied to the entity,
     * regardless of canUpdate.
     * 
     * @param world: An instance of the World that this is taking place in.
     * @param entity: The entity that has the effect.
     * @param duration: The remaining duration of the effect.
     * @param power: The power of the effect.
     */
    public void onBuffTick (World world, EntityLivingBase entity, int duration, int power) {
    
    }
    
    // TODO Comment
    public void onEffectEnded () {
    
    }
    
    /**
     * A check to see whether or not this effect can be cured. By default, only milk can cure
     * these effects.
     * 
     * @param entity: The entity being cured.
     * @param stack: The ItemStack being used to cure the entity.
     * @return boolean: Whether or not the cure was successful.
     */
    public boolean shouldBeCured (EntityLivingBase entity, ItemStack stack) {
        
        return (ItemStackUtils.isValidStack(stack) && stack.getItem() == Items.milk_bucket);
    }
    
    /**
     * A check to determine whether or not the text for this effect should be rendered in the
     * inventory.
     * 
     * @param effect: An instance of the BuffEffect in place.
     * @return boolean: True if it should render, false if it should not.
     */
    public boolean shouldRenderInvText (BuffEffect effect) {
        
        return true;
    }
    
    /**
     * Retrieves the statusIconIndex for this effect. Typically used for the iconMap.
     * 
     * @return int: An integer, referring to the position of this Buff's texture on a 2D texture
     *         map.
     */
    public int getStatusIconIndex () {
        
        return this.statusIconIndex;
    }
    
    /**
     * Retrieves the basic name of the Buff, not translated, or the unlocalized key.
     * 
     * @return String: The name set for this Buff.
     */
    public String getPotionName () {
        
        return potionName;
    }
    
    /**
     * Provides the translated name for this effect. If no entry is found, the raw key will be
     * returned.
     * 
     * @return String: The name of the potion, translated using the user set language file. If
     *         no name is found, the raw key will be used.
     */
    public String getTranslatedName () {
        
        return StatCollector.translateToLocal("potion." + this.potionName + ".name");
    }
    
    /**
     * A check to see if this effect has an Icon which can be rendered. By default, this will
     * only return true if the effect has an icon, iconMap, iconStack or a statusIconIndex
     * greater than or equal to 0.
     * 
     * @return boolean: Whether or not this Buff has an icon to render.
     */
    public boolean hasStatusIcon () {
        
        return this.icon != null || this.iconStack != null || this.iconMap != null || this.statusIconIndex >= 0;
    }
    
    /**
     * Retrieves the ResourceLocation for the icon of this Buff.
     * 
     * @return ResourceLocation: The icon for this buff.
     */
    public ResourceLocation getStatusIcon () {
        
        return this.icon;
    }
    
    /**
     * Retrieves the ResourceLocation for the iconMap used by this buff.
     * 
     * @return ResourceLocation: The iconMap used by this buff.
     */
    public ResourceLocation getIconMap () {
        
        return this.iconMap;
    }
    
    /**
     * Retrieves the iconStack used as this Buffs icon.
     * 
     * @return ItemStack: An ItemStack used as an icon.
     */
    public ItemStack getIconStack () {
        
        return this.iconStack;
    }
    
    /**
     * Determines whether or not this effect is negative.
     * 
     * @return boolean: Is this effect negative.
     */
    public boolean isEffectBad () {
        
        return this.isBad;
    }
    
    /**
     * Retrieves the color associated with this effect.
     * 
     * @return int: An RGB integer which represents the color associated with this effect.
     */
    public int getColor () {
        
        return this.color;
    }
    
    /**
     * A render call which is used to render this effect within the inventory, and possible
     * other places. This method is only accessible client side. By default, there is only
     * support for individual icons, an icon map, and an ItemStack.
     * 
     * @param x: The X coordinate to render at. An offset of 6 is recommended for most
     *            situations.
     * @param y: The Y coordinate to render at. An offset of 7 is recommended for most
     *            situations.
     * @param effect: An instance of the BuffEffect object.
     * @param mc: An instance of Minecraft, for convenience.
     */
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect (int x, int y, BuffEffect effect, Minecraft mc) {
        
        if (this.icon != null) {
            
            mc.renderEngine.bindTexture(this.icon);
            RenderUtils.drawTextureModalRectSize(x + 6, y + 7, 0, 0, 18, 18, 18, 1f);
        }
        
        else if (ItemStackUtils.isValidStack(this.iconStack)) {
            
            Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(1));
            mc.currentScreen.drawTexturedModelRectFromIcon(x + 8, y + 8, this.iconStack.getIconIndex(), 16, 16);
        }
        
        else if (this.iconMap != null) {
            
            int index = this.getStatusIconIndex();
            mc.renderEngine.bindTexture(this.iconMap);
            RenderUtils.drawTextureModalRectSize(x + 6, y + 7, index % 8 * 18, index / 8 * 18, 18, 18, 144, 1f);
        }
    }
}