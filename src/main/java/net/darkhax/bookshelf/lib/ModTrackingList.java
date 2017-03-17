/*******************************************************************************************************************
 * Copyright: Mezz
 *
 * License:   GNU Lesser General Public License 2.1
 *            http://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Original:  https://github.com/MinecraftForge/MinecraftForge/pull/3739
 *
 * Changes:   - Backported the code to a 1.10.2 workspace (not much of a change)
 *            - Added more documentation in the form of JavaDocs.
 *            - Added a third trackModContainerMethod to reduce code duplication.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.lib;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingListIterator;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class ModTrackingList<T> extends ForwardingList<T> implements RandomAccess {

    /**
     * The parent/delegate list.
     */
    private final List<T> delegate;

    /**
     * A map containing the active mod container at the time the value was added to this list.
     */
    private final Map<T, ModContainer> modContainerMap;

    /**
     * The main constructor for the tracking list. Entries added and removed from this list
     * will be tracked.
     *
     * @param delegate The parent/delegate list. This can be empty, but should probably
     *        represent the list you are replacing, if you are replacing one.
     */
    public <D extends List<T> & RandomAccess> ModTrackingList (@Nonnull D delegate) {

        this(delegate, new IdentityHashMap<T, ModContainer>());
    }

    /**
     * The backing constructor for the tracking list. Entries added and removed from this list
     * will be tracked.
     *
     * @param delegate The parent/delegate list. This can be empty, but should probably
     *        represent the list you are replacing, if you are replacing one.
     * @param modContainerMap The map used to hold the tracking data.
     */
    private ModTrackingList (@Nonnull List<T> delegate, @Nonnull Map<T, ModContainer> modContainerMap) {

        this.delegate = delegate;
        this.modContainerMap = modContainerMap;
    }

    /**
     * Tracks the mod container that is active when the element was handled.
     *
     * @param element The element being handled.
     */
    private void trackModContainer (@Nonnull T element) {

        this.trackModContainer(element, Loader.instance().activeModContainer());
    }

    /**
     * Tracks the mod container that is active when multiple elements are handled.
     *
     * @param elements The collection of elements being handled.
     */
    private void trackModContainer (@Nonnull Collection<? extends T> elements) {

        final ModContainer modContainer = Loader.instance().activeModContainer();

        for (final T element : elements) {

            this.trackModContainer(element, modContainer);
        }
    }

    /**
     * Tracks the mod container that was active when the element was handled.
     *
     * @param element The element being handled.
     * @param container The mod container that was active when the element was handled.
     */
    private void trackModContainer (@Nonnull T element, ModContainer container) {

        if (container != null) {
            this.modContainerMap.put(element, container);
        }
    }

    public Map<T, ModContainer> getTrackedEntries () {

        return this.modContainerMap;
    }

    /**
     * Gets the mod container that was active when the passed element was handled by this list.
     * This can be null if the element has not been handled by this list.
     *
     * @param element The element to get the mod container of.
     * @return The ModContainer that was active while the passed element was being handled.
     */
    @Nullable
    public ModContainer getModContainer (@Nonnull T element) {

        return this.modContainerMap.get(element);
    }

    @Override
    protected List<T> delegate () {

        return this.delegate;
    }

    @Override
    public boolean add (@Nonnull T t) {

        final boolean changed = this.delegate.add(t);

        if (changed) {

            this.trackModContainer(t);
        }

        return changed;
    }

    @Override
    public void add (int index, @Nonnull T element) {

        this.delegate.add(index, element);
        this.trackModContainer(element);
    }

    @Override
    public boolean addAll (int index, @Nonnull Collection<? extends T> elements) {

        final boolean changed = this.delegate.addAll(index, elements);

        if (changed) {

            this.trackModContainer(elements);
        }

        return changed;
    }

    @Override
    public boolean addAll (@Nonnull Collection<? extends T> collection) {

        final boolean changed = this.delegate.addAll(collection);

        if (changed) {

            this.trackModContainer(collection);
        }

        return changed;
    }

    @Override
    public T remove (int index) {

        final T removed = this.delegate.remove(index);

        if (removed != null) {

            this.modContainerMap.remove(removed);
        }

        return removed;
    }

    @Override
    public boolean remove (@Nonnull Object object) {

        final boolean changed = this.delegate.remove(object);

        if (changed) {

            this.modContainerMap.remove(object);
        }

        return changed;
    }

    @Override
    public boolean removeAll (@Nonnull Collection<?> collection) {

        final boolean changed = this.delegate.removeAll(collection);

        if (changed) {

            this.modContainerMap.keySet().removeAll(collection);
        }

        return changed;
    }

    @Override
    public T set (int index, @Nonnull T element) {

        final T previous = this.delegate.set(index, element);

        if (previous != null) {

            this.modContainerMap.remove(previous);
        }

        this.trackModContainer(element);
        return previous;
    }

    @Nonnull
    @Override
    public List<T> subList (int fromIndex, int toIndex) {

        final List<T> delegateSubList = this.delegate.subList(fromIndex, toIndex);
        return new ModTrackingList<>(delegateSubList, this.modContainerMap);
    }

    @Override
    public boolean retainAll (@Nonnull Collection<?> collection) {

        final boolean changed = this.delegate.retainAll(collection);

        if (changed) {

            this.modContainerMap.keySet().retainAll(collection);
        }

        return changed;
    }

    @Nonnull
    @Override
    public Iterator<T> iterator () {

        return new ModTrackedIterator(this.delegate.iterator());
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator () {

        return new ModTrackedListIterator(this.delegate.listIterator());
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator (int index) {

        return new ModTrackedListIterator(this.delegate.listIterator(index));
    }

    @Override
    public void clear () {

        this.delegate.clear();
        this.modContainerMap.clear();
    }

    private class ModTrackedIterator extends ForwardingIterator<T> {

        private final Iterator<T> delegate;

        @Nullable
        private T lastValue;

        public ModTrackedIterator (Iterator<T> delegate) {

            this.delegate = delegate;
        }

        @Override
        protected Iterator<T> delegate () {

            return this.delegate;
        }

        @Override
        public T next () {

            this.lastValue = this.delegate.next();
            return this.lastValue;
        }

        @Override
        public void remove () {

            this.delegate.remove();

            if (this.lastValue != null) {

                ModTrackingList.this.modContainerMap.remove(this.lastValue);
            }
        }
    }

    private class ModTrackedListIterator extends ForwardingListIterator<T> {

        private final ListIterator<T> delegate;

        @Nullable
        private T lastValue;

        public ModTrackedListIterator (ListIterator<T> delegate) {

            this.delegate = delegate;
        }

        @Override
        protected ListIterator<T> delegate () {

            return this.delegate;
        }

        @Override
        public T next () {

            this.lastValue = this.delegate.next();
            return this.lastValue;
        }

        @Override
        public T previous () {

            this.lastValue = this.delegate.previous();
            return this.lastValue;
        }

        @Override
        public void remove () {

            this.delegate.remove();

            if (this.lastValue != null) {

                ModTrackingList.this.modContainerMap.remove(this.lastValue);
            }
        }

        @Override
        public void add (@Nonnull T element) {

            this.delegate.add(element);
            ModTrackingList.this.trackModContainer(element);
        }

        @Override
        public void set (@Nonnull T element) {

            this.delegate.set(element);

            if (this.lastValue != null) {

                ModTrackingList.this.modContainerMap.remove(this.lastValue);
            }

            ModTrackingList.this.trackModContainer(element);
        }
    }
}