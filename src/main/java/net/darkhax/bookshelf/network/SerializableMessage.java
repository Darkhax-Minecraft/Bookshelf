/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [11/01/2016, 22:00:30 (GMT)]
 *
 * Changes
 * - Added a serial version UID
 * - Renamed to SerializableMessage
 * - Formated code using Bookshelf's formatter
 * - Added a bunch of javadocs
 * - Changed HashMap fields to Map
 * - Made handleMessage abstract so overriding is enforced
 * - Changed some field/method names/params to be less lazy
 * - Added resource location serialization
 */
package net.darkhax.bookshelf.network;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class SerializableMessage<REQ extends SerializableMessage> implements Serializable, IMessage, IMessageHandler<REQ, IMessage> {

    /**
     * The serial version for this UID.
     */
    private static final long serialVersionUID = 3214832642504369023L;

    /**
     * A map which holds all the serialization handlers.
     */
    private static final Map<Class<?>, Pair<Reader, Writer>> handlers = new HashMap();

    /**
     * A cache of fields in a given class for faster lookup.
     */
    private static final Map<Class<?>, Field[]> fieldCache = new HashMap();

    static {

        addIOHandler(byte.class, SerializableMessage::readByte, SerializableMessage::writeByte);
        addIOHandler(short.class, SerializableMessage::readShort, SerializableMessage::writeShort);
        addIOHandler(int.class, SerializableMessage::readInt, SerializableMessage::writeInt);
        addIOHandler(long.class, SerializableMessage::readLong, SerializableMessage::writeLong);
        addIOHandler(float.class, SerializableMessage::readFloat, SerializableMessage::writeFloat);
        addIOHandler(double.class, SerializableMessage::readDouble, SerializableMessage::writeDouble);
        addIOHandler(boolean.class, SerializableMessage::readBoolean, SerializableMessage::writeBoolean);
        addIOHandler(char.class, SerializableMessage::readChar, SerializableMessage::writeChar);
        addIOHandler(String.class, SerializableMessage::readString, SerializableMessage::writeString);
        addIOHandler(NBTTagCompound.class, SerializableMessage::readNBT, SerializableMessage::writeNBT);
        addIOHandler(ItemStack.class, SerializableMessage::readItemStack, SerializableMessage::writeItemStack);
        addIOHandler(BlockPos.class, SerializableMessage::readBlockPos, SerializableMessage::writeBlockPos);
        addIOHandler(ResourceLocation.class, SerializableMessage::readResourceLocation, SerializableMessage::writeResourceLocation);
    }

    /**
     * Called when the message is received and handled. This is where you process the message.
     *
     * @param context The context for the message.
     * @return A message to send as a response.
     */
    public abstract IMessage handleMessage (MessageContext context);

    @Override
    public final IMessage onMessage (REQ message, MessageContext context) {

        return message.handleMessage(context);
    }

    @Override
    public final void fromBytes (ByteBuf buf) {

        try {
            final Class<?> clazz = this.getClass();
            final Field[] clFields = getClassFields(clazz);
            for (final Field f : clFields) {
                final Class<?> type = f.getType();
                if (acceptField(f, type)) {
                    this.readField(f, type, buf);
                }
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Error at reading packet " + this, e);
        }
    }

    @Override
    public final void toBytes (ByteBuf buf) {

        try {
            final Class<?> clazz = this.getClass();
            final Field[] clFields = getClassFields(clazz);
            for (final Field f : clFields) {
                final Class<?> type = f.getType();
                if (acceptField(f, type)) {
                    this.writeField(f, type, buf);
                }
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Error at writing packet " + this, e);
        }
    }

    /**
     * Gets an array of fields from a class. These arrays will be cached.
     *
     * @param clazz The class to get fields for.
     * @return An array of fields held by the class.
     */
    private static Field[] getClassFields (Class<?> clazz) {

        if (fieldCache.containsValue(clazz)) {
            return fieldCache.get(clazz);
        }
        else {
            final Field[] fields = clazz.getFields();
            Arrays.sort(fields, (Field f1, Field f2) -> {
                return f1.getName().compareTo(f2.getName());
            });
            fieldCache.put(clazz, fields);
            return fields;
        }
    }

    /**
     * Writes the value of a field to the packet byte buffer.
     *
     * @param field The field to write.
     * @param type The type for the field.
     * @param buf The buffer to write to.
     */
    private final void writeField (Field field, Class<?> type, ByteBuf buf) throws IllegalArgumentException, IllegalAccessException {

        final Pair<Reader, Writer> handler = getHandler(type);
        handler.getRight().write(field.get(this), buf);
    }

    /**
     * Reads the value of a field from the packet byte buffer.
     *
     * @param field The field to read.
     * @param type The type for the field.
     * @param buf The buffer to read from.
     */
    private final void readField (Field field, Class<?> type, ByteBuf buf) throws IllegalArgumentException, IllegalAccessException {

        final Pair<Reader, Writer> handler = getHandler(type);
        field.set(this, handler.getLeft().read(buf));
    }

    /**
     * Gets the read/write handler for a type.
     *
     * @param type The type to get the IO handler for.
     * @return The read and write handler for the class.
     */
    private static Pair<Reader, Writer> getHandler (Class<?> type) {

        final Pair<Reader, Writer> pair = handlers.get(type);
        if (pair == null) {
            throw new RuntimeException("No R/W handler for  " + type);
        }
        return pair;
    }

    /**
     * Checks if a field is acceptable for message IO.
     *
     * @param field The field to check.
     * @param type The type for the field.
     * @return Whether or not the field should be accepted.
     */
    private static boolean acceptField (Field field, Class<?> type) {

        final int mods = field.getModifiers();
        if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods)) {
            return false;
        }

        return handlers.containsKey(type);
    }

    /**
     * Adds a new read/write IO handler for messages.
     *
     * @param type The type to add the IO handler for.
     * @param reader The reader function.
     * @param writer The writer function.
     */
    public static <T extends Object> void addIOHandler (Class<T> type, Reader<T> reader, Writer<T> writer) {

        handlers.put(type, Pair.of(reader, writer));
    }

    private static byte readByte (ByteBuf buf) {

        return buf.readByte();
    }

    private static void writeByte (byte b, ByteBuf buf) {

        buf.writeByte(b);
    }

    private static short readShort (ByteBuf buf) {

        return buf.readShort();
    }

    private static void writeShort (short s, ByteBuf buf) {

        buf.writeShort(s);
    }

    private static int readInt (ByteBuf buf) {

        return buf.readInt();
    }

    private static void writeInt (int i, ByteBuf buf) {

        buf.writeInt(i);
    }

    private static long readLong (ByteBuf buf) {

        return buf.readLong();
    }

    private static void writeLong (long l, ByteBuf buf) {

        buf.writeLong(l);
    }

    private static float readFloat (ByteBuf buf) {

        return buf.readFloat();
    }

    private static void writeFloat (float f, ByteBuf buf) {

        buf.writeFloat(f);
    }

    private static double readDouble (ByteBuf buf) {

        return buf.readDouble();
    }

    private static void writeDouble (double d, ByteBuf buf) {

        buf.writeDouble(d);
    }

    private static boolean readBoolean (ByteBuf buf) {

        return buf.readBoolean();
    }

    private static void writeBoolean (boolean b, ByteBuf buf) {

        buf.writeBoolean(b);
    }

    private static char readChar (ByteBuf buf) {

        return buf.readChar();
    }

    private static void writeChar (char c, ByteBuf buf) {

        buf.writeChar(c);
    }

    private static String readString (ByteBuf buf) {

        return ByteBufUtils.readUTF8String(buf);
    }

    private static void writeString (String s, ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, s);
    }

    private static NBTTagCompound readNBT (ByteBuf buf) {

        return ByteBufUtils.readTag(buf);
    }

    private static void writeNBT (NBTTagCompound cmp, ByteBuf buf) {

        ByteBufUtils.writeTag(buf, cmp);
    }

    private static ItemStack readItemStack (ByteBuf buf) {

        return ByteBufUtils.readItemStack(buf);
    }

    private static void writeItemStack (ItemStack stack, ByteBuf buf) {

        ByteBufUtils.writeItemStack(buf, stack);
    }

    private static BlockPos readBlockPos (ByteBuf buf) {

        return BlockPos.fromLong(buf.readLong());
    }

    private static void writeBlockPos (BlockPos pos, ByteBuf buf) {

        buf.writeLong(pos.toLong());
    }

    private static ResourceLocation readResourceLocation (ByteBuf buf) {

        return new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    private static void writeResourceLocation (ResourceLocation location, ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, location.toString());
    }

    // Functional interfaces
    public static interface Writer<T extends Object> {

        public void write (T t, ByteBuf buf);
    }

    public static interface Reader<T extends Object> {

        public T read (ByteBuf buf);
    }
}