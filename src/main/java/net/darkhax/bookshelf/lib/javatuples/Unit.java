/*
 * =============================================================================
 *
 *   Copyright (c) 2010, The JAVATUPLES team (http://www.javatuples.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package net.darkhax.bookshelf.lib.javatuples;

import net.darkhax.bookshelf.lib.javatuples.valueintf.IValue0;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * A tuple of one element.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez, edited by SanAndreasP
 * @since 1.0
 */
public final class Unit<A>
        extends Tuple
        implements IValue0<A>
{
    private static final long serialVersionUID = -9113114724069537096L;
    private static final int SIZE = 1;

    private final A val0;

    public static <A> Unit<A> with(final A value0) {
        return new Unit<A>(value0);
    }

    /**
     * <p>
     * Create tuple from array. Array has to have exactly one element.
     * </p>
     *
     * @param <X>   the array component type
     * @param array the array to be converted to a tuple
     * @return the tuple
     */
    public static <X> Unit<X> fromArray(final X[] array) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if( array.length != 1 ) {
            throw new IllegalArgumentException("Array must have exactly 1 element in order to create a Unit. Size is " + array.length);
        }

        return new Unit<X>(array[0]);
    }

    /**
     * <p>
     * Create tuple from collection.
     * Collection has to have exactly one element.
     * </p>
     *
     * @param <X>        the collection component type
     * @param collection the collection to be converted to a tuple
     * @return the tuple
     */
    public static <X> Unit<X> fromCollection(final Collection<X> collection) {
        if( collection == null ) {
            throw new IllegalArgumentException("Collection cannot be null");
        }

        if( collection.size() != 1 ) {
            throw new IllegalArgumentException("Collection must have exactly 1 element in order to create a Unit. Size is " + collection.size());
        }

        return new Unit<X>(collection.iterator().next());
    }

    /**
     * <p>
     * Create tuple from iterable.
     * Iterable has to have exactly one element.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Unit<X> fromIterable(final Iterable<X> iterable) {
        return fromIterable(iterable, 0, true);
    }

    /**
     * <p>
     * Create tuple from iterable, starting from the specified index.
     * Iterable can have more elements than the tuple to be created.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Unit<X> fromIterable(final Iterable<X> iterable, int index) {
        return fromIterable(iterable, index, false);
    }

    private static <X> Unit<X> fromIterable(final Iterable<X> iterable, int index, boolean checkSize) {
        if( iterable == null ) {
            throw new IllegalArgumentException("Iterable cannot be null");
        }

        X element;
        final Iterator<X> iter = iterable.iterator();

        for( int i = 0; i <= index; i++ ) {
            if( iter.hasNext() ) {
                element = iter.next();
                if( i == index ) {
                    if( checkSize && iter.hasNext() ) {
                        throw new IllegalArgumentException("Iterable must have exactly 1 element in order to create a Unit.");
                    }
                    return new Unit<X>(element);
                }
            } else {
                if( i < index ) {
                    throw new IllegalArgumentException(String.format("Iterable has not enough elements to grab a value from index %d", index));
                } else {
                    throw new IllegalArgumentException(String.format("Not enough elements for creating a Unit (1 needed, %d given)", i));
                }
            }
        }

        return null;
    }

    public Unit(final A value0) {
        super(value0);
        this.val0 = value0;
    }

    @Override
    public A getValue0() {
        return this.val0;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
