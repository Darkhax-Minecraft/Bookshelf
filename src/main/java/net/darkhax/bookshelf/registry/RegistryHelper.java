package net.darkhax.bookshelf.registry;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.darkhax.bookshelf.world.DimensionFactory;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHelper {
    
    protected final String modid;
    protected final Logger logger;
    
    @Nullable
    protected final ItemGroup group;
    
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
        
        if (!this.dimensions.isEmpty()) {
            
            modBus.addGenericListener(ModDimension.class, this::registerDimensions);
            MinecraftForge.EVENT_BUS.addListener(this::registerDimensionTypes);
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
        
        final IRecipeType<T> type = new IRecipeType<T>() {
            
            @Override
            public String toString () {
                
                return RegistryHelper.this.modid + ":" + typeId;
            }
        };
        
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
     * DIMENSIONS
     */
    private final List<DimensionFactory> dimensions = NonNullList.create();
    
    public DimensionFactory registerDimension (DimensionFactory dimension, String id) {
        
        dimension.setRegistryName(this.modid, id);
        this.dimensions.add(dimension);
        return dimension;
    }
    
    private void registerDimensions (Register<ModDimension> event) {
        
        if (!this.dimensions.isEmpty()) {
            
            this.logger.info("Registering {} dimensions.", this.dimensions.size());
            
            final IForgeRegistry<ModDimension> registry = event.getRegistry();
            
            for (final DimensionFactory dimension : this.dimensions) {
                
                // registry.register(dimension);
            }
        }
    }
    
    private void registerDimensionTypes (RegisterDimensionsEvent event) {
        
        if (!this.dimensions.isEmpty()) {
            
            this.logger.info("Registering {} dimensions types.", this.dimensions.size());
            
            for (final DimensionFactory dimension : this.dimensions) {
                
                DimensionManager.registerDimension(dimension.getRegistryName(), dimension, dimension.getDefaultData(), dimension.hasSkylight());
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
}