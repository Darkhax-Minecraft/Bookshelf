package net.darkhax.bookshelf.impl.gametest;

import net.darkhax.bookshelf.Constants;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BookshelfForgeGameTests {

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {

        event.register(BookshelfGameTests.class);
    }
}
