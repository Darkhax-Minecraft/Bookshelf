package net.darkhax.bookshelf.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.loot.modifier.SerializerFactory;
import net.darkhax.bookshelf.util.LootUtils;
import net.darkhax.bookshelf.util.MCJsonUtils;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.ILootCondition.AbstractSerializer;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHelper {
    
    protected final String modid;
    protected final Logger logger;
    
    @Nullable
    protected final ItemGroup group;
    
    /**
     * Creates a new registry helper for the given side the mod is running on.
     * 
     * @param modid The mod id.
     * @param logger A logger for the mod.
     * @param group The item group if you have one.
     * @return A registry helper that has been created for the given side.
     */
    public static RegistryHelper create (String modid, Logger logger, @Nullable ItemGroup group) {
        
        return DistExecutor.runForDist( () -> () -> new RegistryHelperClient(modid, logger, group), () -> () -> new RegistryHelper(modid, logger, group));
    }
    
    public RegistryHelper(String modid, Logger logger, @Nullable ItemGroup group) {
        
        this.modid = modid;
        this.logger = logger;
        this.group = group;
    }
    
    public void initialize (IEventBus modBus) {
        
        if (!this.blocks.isEmpty()) {
            
            modBus.addGenericListener(Block.class, this::registerBlocks);
        }
        
        if (!this.items.isEmpty()) {
            
            modBus.addGenericListener(Item.class, this::registerItems);
        }
        
        if (!this.tileEntityTypes.isEmpty()) {
            
            modBus.addGenericListener(TileEntityType.class, this::registerTileEntities);
        }
        
        if (!this.recipeSerializers.isEmpty() || !this.recipeTypes.isEmpty()) {
            
            modBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeTypes);
        }
        
        if (!this.containers.isEmpty()) {
            
            modBus.addGenericListener(ContainerType.class, this::registerContainerTypes);
        }
        
        if (!this.commands.isEmpty()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        }
        
        if (!this.entityTypes.isEmpty()) {
            
            modBus.addGenericListener(EntityType.class, this::registerEntityTypes);
        }
        
        if (!this.biomeProviderTypes.isEmpty()) {
            
            modBus.addGenericListener(BiomeProviderType.class, this::registerBiomeProviders);
        }
        
        if (!this.chunkGeneratorTypes.isEmpty()) {
            
            modBus.addGenericListener(ChunkGeneratorType.class, this::registerChunkGeneratorTypes);
        }
        
        if (!this.potions.isEmpty()) {
            
            modBus.addGenericListener(Potion.class, this::registerPotionTypes);
        }
        
        if (!this.trades.isEmpty()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::registerVillagerTrades);
        }
        
        if (!this.basicTrades.isEmpty() || !this.rareTrades.isEmpty()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::registerWanderingTrades);
        }
        
        if (!this.commandArguments.isEmpty()) {
            
            modBus.addListener(this::registerCommandArguments);
        }
        
        if (!this.lootConditions.isEmpty()) {
            
            modBus.addListener(this::registerLootConditions);
        }
        
        if (!this.injectionTables.isEmpty()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::loadTableInjections);
        }
        
        if (!this.globalModifierSerializers.isEmpty()) {
            
            modBus.addGenericListener(GlobalLootModifierSerializer.class, this::registerGlobalLootModifierSerializers);
        }
        
        if (!this.enchantments.isEmpty()) {
            
            modBus.addGenericListener(Enchantment.class, this::registerEnchantments);
        }
        
        if (!this.paintings.isEmpty()) {
            
            modBus.addGenericListener(PaintingType.class, this::registerPaintings);
        }
    }
    
    /**
     * BLOCKS
     */
    private final List<Block> blocks = NonNullList.create();
    
    private void registerBlocks (Register<Block> event) {
        
        if (!this.blocks.isEmpty()) {
            
            this.logger.info("Registering {} blocks.", this.blocks.size());
            final IForgeRegistry<Block> registry = event.getRegistry();
            
            for (final Block block : this.blocks) {
                registry.register(block);
            }
        }
    }
    
    public Block registerBlock (Block block, String id) {
        
        return this.registerBlock(block, new BlockItem(block, new Item.Properties().group(this.group)), id);
    }
    
    public Block registerBlock (Block block, BlockItem item, String id) {
        
        block.setRegistryName(new ResourceLocation(this.modid, id));
        this.blocks.add(block);
        this.registerItem(item, id);
        return block;
    }
    
    public List<Block> getBlocks () {
        
        return ImmutableList.copyOf(this.blocks);
    }
    
    /**
     * ITEMS
     */
    private final List<Item> items = NonNullList.create();
    
    private void registerItems (Register<Item> event) {
        
        if (!this.items.isEmpty()) {
            
            this.logger.info("Registering {} items.", this.items.size());
            final IForgeRegistry<Item> registry = event.getRegistry();
            
            for (final Item item : this.items) {
                registry.register(item);
            }
        }
    }
    
    public Item registerItem (Item item, String id) {
        
        item.setRegistryName(new ResourceLocation(this.modid, id));
        item.group = this.group;
        this.items.add(item);
        return item;
    }
    
    public List<Item> getItems () {
        
        return ImmutableList.copyOf(this.items);
    }
    
    /**
     * TILE ENTITIES
     */
    private final List<TileEntityType<?>> tileEntityTypes = NonNullList.create();
    
    public <T extends TileEntity> TileEntityType<T> registerTileEntity (Supplier<T> factory, String id, Block... blocks) {
        
        final TileEntityType<T> tileEntityType = TileEntityType.Builder.create(factory, blocks).build(null);
        tileEntityType.setRegistryName(this.modid, id);
        this.tileEntityTypes.add(tileEntityType);
        return tileEntityType;
    }
    
    private void registerTileEntities (Register<TileEntityType<?>> event) {
        
        if (!this.tileEntityTypes.isEmpty()) {
            
            this.logger.info("Registering {} tile entity types.", this.tileEntityTypes.size());
            final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
            
            for (final TileEntityType<?> tileEntityType : this.tileEntityTypes) {
                registry.register(tileEntityType);
            }
        }
    }
    
    public List<TileEntityType<?>> getTileEntities () {
        
        return ImmutableList.copyOf(this.tileEntityTypes);
    }
    
    /**
     * RECIPE TYPES
     */
    private final List<IRecipeType<?>> recipeTypes = NonNullList.create();
    private final List<IRecipeSerializer<?>> recipeSerializers = NonNullList.create();
    
    public <T extends IRecipe<?>> IRecipeType<T> registerRecipeType (String typeId) {
        
        final IRecipeType<T> type = IRecipeType.register(typeId);
        this.recipeTypes.add(type);
        return type;
    }
    
    public <T extends IRecipe<?>> IRecipeSerializer<T> registerRecipeSerializer (IRecipeSerializer<T> serializer, String id) {
        
        this.recipeSerializers.add(serializer);
        serializer.setRegistryName(new ResourceLocation(this.modid, id));
        return serializer;
    }
    
    private void registerRecipeTypes (Register<IRecipeSerializer<?>> event) {
        
        if (!this.recipeTypes.isEmpty()) {
            
            this.logger.info("Registering {} recipe types.", this.recipeTypes.size());
            
            for (final IRecipeType<?> recipeType : this.recipeTypes) {
                
                Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(recipeType.toString()), recipeType);
            }
        }
        
        if (!this.recipeSerializers.isEmpty()) {
            
            this.logger.info("Registering {} recipe serializers.", this.recipeSerializers.size());
            
            final IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
            
            for (final IRecipeSerializer<?> serializer : this.recipeSerializers) {
                
                registry.register(serializer);
            }
        }
    }
    
    public List<IRecipeType<?>> getRecipeTypes () {
        
        return ImmutableList.copyOf(this.recipeTypes);
    }
    
    public List<IRecipeSerializer<?>> getRecipeSerializers () {
        
        return ImmutableList.copyOf(this.recipeSerializers);
    }
    
    /**
     * CONTAINERS
     */
    private final List<ContainerType<?>> containers = NonNullList.create();
    
    public <T extends Container> ContainerType<T> registerContainer (ContainerType.IFactory<T> factory, String id) {
        
        return this.registerContainer(new ContainerType<>(factory), id);
    }
    
    public <T extends Container> ContainerType<T> registerContainer (ContainerType<T> type, String id) {
        
        type.setRegistryName(this.modid, id);
        this.containers.add(type);
        return type;
    }
    
    protected void registerContainerTypes (Register<ContainerType<?>> event) {
        
        if (!this.containers.isEmpty()) {
            
            this.logger.info("Registering {} containers.", this.containers.size());
            
            final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
            
            for (final ContainerType<?> containerType : this.containers) {
                
                registry.register(containerType);
            }
        }
    }
    
    public List<ContainerType<?>> getContainers () {
        
        return ImmutableList.copyOf(this.containers);
    }
    
    /**
     * COMMANDS
     */
    private final List<LiteralArgumentBuilder<CommandSource>> commands = NonNullList.create();
    
    public LiteralArgumentBuilder<CommandSource> registerCommand (LiteralArgumentBuilder<CommandSource> command) {
        
        this.commands.add(command);
        return command;
    }
    
    private void registerCommands (FMLServerStartingEvent event) {
        
        if (!this.commands.isEmpty()) {
            
            this.logger.info("Registering {} commands.", this.commands.size());
            final CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
            
            for (final LiteralArgumentBuilder<CommandSource> command : this.commands) {
                
                dispatcher.register(command);
            }
        }
    }
    
    public List<LiteralArgumentBuilder<CommandSource>> getCommands () {
        
        return ImmutableList.copyOf(this.commands);
    }
    
    /**
     * ENTITIES
     */
    private final List<EntityType<?>> entityTypes = NonNullList.create();
    private final List<Item> spawnEggs = NonNullList.create();
    
    public <T extends Entity> EntityType<T> registerMob (Class<T> entityClass, EntityType.IFactory<T> factory, EntityClassification classification, String id, float width, float height, int primary, int secondary) {
        
        return this.registerMob(entityClass, factory, classification, id, width, height, 64, 1, primary, secondary);
    }
    
    public <T extends Entity> EntityType<T> registerMob (Class<T> entityClass, EntityType.IFactory<T> factory, EntityClassification classification, String id, float width, float height, int trackingRange, int updateInterval, int primary, int secondary) {
        
        final EntityType<T> type = this.registerEntityType(entityClass, factory, classification, id, width, height, trackingRange, updateInterval);
        final Item spawnEgg = new SpawnEggItem(type, primary, secondary, new Item.Properties().group(ItemGroup.MISC));
        spawnEgg.setRegistryName(this.modid, id + "_spawn_egg");
        this.items.add(spawnEgg);
        this.spawnEggs.add(spawnEgg);
        return type;
    }
    
    public <T extends Entity> EntityType<T> registerEntityType (Class<T> entityClass, EntityType.IFactory<T> factory, EntityClassification classification, String id, float width, float height) {
        
        return this.registerEntityType(entityClass, factory, classification, id, width, height, 64, 1);
    }
    
    public <T extends Entity> EntityType<T> registerEntityType (Class<T> entityClass, EntityType.IFactory<T> factory, EntityClassification classification, String id, float width, float height, int trackingRange, int updateInterval) {
        
        final EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
        builder.size(width, height);
        builder.setTrackingRange(trackingRange);
        builder.setUpdateInterval(updateInterval);
        
        return this.registerEntityType(builder.build(this.modid + ":" + id), id);
    }
    
    public <T extends Entity> EntityType<T> registerEntityType (EntityType<T> type, String id) {
        
        this.entityTypes.add(type);
        type.setRegistryName(this.modid, id);
        return type;
    }
    
    public List<EntityType<?>> getEntityTypes () {
        
        return ImmutableList.copyOf(this.entityTypes);
    }
    
    public List<Item> getSpawnEggs () {
        
        return ImmutableList.copyOf(this.spawnEggs);
    }
    
    protected void registerEntityTypes (Register<EntityType<?>> event) {
        
        if (!this.entityTypes.isEmpty()) {
            
            this.logger.info("Registering {} entity types.", this.entityTypes.size());
            
            final IForgeRegistry<EntityType<?>> registry = event.getRegistry();
            
            for (final EntityType<?> entityType : this.entityTypes) {
                
                registry.register(entityType);
            }
        }
    }
    
    /**
     * CHUNK GENERATOR TYPES
     */
    private final List<ChunkGeneratorType<?, ?>> chunkGeneratorTypes = NonNullList.create();
    
    public <C extends GenerationSettings, T extends ChunkGenerator<C>> ChunkGeneratorType<C, T> registerChunkGeneratorType (ChunkGeneratorType<C, T> type, String id) {
        
        this.chunkGeneratorTypes.add(type);
        type.setRegistryName(this.modid, id);
        return type;
    }
    
    private void registerChunkGeneratorTypes (Register<ChunkGeneratorType<?, ?>> event) {
        
        if (!this.chunkGeneratorTypes.isEmpty()) {
            
            this.logger.info("Registering {} chunk generator types.", this.chunkGeneratorTypes.size());
            
            final IForgeRegistry<ChunkGeneratorType<?, ?>> registry = event.getRegistry();
            
            for (final ChunkGeneratorType<?, ?> containerType : this.chunkGeneratorTypes) {
                
                registry.register(containerType);
            }
        }
    }
    
    /**
     * BIOME PROVIDER TYPES
     */
    private final List<BiomeProviderType<?, ?>> biomeProviderTypes = NonNullList.create();
    
    public <C extends IBiomeProviderSettings, T extends BiomeProvider> BiomeProviderType<C, T> registerBiomeProvider (BiomeProviderType<C, T> type, String id) {
        
        this.biomeProviderTypes.add(type);
        type.setRegistryName(this.modid, id);
        return type;
    }
    
    private void registerBiomeProviders (Register<BiomeProviderType<?, ?>> event) {
        
        if (!this.biomeProviderTypes.isEmpty()) {
            
            this.logger.info("Registering {} biome provider types.", this.biomeProviderTypes.size());
            
            final IForgeRegistry<BiomeProviderType<?, ?>> registry = event.getRegistry();
            
            for (final BiomeProviderType<?, ?> containerType : this.biomeProviderTypes) {
                
                registry.register(containerType);
            }
        }
    }
    
    /**
     * STATS
     */
    private final List<ResourceLocation> stats = NonNullList.create();
    
    public ResourceLocation registerStat (String key) {
        
        return this.registerStat(key, IStatFormatter.DEFAULT);
    }
    
    public ResourceLocation registerStat (String key, IStatFormatter formatter) {
        
        final ResourceLocation statIdentifier = new ResourceLocation(this.modid, key);
        Registry.register(Registry.CUSTOM_STAT, key, statIdentifier);
        Stats.CUSTOM.get(statIdentifier, formatter);
        this.stats.add(statIdentifier);
        return statIdentifier;
    }
    
    public List<ResourceLocation> getStatIdentifiers () {
        
        return ImmutableList.copyOf(this.stats);
    }
    
    /**
     * POTIONS
     */
    private final List<Potion> potions = NonNullList.create();
    
    public Potion registerPotion (Potion potion, String id) {
        
        this.potions.add(potion);
        potion.setRegistryName(this.modid, id);
        return potion;
    }
    
    private void registerPotionTypes (Register<Potion> event) {
        
        final IForgeRegistry<Potion> registry = event.getRegistry();
        this.logger.info("Registering {} potion types.", this.potions.size());
        
        for (final Potion potion : this.potions) {
            
            registry.register(potion);
        }
    }
    
    public List<Potion> getPotions () {
        
        return ImmutableList.copyOf(this.potions);
    }
    
    /**
     * VILLAGER TRADES
     */
    private final Map<VillagerProfession, Int2ObjectMap<List<ITrade>>> trades = new HashMap<>();
    
    public ITrade registerVillagerTrade (VillagerProfession profession, int level, ITrade trade) {
        
        // Get or create a new int map
        final Int2ObjectMap<List<ITrade>> tradesByLevel = this.trades.getOrDefault(profession, new Int2ObjectOpenHashMap<>());
        
        // Get or create a new list of trades for the level.
        final List<ITrade> tradesForLevel = tradesByLevel.getOrDefault(level, new ArrayList<>());
        
        // Add the trades.
        tradesForLevel.add(trade);
        
        // Add the various maps and lists to their parent collection.
        tradesByLevel.put(level, tradesForLevel);
        this.trades.put(profession, tradesByLevel);
        return trade;
    }
    
    private void registerVillagerTrades (VillagerTradesEvent event) {
        
        // Ensure type isn't null, because mods.
        if (event.getType() != null) {
            
            // Get all trades for the current profession
            final Int2ObjectMap<List<ITrade>> tradesByLevel = this.trades.get(event.getType());
            
            // Check to make sure a trade has been registered.
            if (tradesByLevel != null && !tradesByLevel.isEmpty()) {
                
                // Iterate for the various profession levels
                for (final int level : tradesByLevel.keySet()) {
                    
                    final List<ITrade> tradeRegistry = event.getTrades().get(level);
                    
                    // If the trade pool exists add all trades for that tier.
                    if (tradeRegistry != null) {
                        
                        tradeRegistry.addAll(tradesByLevel.get(level));
                    }
                    
                    else {
                        
                        // Level 1 through 5 should always exist, but this is modded so people
                        // will inevitably mess this up.
                        this.logger.error("The mod {} tried to register a trade at profession level {} for villager type {}. This profession level does not exist!", this.modid, level, event.getType().getRegistryName().toString());
                    }
                }
            }
        }
    }
    
    /**
     * WANDERER TRADES
     */
    private final List<ITrade> basicTrades = new ArrayList<>();
    private final List<ITrade> rareTrades = new ArrayList<>();
    
    public ITrade addBasicWanderingTrade (ITrade trade) {
        
        this.basicTrades.add(trade);
        return trade;
    }
    
    public ITrade addRareWanderingTrade (ITrade trade) {
        
        this.rareTrades.add(trade);
        return trade;
    }
    
    private void registerWanderingTrades (WandererTradesEvent event) {
        
        event.getGenericTrades().addAll(this.basicTrades);
        event.getRareTrades().addAll(this.rareTrades);
    }
    
    /**
     * COMMAND ARGUMENT TYPES
     */
    private final Map<String, Tuple<Class, IArgumentSerializer>> commandArguments = new HashMap<>();
    
    public <T extends ArgumentType<?>> void registerCommandArgument (String name, Class<T> clazz, IArgumentSerializer<T> serializer) {
        
        this.commandArguments.put(name, new Tuple<>(clazz, serializer));
    }
    
    private void registerCommandArguments (FMLCommonSetupEvent event) {
        
        this.logger.info("Registering {} command argument types.", this.commandArguments.size());
        
        for (final Entry<String, Tuple<Class, IArgumentSerializer>> entry : this.commandArguments.entrySet()) {
            
            ArgumentTypes.register(entry.getKey(), entry.getValue().getA(), entry.getValue().getB());
        }
    }
    
    /**
     * LOOT CONDITION
     */
    private final List<AbstractSerializer<?>> lootConditions = NonNullList.create();
    
    public void registerLootCondition (AbstractSerializer<?> condition) {
        
        this.lootConditions.add(condition);
    }
    
    private void registerLootConditions (FMLCommonSetupEvent event) {
        
        this.logger.info("Registering {} loot condition types.", this.lootConditions.size());
        
        for (final AbstractSerializer<?> entry : this.lootConditions) {
            
            LootConditionManager.registerCondition(entry);
        }
    }
    
    /**
     * LOOT TABLE INJECTION
     */
    private final Map<ResourceLocation, ResourceLocation> injectionTables = new HashMap<>();
    
    public ResourceLocation injectTable (ResourceLocation toInject) {
        
        final ResourceLocation injectId = new ResourceLocation(this.modid, "inject/" + toInject.getNamespace() + "/" + toInject.getPath());
        this.injectionTables.put(toInject, injectId);
        return injectId;
    }
    
    private void loadTableInjections (LootTableLoadEvent event) {
        
        final ResourceLocation injectTableName = this.injectionTables.get(event.getName());
        
        // Checks if the table being loaded has a known injection table
        if (injectTableName != null) {
            
            final LootTable originalTable = event.getTable();
            final MinecraftServer server = Bookshelf.SIDED.getCurrentServer();
            
            if (server != null) {
                
                try {
                    
                    // Force load the injection table as it likely isn't loaded yet.
                    final LootTable inject = MCJsonUtils.loadLootTable(event.getLootTableManager(), server.getResourceManager(), injectTableName);
                    
                    if (inject != null) {
                        
                        this.logger.info("Injecting loot table {} with {}.", event.getName(), injectTableName);
                        this.mergeTables(originalTable, event.getName(), inject, injectTableName);
                    }
                }
                
                catch (final IOException e) {
                    
                    this.logger.error("Failed to load {} as a loot table.", injectTableName, e);
                }
            }
        }
    }
    
    /**
     * Merges all the loot pools of one table into another table. Pools with the same name will
     * have their entries and conditions merged. Pools not present in the original will be
     * injected in their entirety.
     * 
     * @param original The original loot table.
     * @param originalName The name of the original loot table.
     * @param mergeWith The table to merge into the original table.
     * @param mergeWithName The name of the table to merge.
     */
    private void mergeTables (LootTable original, ResourceLocation originalName, LootTable mergeWith, ResourceLocation mergeWithName) {
        
        final List<LootPool> pools = LootUtils.getPools(original);
        final Map<String, LootPool> mappedPools = LootUtils.mapPools(pools);
        
        for (final LootPool poolToInject : LootUtils.getPools(mergeWith)) {
            
            final String poolName = poolToInject.getName();
            
            if (mappedPools.containsKey(poolToInject.getName())) {
                
                final LootPool originalPool = mappedPools.get(poolName);
                
                this.logger.info("Merged pool {} into {} from {}.", originalPool.getName(), originalName, mergeWithName);
                LootUtils.getEntries(originalPool).addAll(LootUtils.getEntries(poolToInject));
                LootUtils.getConditions(originalPool).addAll(LootUtils.getConditions(poolToInject));
            }
            
            else {
                
                pools.add(poolToInject);
                this.logger.info("Injected new pool {} into table {}.", poolName, originalName);
            }
        }
    }
    
    /**
     * LOOT MODIFIERS
     */
    private final List<GlobalLootModifierSerializer<?>> globalModifierSerializers = NonNullList.create();
    
    public <T extends IGlobalLootModifier> GlobalLootModifierSerializer<T> registerGlobalModifier (Function<ILootCondition[], T> factory, String id) {
        
        return this.registerGlobalModifier(new SerializerFactory<>(factory), id);
    }
    
    public <T extends IGlobalLootModifier> GlobalLootModifierSerializer<T> registerGlobalModifier (GlobalLootModifierSerializer<T> serializer, String id) {
        
        serializer.setRegistryName(this.modid, id);
        this.globalModifierSerializers.add(serializer);
        return serializer;
    }
    
    private void registerGlobalLootModifierSerializers (Register<GlobalLootModifierSerializer<?>> event) {
        
        this.logger.info("Registering {} global loot modifier serializers.", this.globalModifierSerializers.size());
        
        final IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();
        
        for (final GlobalLootModifierSerializer<?> entry : this.globalModifierSerializers) {
            
            registry.register(entry);
        }
    }
    
    /**
     * ENCHANTMENTS
     */
    private final List<Enchantment> enchantments = NonNullList.create();
    
    public <T extends Enchantment> T registerEnchantment (T enchantment, String id) {
        
        enchantment.setRegistryName(this.modid, id);
        this.enchantments.add(enchantment);
        return enchantment;
    }
    
    public List<Enchantment> getEnchantments () {
        
        return ImmutableList.copyOf(this.enchantments);
    }
    
    private void registerEnchantments (Register<Enchantment> event) {
        
        this.logger.info("Registering {} enchantments.", this.enchantments.size());
        
        final IForgeRegistry<Enchantment> registry = event.getRegistry();
        
        for (final Enchantment entry : this.enchantments) {
            
            registry.register(entry);
        }
    }
    
    /**
     * PAINTINGS
     */
    private final List<PaintingType> paintings = NonNullList.create();
    
    public PaintingType registerPainting (String id, int width, int height) {
        
        return this.registerPainting(new PaintingType(width, height), id);
    }
    
    public <T extends PaintingType> T registerPainting (T painting, String id) {
        
        painting.setRegistryName(this.modid, id);
        this.paintings.add(painting);
        return painting;
    }
    
    public List<PaintingType> getPaintings () {
        
        return ImmutableList.copyOf(this.paintings);
    }
    
    private void registerPaintings (Register<PaintingType> event) {
        
        this.logger.info("Registering {} paintings.", this.paintings.size());
        
        final IForgeRegistry<PaintingType> registry = event.getRegistry();
        
        for (final PaintingType entry : this.paintings) {
            
            registry.register(entry);
        }
    }
}