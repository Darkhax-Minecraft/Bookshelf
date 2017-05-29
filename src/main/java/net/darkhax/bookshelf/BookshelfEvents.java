/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.crafting.IAnvilRecipe;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.registry.RegistryListener;
import net.darkhax.bookshelf.registry.RegistrySubscribe;
import net.darkhax.bookshelf.util.AnnotationUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookshelfEvents {

    @SubscribeEvent
    public void registerBlocks (RegistryEvent.Register<Block> event) {

        System.out.println("Starting!");
        // Initial registry call, to allow all the listeners to fire first.
        for (final RegistryListener listener : AnnotationUtils.getAnnotations(RegistrySubscribe.class, RegistryListener.class).keySet()) {

            listener.onRegistry();
            System.out.println("HIYA");
        }

        // Goes through all the blocks registered using the helper, and adds them to the event.
        for (final RegistryHelper helper : RegistryHelper.HELPERS) {

            for (final Block block : helper.getBlocks()) {

                event.getRegistry().register(block);
            }
        }
    }

    @SubscribeEvent
    public void registerItems (RegistryEvent.Register<Item> event) {

        // Goes through all the items registered using the helper, and adds them to the event.
        for (final RegistryHelper helper : RegistryHelper.HELPERS) {

            for (final Item item : helper.getItems()) {

                event.getRegistry().register(item);
            }
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate (AnvilUpdateEvent event) {

        for (final IAnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes()) {
            if (recipe.isValidRecipe(event.getLeft(), event.getRight(), event.getName())) {
                event.setCost(recipe.getExperienceCost(event.getLeft(), event.getRight(), event.getName()));
                event.setMaterialCost(recipe.getMaterialCost(event.getLeft(), event.getRight(), event.getName()));
                event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                return;
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick (TickEvent.ClientTickEvent event) {

        if (RenderUtils.requireRenderReload()) {

            Minecraft.getMinecraft().renderGlobal.loadRenderers();
            RenderUtils.markRenderersForReload(false);
        }
    }
}
