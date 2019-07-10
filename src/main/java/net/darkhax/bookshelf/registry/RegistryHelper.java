package net.darkhax.bookshelf.registry;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHelper {
    
    protected final String modid;
    protected final Logger logger;
    protected final ItemGroup group;
    
    public RegistryHelper(String modid, Logger logger, ItemGroup group) {
        
        this.modid = modid;
        this.logger = logger;
        this.group = group;
    }
    
    public void initialize (IEventBus modBus) {
        
        modBus.addGenericListener(Block.class, this::registerBlocks);
        modBus.addGenericListener(Item.class, this::registerItems);
        modBus.addGenericListener(TileEntityType.class, this::registerTileEntities);
        modBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeTypes);
        modBus.addGenericListener(ContainerType.class, this::registerContainerTypes);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
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
    
    public void registerItem (Item item, String id) {
        
        item.setRegistryName(new ResourceLocation(this.modid, id));
        this.items.add(item);
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
    private final NonNullList<LiteralArgumentBuilder<CommandSource>> commands = NonNullList.create();
    
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
}
