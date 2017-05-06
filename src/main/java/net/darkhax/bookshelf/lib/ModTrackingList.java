/*******************************************************************************************************************
 * Copyright: Mezz
 *
 * License:   GNU Lesser General Public License 2.1
 *            http://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Original:  https://github.com/MinecraftForge/MinecraftForge/pull/3739
 *
 * Changes:   - Backported the code to a 1.10.2 workspace (not much of a change).
 *            - Added more documentation in the form of JavaDocs.
 *            - Added a third trackModContainerMethod to reduce code duplication.
 *            - Final local variables marked final.
 *            - Made all constructors public.
 *            - Added a bunch of additional delegate methods for more coverage.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.lib;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class ModTrackingList<T> extends AbstractList<T> implements RandomAccess {

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
    public ModTrackingList (@Nonnull List<T> delegate, @Nonnull Map<T, ModContainer> modContainerMap) {

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

        if (container != null && !container.getModId().equalsIgnoreCase("Forge")) {

            this.modContainerMap.put(element, container);
        }
    }

    /**
     * Gets the map of tracked entries.
     *
     * @return The map of tracked entries.
     */
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
    public boolean add (@Nonnull T t) {

        final boolean changed = this.delegate.add(t);
        if (changed) {
            this.trackModContainer(t);
        }
        return changed;
    }

    @Override
    public T get (int index) {

        return this.delegate.get(index);
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

        return this.delegate.remove(index);
    }

    @Override
    public boolean remove (@Nonnull Object object) {

        return this.delegate.remove(object);
    }

    @Override
    public boolean removeAll (@Nonnull Collection<?> collection) {

        return this.delegate.removeAll(collection);
    }

    @Override
    public T set (int index, @Nonnull T element) {

        final T previous = this.delegate.set(index, element);
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
    public int size () {

        return this.delegate.size();
    }

    @Override
    public void clear () {

        this.delegate.clear();
    }

    @Override
    public void forEach (Consumer<? super T> action) {

        this.delegate.forEach(action);
    }

    @Override
    public boolean isEmpty () {

        return this.delegate.isEmpty();
    }

    @Override
    public boolean contains (Object o) {

        return this.delegate.contains(o);
    }

    @Override
    public Iterator<T> iterator () {

        return this.delegate.iterator();
    }

    @Override
    public Object[] toArray () {

        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray (T[] a) {

        return this.delegate.toArray(a);
    }

    @Override
    public boolean containsAll (Collection<?> c) {

        return this.delegate.containsAll(c);
    }

    @Override
    public boolean retainAll (Collection<?> c) {

        return this.delegate.retainAll(c);
    }

    @Override
    public void replaceAll (UnaryOperator<T> operator) {

        this.delegate.replaceAll(operator);
    }

    @Override
    public boolean removeIf (Predicate<? super T> filter) {

        return this.delegate.removeIf(filter);
    }

    @Override
    public void sort (Comparator<? super T> c) {

        this.delegate.sort(c);
    }

    @Override
    public boolean equals (Object o) {

        return this.delegate.equals(o);
    }

    @Override
    public int hashCode () {

        return this.delegate.hashCode();
    }

    @Override
    public Stream<T> stream () {

        return this.delegate.stream();
    }

    @Override
    public Stream<T> parallelStream () {

        return this.delegate.parallelStream();
    }

    @Override
    public int indexOf (Object o) {

        return this.delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf (Object o) {

        return this.delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator () {

        return this.delegate.listIterator();
    }

    @Override
    public ListIterator<T> listIterator (int index) {

        return this.delegate.listIterator(index);
    }

    @Override
    public Spliterator<T> spliterator () {

        return this.delegate.spliterator();
    }
}