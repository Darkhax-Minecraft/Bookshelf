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
 * A tuple of ten elements.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez, edited by SanAndreasP
 * @since 1.0
 */
public final class Decade<A, B, C, D, E, F, G, H, I, J>
        extends Tuple
        implements IValue0<A>, IValue1<B>, IValue2<C>, IValue3<D>, IValue4<E>, IValue5<F>, IValue6<G>, IValue7<H>, IValue8<I>, IValue9<J>
{
    private static final long serialVersionUID = -1607420937567707033L;
    private static final int SIZE = 10;

    private final A val0;
    private final B val1;
    private final C val2;
    private final D val3;
    private final E val4;
    private final F val5;
    private final G val6;
    private final H val7;
    private final I val8;
    private final J val9;

    public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> with(final A value0, final B value1, final C value2, final D value3,
                                                                                           final E value4, final F value5, final G value6, final H value7,
                                                                                           final I value8, final J value9) {
        return new Decade<>(value0, value1, value2, value3, value4, value5, value6, value7, value8, value9);
    }

    /**
     * <p>
     * Create tuple from array. Array has to have exactly ten elements.
     * </p>
     *
     * @param <X>   the array component type
     * @param array the array to be converted to a tuple
     * @return the tuple
     */
    public static <X> Decade<X, X, X, X, X, X, X, X, X, X> fromArray(final X[] array) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if( array.length != 10 ) {
            throw new IllegalArgumentException("Array must have exactly 10 elements in order to create a Decade. Size is " + array.length);
        }

        return new Decade<>(array[0], array[1], array[2], array[3], array[4], array[5], array[6], array[7], array[8], array[9]);
    }

    /**
     * <p>
     * Create tuple from collection. Collection has to have exactly ten elements.
     * </p>
     *
     * @param <X>        the collection component type
     * @param collection the collection to be converted to a tuple
     * @return the tuple
     */
    public static <X> Decade<X, X, X, X, X, X, X, X, X, X> fromCollection(final Collection<X> collection) {
        return fromIterable(collection);
    }

    /**
     * <p>
     * Create tuple from iterable. Iterable has to have exactly ten elements.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Decade<X, X, X, X, X, X, X, X, X, X> fromIterable(final Iterable<X> iterable) {
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
    public static <X> Decade<X, X, X, X, X, X, X, X, X, X> fromIterable(final Iterable<X> iterable, int index) {
        return fromIterable(iterable, index, false);
    }

    private static <X> Decade<X, X, X, X, X, X, X, X, X, X> fromIterable(final Iterable<X> iterable, int index, final boolean checkSize) {
        if( iterable == null ) {
            throw new IllegalArgumentException("Iterable cannot be null");
        }

        X element;
        ArrayList<X> elements = new ArrayList<>(10);
        final Iterator<X> iter = iterable.iterator();
        int lastIndex = index + SIZE - 1;

        for( int i = 0; i <= lastIndex; i++ ) {
            if( iter.hasNext() ) {
                element = iter.next();
                if( i >= index ) {
                    if( checkSize && i == lastIndex && iter.hasNext() ) {
                        throw new IllegalArgumentException("Iterable must have exactly 10 elements in order to create a Decade.");
                    }

                    elements.add(element);
                }
            } else {
                if( i < index ) {
                    throw new IllegalArgumentException(String.format("Iterable has not enough elements to grab a value from index %d", index));
                } else {
                    throw new IllegalArgumentException(String.format("Not enough elements for creating a Decade (10 needed, %d given)", i));
                }
            }
        }

        return new Decade<>(elements.get(0), elements.get(1), elements.get(2), elements.get(3), elements.get(4), elements.get(5), elements.get(6), elements.get(7), elements.get(8), elements.get(9));
    }

    public Decade(final A value0, final B value1, final C value2, final D value3, final E value4, final F value5, final G value6, final H value7, final I value8,
                  final J value9) {
        super(value0, value1, value2, value3, value4, value5, value6, value7, value8, value9);
        this.val0 = value0;
        this.val1 = value1;
        this.val2 = value2;
        this.val3 = value3;
        this.val4 = value4;
        this.val5 = value5;
        this.val6 = value6;
        this.val7 = value7;
        this.val8 = value8;
        this.val9 = value9;
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
    public F getValue5() {
        return this.val5;
    }

    @Override
    public G getValue6() {
        return this.val6;
    }

    @Override
    public H getValue7() {
        return this.val7;
    }

    @Override
    public I getValue8() {
        return this.val8;
    }

    @Override
    public J getValue9() {
        return this.val9;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
