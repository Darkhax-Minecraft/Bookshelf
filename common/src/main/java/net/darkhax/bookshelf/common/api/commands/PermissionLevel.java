package net.darkhax.bookshelf.common.api.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.Predicate;

public enum PermissionLevel implements Predicate<CommandSourceStack> {

    /**
     * All players will generally meet the requirements for this permission level.
     */
    PLAYER(Commands.LEVEL_ALL),

    /**
     * These players have slightly elevated permission levels. In vanilla, they do not gain access to any additional
     * commands, but they are able to bypass spawn chunk protection.
     */
    MODERATOR(Commands.LEVEL_MODERATORS),

    /**
     * These players can execute commands that modify the world and player data. They are also allowed to use and modify
     * command blocks.
     */
    GAMEMASTER(Commands.LEVEL_GAMEMASTERS),

    /**
     * These players can use commands related to player management. For example, they can ban, kick, op, and de-op.
     */
    ADMIN(Commands.LEVEL_ADMINS),

    /**
     * This is the highest permission level available in vanilla Minecraft. Players with this permission level generally
     * have no restrictions.
     */
    OWNER(Commands.LEVEL_OWNERS);

    final int level;

    PermissionLevel(int level) {
        this.level = level;
    }

    public int get() {
        return this.level;
    }

    @Override
    public boolean test(CommandSourceStack source) {
        return source.hasPermission(this.level);
    }
}
