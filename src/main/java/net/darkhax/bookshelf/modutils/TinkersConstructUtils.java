package net.darkhax.bookshelf.modutils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.lib.util.Utilities;

public class TinkersConstructUtils {
    
    /**
     * Access to the Tinkers Construct materials tab. This tab is used for ingots, patterns,
     * books and other materials.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabMaterials = Utilities.getTabFromLabel("TConstructMaterials");
    
    /**
     * Access to the Tinkers Construct tools tab. This tab is used for all of the tools that
     * can be created using the mod. Tools of new materials are automatically added here.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabTools = Utilities.getTabFromLabel("TConstructTools");
    
    /**
     * Access to the Tinkers Construct parts tab. This tab is used for all of the tool parts in
     * the mod. Parts from new materials will be added automatically.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabToolParts = Utilities.getTabFromLabel("TConstructParts");
    
    /**
     * Access to the Tinkers Construct blocks tab. This tab is used for all of the various
     * blocks added by the mod, including fluids and crafting stations.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabBlocks = Utilities.getTabFromLabel("TConstructBlocks");
    
    /**
     * Access to the Tinkers Construct equipables tab. This tab is used for items that can be
     * equipped, such as the travelers armor.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabEquipables = Utilities.getTabFromLabel("TConstructEquipables");
    
    /**
     * Access to the Tinkers Construct weapons tab. This tab is primarily used for projectile
     * based weapons.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs tabWeaponry = Utilities.getTabFromLabel("TConstructWeaponry");
    
    /**
     * Adds a fluid to the list of valid smeltery fuels.
     * 
     * @param fluidName: The fluidName of the fluid to become a smeltery fuel.
     * @param temperature: How hot this new fuel will be.
     * @param duration: How long the new fuel will last.
     */
    public static void addSmelteryFuel (String fluidName, int temperature, int duration) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("FluidName", fluidName);
        tag.setInteger("Temperature", temperature);
        tag.setInteger("Duration", duration);
        FMLInterModComms.sendMessage("TConstruct", "addSmelteryFuel", tag);
    }
    
    /**
     * Adds a new material to the Tinkers Construct material registry. This will create a new
     * material, which can be used by the Tinkers Construct mod, and it's various systems.
     * 
     * @param id: A numeric ID to associate this fluid with. This is used to reference the
     *            material in other methods such as the addMaterialItem method. The value must
     *            be between Integer.MIN_VALUE and Integer.MAX_VALUE.
     * @param name: A name to associate with this material.
     * @param localization: A localization string used for this material. This is used in the
     *            display name on various tool parts.
     * @param durability: A base durability value for tools of this material.
     * @param miningSpeed: A base mining speed for tools of this material.
     * @param harvestLevel: A base harvest level for tools of this material.
     * @param attack: A base attack value for tools of this material.
     * @param handleModifier: A stat modifier provided by this handle. Less than 1 will reduce
     *            various stats, and greater than 1 will improve them. This mainly effects the
     *            durability of the tool.
     * @param bowProjectileSpeed: A base speed for bow projectiles of this material.
     * @param bowDrawSpeed: A base draw speed for bows of this material.
     * @param projectileMass: A base mass for projectiles of this material.
     * @param projectileFragility: A base fragility of projectiles made of this material.
     * @param style: A style effect used for this material type. See EnumChatFormatting
     * @param color: An integer representation of an RGB color.
     */
    public static void addMaterial (int id, String name, String localization, int durability, int miningSpeed, int harvestLevel, int attack, float handleModifier, float bowProjectileSpeed, int bowDrawSpeed, float projectileMass, float projectileFragility, String style, int color) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Id", id);
        tag.setString("Name", name);
        tag.setString("localizationString", localization);
        tag.setInteger("Durability", durability);
        tag.setInteger("MiningSpeed", miningSpeed);
        tag.setInteger("HarvestLevel", harvestLevel);
        tag.setInteger("Attack", attack);
        tag.setFloat("HandleModifier", handleModifier);
        tag.setFloat("Bow_ProjectileSpeed", bowProjectileSpeed);
        tag.setInteger("Bow_DrawSpeed", bowDrawSpeed);
        tag.setFloat("Projectile_Mass", projectileMass);
        tag.setFloat("Projectile_Fragility", projectileFragility);
        tag.setString("Style", style);
        tag.setInteger("Color", color);
        FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
    }
    
    /**
     * Registers a fluid as a valid casting fluid. This is used to enable a material to be
     * casted into tool parts.
     * 
     * @param fluidName: The fluidName of the fluid to allow casting for.
     * @param materialID: A numeric ID that represents the material you want to be casted by
     *            the fluid.
     */
    public static void addPartCastinMaterial (String fluidName, int materialID) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("FluidName", fluidName);
        tag.setInteger("MaterialId", materialID);
        FMLInterModComms.sendMessage("TConstruct", "addPartCastingMaterial", tag);
    }
    
    /**
     * Associates an Item or Block with a Tinkers Construct material. This is used to add
     * things like the shards in Tinkers Construct.
     * 
     * @param materialID: A numeric ID that represents the material you want to associate the
     *            Item or Block with.
     * @param stack: An ItemStack containing the Item or Block you wish to associate.
     * @param value: The value of the item being registered. Shards have a value of one.
     */
    public static void addMaterialItem (int materialID, ItemStack stack, int value) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("MaterialId", materialID);
        tag.setTag("Item", stack.writeToNBT(new NBTTagCompound()));
        tag.setInteger("Value", value);
        FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
    }
    
    /**
     * Adds a new input item or block for the smeltery. This will make a specified item, melt
     * into the specified fluid.
     * 
     * @param fluidName: The name of the fluid to be created by melting this item.
     * @param input: An ItemStack that will be accepted as an input for the smeltery.
     * @param displayBlock: A block to use when rendering the unmelted input inside of the
     *            smeltery structure.
     * @param amount: The amount of the fluid to generate per input. This is in mb(milibucket).
     * @param temperature: The amount of heat required to melt the input item.
     */
    public static void addMeltingItem (String fluidName, ItemStack input, ItemStack displayBlock, int amount, int temperature) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("FluidName", fluidName);
        tag.setTag("Item", input.writeToNBT(new NBTTagCompound()));
        tag.setTag("Block", displayBlock.writeToNBT(new NBTTagCompound()));
        tag.setInteger("Amount", amount);
        tag.setInteger("Temperature", temperature);
        FMLInterModComms.sendMessage("TConstruct", "addSmelteryMelting", tag);
    }
    
    /**
     * Adds a new FluxBatter to the list of battery modifiers in Tinkers Construct. A battery
     * item is any Item which implements IEnergyContainerItem from cofh.api.energy. Flux
     * Batteries are used in Tinkers Construct to give a tool the Flux modifier. A tool with
     * the Flux modifier will use RF energy instead of durability. The size of the
     * IEnergyContainerItem determines how much energy the tool can hold.
     * 
     * @param stack: An ItemStack containing the IEnergyContainerItem.
     */
    public static void addFluxBattery (ItemStack stack) {
        
        FMLInterModComms.sendMessage("TConstruct", "addFluxBattery", stack);
    }
}
