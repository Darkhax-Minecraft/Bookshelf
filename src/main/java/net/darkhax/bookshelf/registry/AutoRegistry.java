/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is used to automatically register things from the registry helper. The hope is that by
 * registering the event while the owner is active, Forge will shut up about harmless registry
 * entries being dangerous.
 */
public class AutoRegistry {

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

        int entId = 0;
        
        for (final EntityEntry entry : this.helper.getEntities()) {
            
            //TODO add some stuff for velocity updates and so on
            if (entry.getEgg() != null) {
                
                EntityRegistry.registerModEntity(entry.getRegistryName(), entry.getEntityClass(), entry.getName(), entId, this.helper.getModInstance(), 64, 1, true, entry.getEgg().primaryColor, entry.getEgg().secondaryColor);
            }
            
            else {
                
                EntityRegistry.registerModEntity(entry.getRegistryName(), entry.getEntityClass(), entry.getName(), entId, this.helper.getModInstance(), 64, 1, true);
            }
            
            entId++;
            
            event.getRegistry().register(entry);
        }
    }

    @SubscribeEvent
    public void registerSounds (RegistryEvent.Register<SoundEvent> event) {

        for (final SoundEvent sound : this.helper.getSounds()) {

            event.getRegistry().register(sound);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void modelRegistryEvent (ModelRegistryEvent event) {

        for (final Item item : this.helper.getItems()) {

            this.helper.registerInventoryModel(item);
        }
    }
}
