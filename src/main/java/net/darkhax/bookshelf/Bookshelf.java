/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.internal.BookshelfClient;
import net.darkhax.bookshelf.internal.BookshelfServer;
import net.darkhax.bookshelf.internal.ISidedProxy;
import net.darkhax.bookshelf.internal.command.ArgumentTypeHandOutput;
import net.darkhax.bookshelf.internal.command.BookshelfCommands;
import net.darkhax.bookshelf.internal.network.PacketSetClipboard;
import net.darkhax.bookshelf.network.NetworkHelper;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bookshelf.MOD_ID)
public class Bookshelf {
    
    // System Constants
    public static final Random RANDOM = new Random();
    
    public static final String NEW_LINE = System.getProperty("line.separator");
    
    // Mod Constants
    public static final String MOD_ID = "bookshelf";
    
    public static final String MOD_NAME = "Bookshelf";
    
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    
    public static final NetworkHelper NETWORK = new NetworkHelper(new ResourceLocation(MOD_ID, "main"), "4.2.x");
    
    public static final ISidedProxy SIDED = DistExecutor.runForDist( () -> () -> new BookshelfClient(), () -> () -> new BookshelfServer());
    
    private final RegistryHelper registry = new RegistryHelper(MOD_ID, LOG, null);
    
    public Bookshelf() {
        
        new BookshelfCommands(this.registry);
        
        this.registry.registerCommandArgument("enum", ArgumentTypeHandOutput.class, new ArgumentTypeHandOutput.Serialzier());
        this.registry.registerCommandArgument("mod", ArgumentTypeMod.class, new ArgumentSerializer<>( () -> ArgumentTypeMod.INSTACE));
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
        
        NETWORK.registerEnqueuedMessage(PacketSetClipboard.class, PacketSetClipboard::encode, PacketSetClipboard::decode, PacketSetClipboard::handle);
    }
}