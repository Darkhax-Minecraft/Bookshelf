/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import net.darkhax.bookshelf.block.IColorfulBlock;
import net.darkhax.bookshelf.item.IColorfulItem;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.LootBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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

        for (final Block block : this.helper.getColoredBlocks()) {

            this.helper.registerColorHandler(block, ((IColorfulBlock) block).getColorHandler());
        }

        for (final Item item : this.helper.getColoredItems()) {

            this.helper.registerColorHandler(item, ((IColorfulItem) item).getColorHandler());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientPostInit () {

    }
}
