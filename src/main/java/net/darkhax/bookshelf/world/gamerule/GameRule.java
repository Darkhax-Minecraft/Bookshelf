package net.darkhax.bookshelf.world.gamerule;

import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.World;

public class GameRule {

    /**
     * The id of the game rule.
     */
    private final String id;

    /**
     * The default state of the game rule.
     */
    private final boolean defaultState;

    /**
     * Constructs a new GameRule manager.
     *
     * @param id The id of the game rule.
     * @param defaultState The defualt state for the game rule.
     */
    public GameRule (String id, boolean defaultState) {

        this.id = id;
        this.defaultState = defaultState;
    }

    /**
     * Gets the id of the rule.
     *
     * @return The id of the rule.
     */
    public String getId () {

        return this.id;
    }

    /**
     * Gets the default state for the rule.
     *
     * @return The default state.
     */
    public boolean getDefaultState () {

        return this.defaultState;
    }

    /**
     * Initializes the GameRule for a world.
     *
     * @param world The world to initialize for.
     */
    public void initialize (World world) {

        final GameRules rules = world.getGameRules();

        if (!rules.hasRule(this.getId())) {

            rules.addGameRule(this.getId(), String.valueOf(this.getDefaultState()), ValueType.BOOLEAN_VALUE);
        }
    }

    /**
     * Gets the state of the rule for a world.
     *
     * @param world The world to check for.
     * @return The rule state for the passed world.
     */
    public boolean getRuleState (World world) {

        return world.getGameRules().getBoolean(this.getId());
    }

    /**
     * Sets the state of the rule for a world.
     *
     * @param world The world to apply the state to.
     * @param state The state to set.
     */
    public void setRuleState (World world, boolean state) {

        world.getGameRules().setOrCreateGameRule(this.getId(), String.valueOf(state));
    }
}
