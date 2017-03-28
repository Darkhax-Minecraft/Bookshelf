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

import net.darkhax.bookshelf.lib.javatuples.valueintf.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * A tuple of five elements.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez, edited by SanAndreasP
 * @since 1.0
 */
public final class Quintet<A, B, C, D, E>
        extends Tuple
        implements IValue0<A>, IValue1<B>, IValue2<C>, IValue3<D>, IValue4<E>
{
    private static final long serialVersionUID = -1579008485383872628L;
    private static final int SIZE = 5;

    private final A val0;
    private final B val1;
    private final C val2;
    private final D val3;
    private final E val4;

    public static <A, B, C, D, E> Quintet<A, B, C, D, E> with(final A value0, final B value1, final C value2,
                                                              final D value3, final E value4) {
        return new Quintet<>(value0, value1, value2, value3, value4);
    }

    /**
     * <p>
     * Create tuple from array. Array has to have exactly five elements.
     * </p>
     *
     * @param <X>   the array component type
     * @param array the array to be converted to a tuple
     * @return the tuple
     */
    public static <X> Quintet<X, X, X, X, X> fromArray(final X[] array) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if( array.length != 5 ) {
            throw new IllegalArgumentException("Array must have exactly 5 elements in order to create a Quintet. Size is " + array.length);
        }

        return new Quintet<>(array[0], array[1], array[2], array[3], array[4]);
    }

    /**
     * <p>
     * Create tuple from collection. Collection has to have exactly five elements.
     * </p>
     *
     * @param <X>        the collection component type
     * @param collection the collection to be converted to a tuple
     * @return the tuple
     */
    public static <X> Quintet<X, X, X, X, X> fromCollection(final Collection<X> collection) {
        return fromIterable(collection);
    }

    /**
     * <p>
     * Create tuple from iterable. Iterable has to have exactly five elements.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Quintet<X, X, X, X, X> fromIterable(final Iterable<X> iterable) {
        return fromIterable(iterable, 0, true);
    }

    /**
     * <p>
     * Create tuple from iterable, starting from the specified index. Iterable
     * can have more (or less) elements than the tuple to be created.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Quintet<X, X, X, X, X> fromIterable(final Iterable<X> iterable, int index) {
        return fromIterable(iterable, index, false);
    }

    private static <X> Quintet<X, X, X, X, X> fromIterable(final Iterable<X> iterable, int index, final boolean checkSize) {
        if( iterable == null ) {
            throw new IllegalArgumentException("Iterable cannot be null");
        }

        X element;
        ArrayList<X> elements = new ArrayList<>(5);
        final Iterator<X> iter = iterable.iterator();
        int lastIndex = index + SIZE - 1;

        for( int i = 0; i <= lastIndex; i++ ) {
            if( iter.hasNext() ) {
                element = iter.next();
                if( i >= index ) {
                    if( checkSize && i == lastIndex && iter.hasNext() ) {
                        throw new IllegalArgumentException("Iterable must have exactly 5 elements in order to create a Quintet.");
                    }

                    elements.add(element);
                }
            } else {
                if( i < index ) {
                    throw new IllegalArgumentException(String.format("Iterable has not enough elements to grab a value from index %d", index));
                } else {
                    throw new IllegalArgumentException(String.format("Not enough elements for creating a Quintet (5 needed, %d given)", i));
                }
            }
        }

        return new Quintet<>(elements.get(0), elements.get(1), elements.get(2), elements.get(3), elements.get(4));
    }

    public Quintet(final A value0, final B value1, final C value2, final D value3, final E value4) {
        super(value0, value1, value2, value3, value4);
        this.val0 = value0;
        this.val1 = value1;
        this.val2 = value2;
        this.val3 = value3;
        this.val4 = value4;
    }

    @Override
    public A getValue0() {
        return this.val0;
    }

    @Override
    public B getValue1() {
        return this.val1;
    }

    @Override
    public C getValue2() {
        return this.val2;
    }

    @Override
    public D getValue3() {
        return this.val3;
    }

    @Override
    public E getValue4() {
        return this.val4;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
