package net.darkhax.bookshelf.common.impl.data.loot.modifiers;

import org.jetbrains.annotations.Nullable;

/**
 * Internal hooks related to loot pools.
 */
public interface ILootPoolHooks {

    void bookshelf$setHash(int fingerprint);

    @Nullable
    Integer bookshelf$getHash();

    default boolean bookshelf$matches(int toMatch) {
        final Integer hash = this.bookshelf$getHash();
        return hash != null && hash == toMatch;
    }
}