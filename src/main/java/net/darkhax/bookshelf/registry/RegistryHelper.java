/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.darkhax.bookshelf.block.IColorfulBlock;
import net.darkhax.bookshelf.block.ITileEntityBlock;
import net.darkhax.bookshelf.item.IColorfulItem;
import net.darkhax.bookshelf.item.ICustomMesh;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.LootBuilder;
import net.darkhax.bookshelf.util.GameUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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
     * A list of all known helpers.
     */
    private static final List<RegistryHelper> HELPERS = NonNullList.create();

    /**
     * The id of the mod the registry helper instance belongs to.
     */
    private final String modid;

    /**
     * A list of all items registered by the helper.
     */
    private final NonNullList<Item> items = NonNullList.create();

    /**
     * A list of all blocks registered by the helper.
     */
    private final NonNullList<Block> blocks = NonNullList.create();

    /**
     * A list of all the sounds registered by the helper.
     */
    private final NonNullList<SoundEvent> sounds = NonNullList.create();

    /**
     * A list of all entities registered by the helper.
     */
    private final NonNullList<EntityEntryBuilder<? extends Entity>> entities = NonNullList.create();

    /**
     * A local map of all the entries that have been added. This is on a per instance basis,
     * used to get mod-specific entries.
     */
    private final Multimap<ResourceLocation, LootBuilder> lootTableEntries = HashMultimap.create();

    /**
     * A list of all the custom mesh definitions.
     */
    private final List<ICustomMesh> customMeshes = NonNullList.create();

    /**
     * A list of all the colored items registered here.
     */
    private final List<Item> coloredItems = NonNullList.create();

    /**
     * A list of all the colored blocks registered here.
     */
    private final List<Block> coloredBlocks = NonNullList.create();

    /**
     * A list of all the tile providers registered here.
     */
    private final List<ITileEntityBlock> tileProviders = NonNullList.create();

    /**
     * The creative tab used by the mod. This can be null.
     */
    private CreativeTabs tab;

    /**
     * The instance of the owning mod.
     */
    private Object modInstance;

    /**
     * The auto registry for the helper.
     */
    private IAutoRegistry autoRegistry;

    /**
     * Constructs a new RegistryHelper. The modid for the helper is equal to that of the active
     * mod container, and auto model registration is enabled.
     */
    public RegistryHelper () {

        this(Loader.instance().activeModContainer().getModId());
    }

    /**
     * Constructs a new RegistryHelper for the specified mod id. Multiple helpers can exist
     * with the same id, but it's not recommended.
     *
     * @param modid The modid for the registry helper.
     * @param autoModels Should models be auto loaded.
     */
    public RegistryHelper (@Nonnull String modid) {

        this.modid = modid;
        HELPERS.add(this);
    }

    /**
     * Enables automatic registration for things like the event bus.
     *
     * @return The RegistryHelper, for convenience.
     */
    public RegistryHelper enableAutoRegistration () {

        this.autoRegistry = this.getNewAutoRegistry();
        MinecraftForge.EVENT_BUS.register(this.autoRegistry);
        return this;
    }

    /**
     * Checks if the registry has automatic registration.
     *
     * @return Whether or not the helper has automatic registration.
     */
    public boolean hasAutoRegistry () {

        return this.autoRegistry != null;
    }

    /**
     * Gets the auto registry instance. Do not confuse with {@link #getNewAutoRegistry()};
     *
     * @return The auto registry for the helper.
     */
    public IAutoRegistry getAutoRegistry () {

        return this.autoRegistry;
    }

    /**
     * Sets the owning mod instance.
     *
     * @param instance An instance of the owning mod.
     */
    public void setModInstance (Object instance) {

        this.modInstance = instance;
    }

    /**
     * Get's the owning mod instance. If none is set, Bookshelf will attempt to auto-get it
     * using Forge's loader.
     *
     * @return The owning mod's instance.
     */
    public Object getModInstance () {

        if (this.modInstance == null) {

            Constants.LOG.error("Registry helper for " + this.modid + " requires a mod instance be set. Attempting to get instance with mod ID. Please ask the mod author to set this themselves.");

            for (final ModContainer container : Loader.instance().getActiveModList()) {

                if (this.modid.equalsIgnoreCase(container.getModId())) {

                    this.modInstance = container.getMod();
                    break;
                }
            }
        }

        return this.modInstance;
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
     * Gets all of the sounds registered with the helper.
     *
     * @return A NonNullList of sound events registered using the helper.
     */
    public NonNullList<SoundEvent> getSounds () {

        return this.sounds;
    }

    /**
     * Gets all of the entities registered with the helper.
     *
     * @return A NonNullList of entities registered using the helper.
     */
    public List<EntityEntryBuilder<? extends Entity>> getEntities () {

        return this.entities;
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
        this.blocks.add(block);

        this.registerItem(itemBlock, id);

        if (this.tab != null) {
            block.setCreativeTab(this.tab);
        }

        if (block instanceof IColorfulBlock) {

            this.coloredBlocks.add(block);
        }

        if (block instanceof ITileEntityBlock) {

            this.tileProviders.add((ITileEntityBlock) block);
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

        return this.registerItem(item, new ResourceLocation(this.modid, id));
    }

    public Item registerItem (@Nonnull Block block, @Nonnull String id) {

        return this.registerItem(new ItemBlock(block), id);
    }

    public Item registerItem (@Nonnull Item item, @Nonnull ResourceLocation id) {

        item.setRegistryName(id);
        item.setUnlocalizedName(id.getResourceDomain().replaceAll("_", ".") + "." + id.getResourcePath().toLowerCase().replace("_", "."));
        this.items.add(item);

        if (this.tab != null) {
            item.setCreativeTab(this.tab);
        }

        if (GameUtils.isClient()) {

            if (item instanceof ICustomMesh) {

                final ICustomMesh mesh = (ICustomMesh) item;
                this.customMeshes.add(mesh);
                ModelLoader.setCustomMeshDefinition(item, mesh.getCustomMesh());
            }

            if (item instanceof IColorfulItem) {

                this.coloredItems.add(item);
            }
        }

        return item;
    }

    /**
     * Registers a new sound with the game. The sound must also exist in the sounds.json file.
     *
     * @param name The name of the sound file. No upper case chars!
     * @return The sound event that was registered.
     */
    public SoundEvent registerSound (String name) {

        final ResourceLocation id = new ResourceLocation(this.modid, name);
        final SoundEvent sound = new SoundEvent(id).setRegistryName(id);
        this.sounds.add(sound);
        return sound;
    }

    /**
     * Registers any sort of entity. Will not have a spawn egg.
     *
     * @param entClass The entity class.
     * @param id The string id for the entity.
     * @return The entity that was registered.
     */
    public <T extends Entity> EntityEntryBuilder<T> registerEntity (Class<T> entClass, String id, int networkId) {

        final EntityEntryBuilder<T> builder = EntityEntryBuilder.create();
        builder.id(new ResourceLocation(this.modid, id), networkId);
        builder.name(this.modid + "." + id);
        builder.entity(entClass);

        this.entities.add(builder);
        return builder;
    }

    /**
     * Registers any sort of entity. Will not have a spawn egg.
     *
     * @param entClass The entity class.
     * @param id The string id for the entity.
     * @return The entity that was registered.
     */
    public <T extends Entity> EntityEntryBuilder<T> registerMob (Class<T> entClass, String id, int networkId, int primary, int seconday) {

        final EntityEntryBuilder<T> builder = EntityEntryBuilder.create();
        builder.id(new ResourceLocation(this.modid, id), networkId);
        builder.name(this.modid + "." + id);
        builder.entity(entClass);
        builder.tracker(64, 1, true);
        builder.egg(primary, seconday);

        this.entities.add(builder);
        return builder;
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with {@link #modid}
     *        .
     * @param pool The name of the pool to add the entry to. This pool must already exist.
     * @param weight The weight of the entry.
     * @param item The item to add.
     * @param meta The metadata for the loot.
     * @param amount The amount of the item to set.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, String name, String pool, int weight, Item item, int meta, int amount) {

        return this.addLoot(location, name, pool, weight, item, meta, amount, amount);
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with {@link #modid}
     *        .
     * @param pool The name of the pool to add the entry to. This pool must already exist.
     * @param weight The weight of the entry.
     * @param item The item to add.
     * @param meta The metadata for the loot.
     * @param min The smallest item size.
     * @param max The largest item size.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, String name, String pool, int weight, Item item, int meta, int min, int max) {

        final LootBuilder loot = this.addLoot(location, name, pool, weight, item, meta);
        loot.addFunction(new SetCount(new LootCondition[0], new RandomValueRange(min, max)));
        return loot;
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with {@link #modid}
     *        .
     * @param pool The name of the pool to add the entry to. This pool must already exist.
     * @param weight The weight of the entry.
     * @param item The item to add.
     * @param meta The metadata for the loot.
     * @return A builder object. It can be used to fine tune the loot entry.
     */
    public LootBuilder addLoot (ResourceLocation location, String name, String pool, int weight, Item item, int meta) {

        final LootBuilder loot = this.addLoot(location, name, pool, weight, item);
        loot.addFunction(new SetMetadata(new LootCondition[0], new RandomValueRange(meta, meta)));
        return loot;
    }

    /**
     * Creates a new loot entry that will be added to the loot pools when a world is loaded.
     *
     * @param location The loot table to add the loot to. You can use
     *        {@link net.minecraft.world.storage.loot.LootTableList} for convenience.
     * @param name The name of the entry being added. This will be prefixed with {@link #modid}
     *        .
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
     * @param name The name of the entry being added. This will be prefixed with {@link #modid}
     *        .
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
     * Gets the loot tables registered for this helper.
     *
     * @return Multimap of registered loot table entries.
     */
    public Multimap<ResourceLocation, LootBuilder> getLootTableEntries () {

        return this.lootTableEntries;
    }

    /**
     * Used to get a new auto registry instance. Only used if {@link #enableAutoRegistration()}
     * is used.
     *
     * @return The new auto registry instance.
     */
    public IAutoRegistry getNewAutoRegistry () {

        return new AutoRegistry(this);
    }

    /**
     * Gets a list of all tile entity providers registered for the handler.
     *
     * @return A list of all the tile entity providers.
     */
    public List<ITileEntityBlock> getTileProviders () {

        return this.tileProviders;
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
     * @param prefix The prefix for the textures. Use empty string for none.
     * @param variants An array of variant names in order of meta.
     */
    @SideOnly(Side.CLIENT)
    public void registerInventoryModel (@Nonnull Block block, @Nonnull String prefix, @Nonnull String... variants) {

        for (int meta = 0; meta < variants.length; meta++) {
            this.registerInventoryModel(Item.getItemFromBlock(block), meta, block.getRegistryName().toString() + "_" + (prefix.isEmpty() ? prefix : prefix + "_") + variants[meta]);
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
    public void registerInventoryModel (@Nonnull Item item, @Nonnull String prefix, @Nonnull String... variants) {

        for (int meta = 0; meta < variants.length; meta++) {
            this.registerInventoryModel(item, meta, item.getRegistryName().toString() + "_" + (prefix.isEmpty() ? prefix : prefix + "_") + variants[meta]);
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

    /**
     * Gets a list of all items registered that have custom color handlers.
     *
     * @return A list of all colored items.
     */
    @SideOnly(Side.CLIENT)
    public List<Item> getColoredItems () {

        return this.coloredItems;
    }

    /**
     * Gets a list of all registered blocks that have custom color handlers.
     *
     * @return A list of all colored blocks.
     */
    @SideOnly(Side.CLIENT)
    public List<Block> getColoredBlocks () {

        return this.coloredBlocks;
    }

    /**
     * Provides a list of all known registry helpers.
     *
     * @return A list of all known registry helpers.
     */
    public static List<RegistryHelper> getAllHelpers () {

        return HELPERS;
    }
}