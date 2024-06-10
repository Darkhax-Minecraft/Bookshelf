package net.darkhax.bookshelf.api.util;

import net.minecraft.world.entity.player.Player;

public final class ExperienceHelper {

    /**
     * Attempts to charge the player an experience point cost. If the player can not afford the full amount they will
     * not be charged and false will be returned.
     *
     * @param player The player to charge.
     * @param cost   The amount to charge the player in experience points.
     * @return True if the amount was paid.
     */
    public static boolean chargeExperiencePoints(Player player, int cost) {

        final int playerExperience = getExperiencePoints(player);

        if (playerExperience >= cost) {

            player.giveExperiencePoints(-cost);

            // The underlying EXP system uses a float which is prone to rounding errors. This will sometimes leave
            // players with a small fraction of exp progress that is worth less than 1 exp point. These rounding errors
            // are so small that they do not introduce functionality issues however they can trigger a vanilla bug
            // where the EXP bar will still render a few pixels of progress even when the player has no exp points. To
            // prevent this issue here we simply reset all progress when the player spends all of their points.
            if (getExperiencePoints(player) <= 0) {

                player.experienceProgress = 0f;
            }

            return true;
        }

        return false;
    }

    /**
     * Calculates the amount of experience points the player currently has. This should be used in favour of
     * {@link Player#totalExperience} which deceptively does not track the amount of experience the player currently
     * has.
     * <p>
     * Contrary to popular belief the {@link Player#totalExperience} value actually loosely represents how much
     * experience points the player has earned during their current life. This value is akin to the old player score
     * metric and appears to be predominantly legacy code. Relying on this value is often incorrect as negative changes
     * to the player level such as enchanting, the anvil, and the level command will not reduce this value.
     *
     * @param player The player to calculate the total experience points of.
     * @return The amount of experience points held by the player.
     */
    public static int getExperiencePoints(Player player) {

        // Start by calculating how many EXP points the player's current level is worth.
        int exp = getTotalPointsForLevel(player.experienceLevel);

        // Add the amount of experience points the player has earned towards their next level.
        exp += player.experienceProgress * getTotalPointsForLevel(player.experienceLevel + 1);

        return exp;
    }

    /**
     * Calculates the amount of additional experience points required to reach the given level when starting from the
     * previous level. This will also be the amount of experience points that an individual level is worth.
     *
     * @param level The level to calculate the point step for.
     * @return The amount of points required to reach the given level when starting from the previous level.
     */
    public static int getPointForLevel(int level) {

        if (level == 0) {

            return 0;
        }

        else if (level > 30) {

            return 112 + (level - 31) * 9;
        }

        else if (level > 15) {

            return 37 + (level - 16) * 5;
        }

        else {

            return 7 + (level - 1) * 2;
        }
    }

    /**
     * Calculates the amount of additional experience points required to reach the target level when starting from the
     * starting level.
     *
     * @param startingLevel The level to start the calculation at.
     * @param targetLevel   The level to reach.
     * @return The amount of additional experience points required to go from the starting level to the target level.
     */
    public static int getPointsForLevel(int startingLevel, int targetLevel) {

        if (targetLevel < startingLevel) {

            throw new IllegalArgumentException("Starting level must be lower than the target level!");
        }

        else if (startingLevel < 0) {

            throw new IllegalArgumentException("Level bounds must be positive!");
        }

        // If the levels are the same there is no point difference.
        else if (targetLevel == startingLevel) {

            return 0;
        }

        int requiredPoints = 0;

        for (int lvl = startingLevel + 1; lvl <= targetLevel; lvl++) {

            requiredPoints += getPointForLevel(lvl);
        }

        return requiredPoints;
    }

    /**
     * Calculates the total amount of experience points required to reach a given level when starting at level 0.
     *
     * @param level The target level to reach.
     * @return The amount of experience points required to reach the target level.
     */
    public static int getTotalPointsForLevel(int level) {

        return getPointsForLevel(0, level);
    }
}
