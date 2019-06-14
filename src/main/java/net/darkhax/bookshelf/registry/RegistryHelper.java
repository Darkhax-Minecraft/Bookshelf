/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHelper {

    private final String modid;
    private final Logger logger;
    private final ItemGroup group;

    public RegistryHelper (String modid, Logger logger, ItemGroup group) {

        this.modid = modid;
        this.logger = logger;
        this.group = group;
    }

    public void initialize (IEventBus modBus) {

        modBus.addGenericListener(Block.class, this::registerBlocks);
        modBus.addGenericListener(Item.class, this::registerItems);
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

        return this.registerBlock(block, new ItemBlock(block, new Item.Properties().group(this.group)), id);
    }

    public Block registerBlock (Block block, ItemBlock item, String id) {

        block.setRegistryName(new ResourceLocation(this.modid, id));
        this.blocks.add(block);
        this.registerItem(item, id);
        return block;
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

    /**
     * TILE ENTITIES
     */
    private final List<TileEntityType<?>> tileEntityTypes = NonNullList.create();

    public TileEntityType<?> registerTileEntity (Supplier<? extends TileEntity> factory, String id) {

        final ResourceLocation fullId = new ResourceLocation(this.modid, id);
        final TileEntityType<TileEntity> type = TileEntityType.register(fullId.toString(), TileEntityType.Builder.create(factory));
        this.tileEntityTypes.add(type);
        return type;
    }
}
