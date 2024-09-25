package net.darkhax.bookshelf.common.api.data.conditions;

/**
 * Load conditions allow JSON entries in data/resource packs to define optional conditions in order for them to load.
 * For example a recipe file can prevent loading if a required item is not registered.
 */
public interface ILoadCondition {

    /**
     * Tests if the condition has been met or not.
     *
     * @return Has the condition been met?
     */
    boolean allowLoading();

    /**
     * Gets the type of the condition. This is required for serializing conditions.
     *
     * @return The type of the condition.
     */
    ConditionType getType();
}