/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.darkhax.bookshelf.item.IVariant;
import net.darkhax.bookshelf.world.loot.LootBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Mods can create new instances of this class, and use it to handle a lot of the registration
 * details. The helper methods included handle all of the basic things such as unlocalized
 * names, creative tabs, registry names, and block/itemblock registry, along with some other
 * stuff for models.
 */
public class RegistryHelper {

    /**
     * The id of the mod the registry helper instance belongs to.
     */
    private final String modid;

    /**
     * A list of all items registered by the helper instance.
     */
    private final NonNullList<Item> items = NonNullList.create();

    /**
     * A list of all blocks registered by the helper instance.
     */
    private final NonNullList<Block> blocks = NonNullList.create();

    /**
     * A local map of all the entires that have been added. This is on a per instance basis,
     * used to get mod-specific entries.
     */
    private final Multimap<ResourceLocation, LootBuilder> lootTableEntries = HashMultimap.create();

    /**
     * The creative tab used by the mod. This can be null.
     */
    private CreativeTabs tab;

    /**
     * Constructs a new RegistryHelper for the specified mod id. Multiple helpers can exist
     * with the same id, but it's not recommended.
     *
     * @param modid The modid for the registry helper.
     */
    public RegistryHelper (@Nonnull String modid) {

        this.modid = modid;
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Gets the creative tab for the registry helper.
     *
     * @return The creative tab for the registry helper.
     */
    public CreativeTabs getTab () {

        return this.tab;
    }

    /**
     * Sets the creative tab to be used by the registry helper.
     *
     * @param tab The creative tab to be used by the registry helper.
     * @return The registry helper.
     */
    public RegistryHelper setTab (CreativeTabs tab) {

        this.tab = tab;
        return this;
    }

    /**
     * Gets the id of the mod linked to the registry helper.
     *
     * @return The id of the mod linked to the registry helper.
     */
    public String getModid () {

        return this.modid;
    }

    /**
     * Gets all of the items registered with the helper.
     *
     * @return A NonNullList of items that were registered using the helper.
     */
    public NonNullList<Item> getItems () {

        return this.items;
    }

    /**
     * Gets all of the blocks registered with the helper.
     *
     * @return A NonNullList of blocks that were registered using the helper.
     */
    public NonNullList<Block> getBlocks () {

        return this.blocks;
    }

    /**
     * Registers a block to the game. This will also set the unlocalized name, and creative tab
     * if {@link #tab} has been set. The block will also be cached in {@link #blocks}.
     *
     * @param block The block to register.
     * @param id The id to register the block with.
     * @return The block being registered.
     */
    public Block registerBlock (@Nonnull Block block, @Nonnull String id) {

        return this.registerBlock(block, new ItemBlock(block), id);
    }

    /**
     * Registers a block to the game. This will also set the unlocalized name, and creative tab
     * if {@link #tab} has been set. The block will also be cached in {@link #blocks}.
     *
     * @param block The block to register.
     * @param itemBlock The ItemBlock for the block.
     * @param id The id to register the block with.
     * @return The block being registered.
     */
    public Block registerBlock (@Nonnull Block block, @Nonnull ItemBlock itemBlock, @Nonnull String id) {

        block.setRegistryName(this.modid, id);
        block.setUnlocalizedName(this.modid + "." + id.toLowerCase().replace("_", "."));
        GameRegistry.register(block);
        GameRegistry.register(itemBlock, block.getRegistryName());
        this.blocks.add(block);

        if (this.tab != null) {
            block.setCreativeTab(this.tab);
        }

        return block;
    }

    /**
     * Registers an item to the game. This will also set the unlocalized name, and creative tab
     * if {@link #tab} has been set. The item will also be cached in {@link #items}.
     *
     * @param item The item to register.
     * @param id The id to register the item with.
     * @return The item being registered.
     */
    public Item registerItem (@Nonnull Item item, @Nonnull String id) {

        item.setRegistryName(this.modid, id);
        item.setUnlocalizedName(this.modid + "." + id.toLowerCase().replace("_", "."));
        GameRegistry.register(item);
        this.items.add(item);

        if (this.tab != null) {
            item.setCreativeTab(this.tab);
        }

        return item;
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with
     *        {@link #modid}.
     * @param pool The name of the pool to add the entry to. This pool must already exist.
     * @param weight The weight of the entry.
     * @param item The item to add.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, String name, String pool, int weight, Item item) {

        return this.addLoot(location, new LootBuilder(this.modid + ":" + name, pool, weight, item));
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with
     *        {@link #modid}.
     * @param pool The name of the pool to add the entry to. This pool must already exist.
     * @param weight The weight of the entry.
     * @param quality The quality of the entry. Quality is an optional value which modifies the
     *        weight of an entry based on the player's luck level. totalWeight = weight +
     *        (quality * luck)
     * @param item The item to add.
     * @param conditions A list of loot conditions.
     * @param functions A list of loot functions.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, String name, String pool, int weight, int quality, Item item, List<LootCondition> conditions, List<LootFunction> functions) {

        return this.addLoot(location, new LootBuilder(this.modid + ":" + name, pool, weight, quality, item, conditions, functions));
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param builder The loot builder to add.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, LootBuilder builder) {

        this.lootTableEntries.put(location, builder);
        return builder;
    }

    /**
     * This is an event hook which is used to inject the loot data to loot tables. This class
     * is registered with the event bus using the
     * {@link net.minecraftforge.fml.common.Mod.EventBusSubscriber} class level annotation.
     * Don't call or use this method yourself!
     *
     * @param event The event instance.
     */
    @SubscribeEvent
    public void onTableLoaded (LootTableLoadEvent event) {

        for (final LootBuilder builder : this.lootTableEntries.get(event.getName())) {

            final LootPool pool = event.getTable().getPool(builder.getPool());

            if (pool != null) {

                pool.addEntry(builder.build());
            }

            else {

                Constants.LOG.info(String.format("The mod %s tried to add loot to %s but the pool was not found. %s", this.modid, event.getName(), builder.toString()));
            }
        }
    }

    /**
     * Registers a color handler for a block. This method is client side only, and should be
     * called during the init stage.
     *
     * @param block The block to register the handler for.
     * @param color The color handler to register.
     */
    @SideOnly(Side.CLIENT)
    public void registerColorHandler (@Nonnull Block block, @Nonnull IBlockColor color) {

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, block);
    }

    /**
     * Registers a color handler for an item. This method is client side only, and should be
     * called during the init stage.
     *
     * @param item The item to register the handler for.
     * @param color The color handler to register.
     */
    @SideOnly(Side.CLIENT)
    public void registerColorHandler (@Nonnull Item item, @Nonnull IItemColor color) {

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, item);
    }

    /**
     * Registers an inventory model for a block. The model name is equal to the registry name
     * of the block. Only set for meta 0.
     *
     * @param block The block to register the model for.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Block block) {

        this.registerInventoryModel(Item.getItemFromBlock(block));
    }

    /**
     * Registers an inventory model for a block with variants. The model name is equal to the
     * registry name of the block, plus the variant string for the meta.
     *
     * @param block The block to register models for.
     * @param variants An array of variant names in order of meta.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Block block, @Nonnull String... variants) {

        for (int meta = 0; meta < variants.length; meta++) {
            this.registerInventoryModel(Item.getItemFromBlock(block), meta, block.getRegistryName().toString() + "_" + variants[meta]);
        }
    }

    /**
     * Registers an inventory model for a block.
     *
     * @param block The block to register the model for.
     * @param meta The meta to register the model for.
     * @param modelName The name of the model to register.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Block block, int meta, @Nonnull String modelName) {

        this.registerInventoryModel(Item.getItemFromBlock(block), meta, modelName);
    }

    /**
     * Registers an inventory model for an item.The model name is equal to the registry name of
     * the item. Only set for meta 0.
     *
     * @param item The item to register the model for.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Item item) {

        if (item instanceof IVariant) {

            final IVariant variant = (IVariant) item;
            this.registerInventoryModel(item, variant.getPrefix(), variant.getVariant());
        }
        else {
            this.registerInventoryModel(item, 0, item.getRegistryName().toString());
        }
    }

    /**
     * Registers an inventory model for an item with variants. The model name is equal to the
     * registry name of the item, plus the variant string for the meta.
     *
     * @param item The item to register models for.
     * @param prefix Adds a prefix to each of the model variants.
     * @param variants An array of variant names in order of meta.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Item item, String prefix, @Nonnull String... variants) {

        for (int meta = 0; meta < variants.length; meta++) {
            this.registerInventoryModel(item, meta, item.getRegistryName().toString() + "_" + prefix + variants[meta]);
        }
    }

    /**
     * Registers an inventory model for an item.
     *
     * @param item The item to register the model for.
     * @param meta The meta to register the model for.
     * @param modelName The name of the model to register.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Item item, int meta, @Nonnull String modelName) {

        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelName, "inventory"));
    }
}