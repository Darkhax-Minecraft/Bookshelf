/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.handler.BookshelfEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.AnnotationUtils;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER)
public class Bookshelf {

    @Instance(Constants.MOD_ID)
    public static Bookshelf instance;

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {

        AnnotationUtils.asmData = event.getASMHarvestedData();
        MinecraftForge.EVENT_BUS.register(new BookshelfEventHandler());
    }

    @EventHandler
    public void Init (FMLInitializationEvent event) {

        OreDictUtils.initAdditionalVanillaEntries();
    }
}