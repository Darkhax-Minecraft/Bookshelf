package net.darkhax.bookshelf.features.crashes;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.events.CrashReportEvent;
import net.darkhax.bookshelf.features.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeatureCrashComments extends Feature {

    private boolean enabled = true;

    private boolean addVanilla = true;

    private String[] messages = {};

    private final String[] vanillas = { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };

    @Override
    public void onPreInit () {

        if (this.enabled) {

            MinecraftForge.EVENT_BUS.register(this);

            BookshelfRegistry.addCrashComment("#BlameJared");
            BookshelfRegistry.addCrashComment("God dammit Vazkii!");
            BookshelfRegistry.addCrashComment("Consider yourself balanced");
            BookshelfRegistry.addCrashComment("The average garden snail has over 14, 000 teeth!");

            for (final String message : this.messages) {

                BookshelfRegistry.addCrashComment(message);
            }

            if (this.addVanilla) {

                for (final String message : this.vanillas) {

                    BookshelfRegistry.addCrashComment(message);
                }
            }
        }
    }

    @Override
    public void setupConfig (Configuration config) {

        this.enabled = config.getBoolean("Enabled", "Crash Comments", true, "While enabled, the witty comment on crash messages will pull entries from Bookshelf's system instead.");
        this.addVanilla = config.getBoolean("AddVanilla", "Crash Comments", true, "While true, vanilla crash comments will be added to the crash comment list");
        this.messages = config.getStringList("ConfigMessages", "Crash Comments", new String[] {}, "Entries in this list have a chance of showing up on crash messages, in place of Mojang's witty comments.");
    }

    @SubscribeEvent
    public void onCrashComment (CrashReportEvent event) {

        event.setMessage(BookshelfRegistry.getCrashComment());
    }
}