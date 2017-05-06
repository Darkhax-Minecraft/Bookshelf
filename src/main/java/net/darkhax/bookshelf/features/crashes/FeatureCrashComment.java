/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.features.crashes;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.darkhax.bookshelf.events.CrashReportEvent;
import net.darkhax.bookshelf.features.BookshelfFeature;
import net.darkhax.bookshelf.features.Feature;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(name = "bookshelf")
@BookshelfFeature(name = "crashcomments", description = "Adds the ability for crash comments to be changed")
public class FeatureCrashComment extends Feature {

    @Configurable(category = "crashcomments", description = "When true, the vanilla crash comments will show up as well")
    public static boolean addVanilla = true;

    @Configurable(category = "crashcomments", description = "You can add new crash comments by entering new lines here!")
    public static String[] messages = {};

    private final String[] vanillas = { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };

    @Override
    public void onPreInit () {

        BookshelfRegistry.addCrashComment("#BlameJared");
        BookshelfRegistry.addCrashComment("God dammit Vazkii!");
        BookshelfRegistry.addCrashComment("Consider yourself balanced");
        BookshelfRegistry.addCrashComment("The average garden snail has over 14, 000 teeth!");

        for (final String message : messages) {

            BookshelfRegistry.addCrashComment(message);
        }

        if (addVanilla) {

            for (final String message : this.vanillas) {

                BookshelfRegistry.addCrashComment(message);
            }
        }
    }

    @Override
    public boolean usesEvents () {

        return true;
    }

    @SubscribeEvent
    public void onCrashComment (CrashReportEvent event) {

        event.setMessage(BookshelfRegistry.getCrashComment());
    }
}