/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import net.darkhax.bookshelf.block.IColorfulBlock;
import net.darkhax.bookshelf.block.ITileEntityBlock;
import net.darkhax.bookshelf.item.IColorfulItem;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.LootBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is used to automatically register things from the registry helper. The hope is that by
 * registering the event while the owner is active, Forge will shut up about harmless registry
 * entries being dangerous.
 */
public class AutoRegistry implements IAutoRegistry {

    /**
     * The registry helper to register things from.
     */
    private final RegistryHelper helper;

    public AutoRegistry (RegistryHelper helper) {

        this.helper = helper;
    }

    @SubscribeEvent
    public void registerBlocks (RegistryEvent.Register<Block> event) {

        for (final Block block : this.helper.getBlocks()) {

            event.getRegistry().register(block);
        }

        for (final ITileEntityBlock provider : this.helper.getTileProviders()) {

            if (provider instanceof Block) {

                GameRegistry.registerTileEntity(provider.getTileEntityClass(), ((Block) provider).getRegistryName().toString().replace(":", ".").replace("_", "."));
            }
        }
    }

    @SubscribeEvent
    public void registerItems (RegistryEvent.Register<Item> event) {

        for (final Item item : this.helper.getItems()) {

            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public void registerEntitys (RegistryEvent.Register<EntityEntry> event) {

        for (final EntityEntryBuilder<? extends Entity> entry : this.helper.getEntities()) {

            event.getRegistry().register(entry.build());
        }
    }

    @SubscribeEvent
    public void registerSounds (RegistryEvent.Register<SoundEvent> event) {

        for (final SoundEvent sound : this.helper.getSounds()) {

            event.getRegistry().register(sound);
        }
    }

    @SubscribeEvent
    public void registerRecipes (RegistryEvent.Register<IRecipe> event) {

        for (final IRecipe recipe : this.helper.getRecipes()) {

            event.getRegistry().register(recipe);
        }
    }

    @SubscribeEvent
    public void registerEnchants (RegistryEvent.Register<Enchantment> event) {
        
        for (final Enchantment enchant : this.helper.getEnchantments()) {
            
            event.getRegistry().register(enchant);
        }
    }
    
    @SubscribeEvent
    public void onTableLoaded (LootTableLoadEvent event) {

        for (final LootBuilder builder : this.helper.getLootTableEntries().get(event.getName())) {

            final LootPool pool = event.getTable().getPool(builder.getPool());

            if (pool != null) {

                pool.addEntry(builder.build());
            }

            else {

                Constants.LOG.info("The mod {} tried to add loot to {} but the pool was not found. {}", this.helper.getModid(), event.getName(), builder.toString());
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void modelRegistryEvent (ModelRegistryEvent event) {

        for (final Item item : this.helper.getItems()) {

            this.helper.registerInventoryModel(item);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerBlockColor (ColorHandlerEvent.Block event) {

        for (final Block block : this.helper.getColoredBlocks()) {

            event.getBlockColors().registerBlockColorHandler(((IColorfulBlock) block).getColorHandler(), block);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerItemColor (ColorHandlerEvent.Item event) {

        for (final Block block : this.helper.getColoredBlocks()) {

            final IColorfulBlock colorfulBlock = (IColorfulBlock) block;

            if (colorfulBlock.getItemColorHandler() != null) {

                event.getItemColors().registerItemColorHandler(colorfulBlock.getItemColorHandler(), Item.getItemFromBlock(block));
            }
        }

        for (final Item item : this.helper.getColoredItems()) {

            event.getItemColors().registerItemColorHandler(((IColorfulItem) item).getColorHandler(), item);
        }
    }

    @Override
    public RegistryHelper getHelper () {

        return this.helper;
    }

    @Override
    public void init () {

    }

    @Override
    public void postInit () {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit () {

        for (final ITileEntityBlock provider : this.helper.getTileProviders()) {

            final TileEntitySpecialRenderer tesr = provider.getTileRenderer();

            if (tesr != null) {

                ClientRegistry.bindTileEntitySpecialRenderer(provider.getTileEntityClass(), tesr);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientPostInit () {

    }
}
