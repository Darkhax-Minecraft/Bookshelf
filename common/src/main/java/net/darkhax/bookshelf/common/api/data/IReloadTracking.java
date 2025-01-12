package net.darkhax.bookshelf.common.api.data;

import net.minecraft.world.item.crafting.RecipeManager;

public interface IReloadTracking {

    int bookshelf$getRevision();

    void bookshelf$setRevision(int revision);

    default void bookshelf$bump() {
        this.bookshelf$setRevision(this.bookshelf$getRevision() + 1);
    }

    static boolean areSameRevision(RecipeManager a, RecipeManager b) {
        return a instanceof IReloadTracking aTrack && b instanceof IReloadTracking bTrack && aTrack.bookshelf$getRevision() == bTrack.bookshelf$getRevision();
    }
}