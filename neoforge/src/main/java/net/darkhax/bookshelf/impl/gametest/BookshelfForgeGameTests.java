package net.darkhax.bookshelf.impl.gametest;

import net.darkhax.bookshelf.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BookshelfForgeGameTests {

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {

        event.register(BookshelfGameTests.class);
    }
}
