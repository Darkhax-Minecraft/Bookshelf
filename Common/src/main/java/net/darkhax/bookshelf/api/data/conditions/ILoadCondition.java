package net.darkhax.bookshelf.api.data.conditions;

/**
 * Load conditions allow JSON data/resource pack entries to be skipped when their condition is not met. For example
 * prevent loading a recipe of a certain mod is not loaded.
 */
public interface ILoadCondition {

    /**
     * Tests if the condition has been met or not.
     *
     * @return Has the condition been met?
     */
    boolean test();
}