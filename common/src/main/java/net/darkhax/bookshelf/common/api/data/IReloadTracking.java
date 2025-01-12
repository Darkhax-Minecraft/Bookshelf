package net.darkhax.bookshelf.common.api.data;

public interface IReloadTracking {

    int bookshelf$getRevision();

    void bookshelf$setRevision(int revision);

    default void bookshelf$bump() {
        this.bookshelf$setRevision(this.bookshelf$getRevision() + 1);
    }

    static boolean areSameRevision(Object a, Object b) {
        return a instanceof IReloadTracking aTrack && b instanceof IReloadTracking bTrack && aTrack.bookshelf$getRevision() == bTrack.bookshelf$getRevision();
    }
}