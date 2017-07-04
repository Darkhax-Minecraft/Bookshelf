/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.command;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * This implementation of CommandTreeBase fixes a few permission issues, and adds a better help
 * screen.
 */
public abstract class CommandTree extends CommandTreeBase {

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 0) {

            // TODO build a better help screen
            sender.sendMessage(new TextComponentString(this.getSubCommandDescriptions(sender)));
        }

        else {

            super.execute(server, sender, args);
        }
    }

    /**
     * Creates a string with all the usage lines for the sub commands.
     *
     * @param sender The thing sending the command.
     * @return A string with all the usage lines for sub commands.
     */
    private String getSubCommandDescriptions (ICommandSender sender) {

        final StringBuilder builder = new StringBuilder(I18n.format(this.getUsage(sender)));

        for (final ICommand command : this.getSubCommands()) {

            builder.append("\n" + ChatFormatting.GREEN + I18n.format(command.getUsage(sender)));
        }

        return builder.toString();
    }

    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {

        return this.getRequiredPermissionLevel() <= 0 || super.checkPermission(server, sender);
    }
}
