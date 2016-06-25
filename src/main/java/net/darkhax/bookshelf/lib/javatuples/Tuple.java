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

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import net.darkhax.bookshelf.lib.Constants;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * Abstract base class for all tuple classes.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez, edited by SanAndreasP
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class Tuple
        implements Iterable<Object>, Serializable, Comparable<Tuple>
{
    private static final long serialVersionUID = 5431085632328343101L;

    private final Object[] valueArray;
    private final List<Object> valueList;

    protected Tuple(final Object... values) {
        this.valueArray = values;
        this.valueList = Arrays.asList(values);
    }

    /**
     * <p>
     * Return the size of the tuple.
     * </p>
     *
     * @return the size of the tuple.
     */
    public abstract int getSize();

    /**
     * <p>
     * Get the value at a specific position in the tuple. This method
     * has to return object, so using it you will lose the type-safety you
     * get with the <tt>getValueX()</tt> methods.
     * </p>
     *
     * @param pos the position of the value to be retrieved.
     * @return the value
     */
    public final Object getValue(final int pos) {
        if( pos >= getSize() ) {
            throw new IllegalArgumentException(
                    String.format("Cannot retrieve value index %1$d in %2$s. Indices of %2$s range from 0 to %3$d",
                                  pos, this.getClass().getSimpleName(), (getSize() - 1))
            );
        }

        return this.valueArray[pos];
    }

    @Override
    public final Iterator<Object> iterator() {
        return this.valueList.iterator();
    }

    @Override
    public final String toString() {
        return this.valueList.toString();
    }

    public final boolean contains(final Object value) {
        for( final Object val : this.valueList ) {
            if( val == null ) {
                if( value == null ) {
                    return true;
                }
            } else {
                if( val.equals(value) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean containsAll(final Collection<?> collection) {
        for( final Object value : collection ) {
            if( !this.contains(value) ) {
                return false;
            }
        }

        return true;
    }

    public final boolean containsAll(final Object... values) {
        if( values == null ) {
            throw new IllegalArgumentException("containsAll needs at least 1 parameter or array cannot be null.");
        }

        for( final Object value : values ) {
            if( !contains(value) ) {
                return false;
            }
        }

        return true;
    }

    public final int indexOf(final Object value) {
        int i = 0;
        for( final Object val : this.valueList ) {
            if( val == null ) {
                if( value == null ) {
                    return i;
                }
            } else {
                if( val.equals(value) ) {
                    return i;
                }
            }
            i++;
        }

        return -1;
    }

    public final int lastIndexOf(final Object value) {
        for( int i = getSize() - 1; i >= 0; i-- ) {
            final Object val = this.valueList.get(i);
            if( val == null ) {
                if( value == null ) {
                    return i;
                }
            } else {
                if( val.equals(value) ) {
                    return i;
                }
            }
        }
        return -1;
    }

    public final List<Object> toList() {
        return Collections.unmodifiableList(new ArrayList<>(this.valueList));
    }

    public final Object[] toArray() {
        return this.valueArray.clone();
    }

    @Override
    public final int hashCode() {
        return 31 + this.valueList.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if( this == obj ) {
            return true;
        }

        if( obj == null ) {
            return false;
        }

        if( getClass() != obj.getClass() ) {
            return false;
        }

        final Tuple other = (Tuple) obj;
        return this.valueList.equals(other.valueList);
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public int compareTo(final Tuple o) {
        final int tLen = this.valueArray.length;
        final Object[] oValues = o.valueArray;
        final int oLen = oValues.length;

        for( int i = 0; i < tLen && i < oLen; i++ ) {
            final Comparable tElement = (Comparable) this.valueArray[i];
            final Comparable oElement = (Comparable) oValues[i];
            final int comparison = tElement.compareTo(oElement);

            if( comparison != 0 ) {
                return comparison;
            }
        }

        return (Integer.valueOf(tLen)).compareTo(oLen);
    }

    public static Tuple readFromByteBufStream(ByteBufInputStream stream) {
        try( ObjectDecoderInputStream odis = new ObjectDecoderInputStream(stream) ) {
            return (Tuple) odis.readObject();
        } catch( IOException | ClassNotFoundException ioEx ) {
            Constants.LOG.log(Level.ERROR, "Cannot deserialize Tuple!", ioEx);
        }

        return null;
    }

    public static void writeToByteBufStream(Tuple tuple, ByteBufOutputStream stream) {
        try( ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(stream) ) {
            oeos.writeObject(tuple);
        } catch( IOException ex ) {
            Constants.LOG.log(Level.ERROR, "Cannot serialize Tuple!", ex);
        }
    }

    public static Tuple from(Object... values) {
        switch( values.length ) {
            case 1:
                return Unit.fromArray(values);
            case 2:
                return Pair.fromArray(values);
            case 3:
                return Triplet.fromArray(values);
            case 4:
                return Quartet.fromArray(values);
            case 5:
                return Quintet.fromArray(values);
            case 6:
                return Sextet.fromArray(values);
            case 7:
                return Septet.fromArray(values);
            case 8:
                return Octet.fromArray(values);
            case 9:
                return Ennead.fromArray(values);
            case 10:
                return Decade.fromArray(values);
            default:
                throw new RuntimeException(String.format("Cannot create Tuple with size %d!", values.length));
        }
    }
}
