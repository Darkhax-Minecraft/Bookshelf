/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.ModTrackingList;
import net.darkhax.bookshelf.lib.RegistryHelper;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER)
public class Bookshelf {

    @Instance(Constants.MOD_ID)
    public static Bookshelf instance;

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {

        CraftingManager.getInstance().recipes = new ModTrackingList(CraftingManager.getInstance().recipes);
    }

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new BookshelfEvents());
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void preInitClient (FMLPreInitializationEvent event) {

    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void init (FMLInitializationEvent event) {

        RegistryHelper.clientInit();
    }
}