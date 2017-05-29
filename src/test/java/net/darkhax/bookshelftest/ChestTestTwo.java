/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelftest;

import net.darkhax.bookshelf.BookshelfEvents;
import net.darkhax.bookshelf.builder.ChestBuilder;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "chesttesttwo", name = "Chest Test Two", version = "0.0.0")
public class ChestTestTwo {

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new BookshelfEvents());

        final RegistryHelper registry = new RegistryHelper("chesttesttwo");
        registry.setTab(CreativeTabs.MISC);
        final ChestBuilder builder = new ChestBuilder("chesttesttwo", true);
        builder.addChestType("four");
        builder.register(registry);
    }
}