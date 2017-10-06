/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.crafting.IAnvilRecipe;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.AnnotationUtils;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.bookshelf.world.gamerule.GameRule;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class Bookshelf {

    @Instance(Constants.MOD_ID)
    public static Bookshelf instance;

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {

        AnnotationUtils.asmData = event.getASMHarvestedData();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void Init (FMLInitializationEvent event) {

        OreDictUtils.initAdditionalVanillaEntries();
        
        for (RegistryHelper helper : RegistryHelper.getAllHelpers()) {
            
            helper.getAutoRegistry().init();
        }
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit (FMLInitializationEvent event) {

        for (RegistryHelper helper : RegistryHelper.getAllHelpers()) {
            
            helper.getAutoRegistry().clientInit();
        }
    }

    @EventHandler
    public void serverStarting (FMLServerStartingEvent event) {

        for (final ICommand command : BookshelfRegistry.getCommands()) {

            event.registerServerCommand(command);
        }
    }

    // Handles game rule registry
    @SubscribeEvent
    public void onWorldLoaded (WorldEvent.Load event) {

        if (!event.getWorld().isRemote) {

            for (final GameRule rule : BookshelfRegistry.getGameRules()) {

                rule.initialize(event.getWorld());
            }
        }
    }

    // Handles anvil stuff, for my anvil recipes.
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

    // Handles the render reload request in RenderUtils
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick (TickEvent.ClientTickEvent event) {

        if (RenderUtils.requireRenderReload()) {

            Minecraft.getMinecraft().renderGlobal.loadRenderers();
            RenderUtils.markRenderersForReload(false);
        }
    }
    
    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        Constants.LOG.error("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}