/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.config;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.AnnotationUtils;
import net.darkhax.bookshelf.util.CollectionUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigurationHandler {

    /**
     * A cache of all the config classes associated with a config file. Allows for multiple
     * mods and config files to exist, without rebuilding the list every time. This is
     * generated the first time {@link #init(ASMDataTable)} is called.
     */
    public static final Multimap<String, Class<?>> CLASS_CACHE = HashMultimap.create();

    /**
     * A map of adapters. These are used to read/write a config option for a specific type. The
     * key is the class of the type being handled, and the value is a functional interface
     * which is used to process the config data.
     */
    public static final Map<Class<?>, Adapter> handlers = new HashMap<>();

    /**
     * The name of the configuration file. Probably the modid.
     */
    private final String name;

    /**
     * The actual file on the hard drive.
     */
    private final File file;

    /**
     * Forge's configuration file.
     */
    private final Configuration config;

    /**
     * A cache of all configurable fields for this config file.
     */
    private Field[] properties;

    static {

        addReader(boolean.class, ConfigurationHandler::readBoolean);
        addReader(boolean[].class, ConfigurationHandler::readBooleanArray);
        addReader(double.class, ConfigurationHandler::readDouble);
        addReader(double[].class, ConfigurationHandler::readDoubleArray);
        addReader(float.class, ConfigurationHandler::readFloat);
        addReader(float[].class, ConfigurationHandler::readFloatArray);
        addReader(int.class, ConfigurationHandler::readInt);
        addReader(int[].class, ConfigurationHandler::readIntArray);
        addReader(String.class, ConfigurationHandler::readString);
        addReader(String[].class, ConfigurationHandler::readStringArray);
        addReader(ResourceLocation.class, ConfigurationHandler::readIdentifier);
        addReader(ResourceLocation[].class, ConfigurationHandler::readIdentifierArray);
        addReader(Item.class, ConfigurationHandler::readItem);
        addReader(Item[].class, ConfigurationHandler::readItemArray);
        addReader(Block.class, ConfigurationHandler::readBlock);
        addReader(Block[].class, ConfigurationHandler::readBlockArray);
        addReader(ItemStack.class, ConfigurationHandler::readItemStack);
        addReader(ItemStack[].class, ConfigurationHandler::readItemStackArray);
        addReader(BlockPos.class, ConfigurationHandler::readPos);
        addReader(Color.class, ConfigurationHandler::readColor);
    }

    /**
     * Base constructor for a configuration handler. The purpose of this class is to provide a
     * basic wrapper for Forge's configuration, but mostly to add support for special
     * configuration based annotations.
     *
     * @param name The name of the config file to represent. This should be all lower case and
     *        have no spaces. Basic file name rules. The .cfg extension and forge config
     *        directory is added automatically.
     */
    public ConfigurationHandler (String name) {

        this.name = name;
        this.file = new File(Loader.instance().getConfigDir(), name.toLowerCase() + ".cfg");
        this.config = new Configuration(this.file);
    }

    /**
     * Initializes the ConfigurationHandler. This should be called after you create your
     * configuration handler.
     *
     * @param table The ASMDataTable. You can get this from your fml construction events.
     */
    public void init (ASMDataTable table) {

        if (CLASS_CACHE.isEmpty()) {

            for (final Tuple<Class<?>, Config> data : AnnotationUtils.getAnnotatedClasses(table, Config.class)) {

                if (data.getSecond() != null) {

                    CLASS_CACHE.put(data.getSecond().name(), data.getFirst());
                }
            }
        }

        this.properties = AnnotationUtils.getAnnotatedFields(CLASS_CACHE.get(this.name), Configurable.class).toArray(new Field[] {});
    }

    /**
     * Syncs the config file.
     */
    public void sync () {

        this.config.load();
        for (final Field field : this.properties) {

            if (!(Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))) {

                Constants.LOG.info("Configuration fields must be public, and static!");
                continue;
            }

            field.setAccessible(true);
            final Configurable configurable = field.getAnnotation(Configurable.class);
            final String propName = configurable.name().isEmpty() ? field.getName() : configurable.name();
            final Class<?> type = field.getType();

            if (handlers.containsKey(type)) {

                try {

                    // TODO look into a better way to handle defaults
                    // TOOD look into adding world restart and game restart anotes
                    final Object result = handlers.get(type).read(propName, configurable.category(), configurable.description(), this.config, field.get(null));
                    field.set(null, result);
                }

                catch (IllegalArgumentException | IllegalAccessException e) {

                    Constants.LOG.info("Error handling configurable field! Could not read/write " + field.getName(), e);
                }
            }

            else {

                Constants.LOG.info("Unhandled type: " + type.getCanonicalName());
            }
        }

        if (this.hasChanged()) {
            this.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged (ConfigChangedEvent event) {

        if (event.getModID().equalsIgnoreCase(this.name)) {

            this.sync();
        }
    }

    public boolean hasChanged () {

        return this.config.hasChanged();
    }

    public void save () {

        this.config.save();
    }

    public Configuration getConfig () {

        return this.config;
    }

    public static <T extends Object> void addReader (Class<T> type, Adapter<T> reader) {

        handlers.put(type, reader);
    }

    // read/write methods

    private static boolean readBoolean (String name, String category, String comment, Configuration config, boolean object) {

        return setComment(config.get(category, name, object), comment).getBoolean();
    }

    private static boolean[] readBooleanArray (String name, String category, String comment, Configuration config, boolean[] object) {

        return setComment(config.get(category, name, object), comment).getBooleanList();
    }

    private static double readDouble (String name, String category, String comment, Configuration config, double object) {

        return setComment(config.get(category, name, object), comment).getDouble();
    }

    private static double[] readDoubleArray (String name, String category, String comment, Configuration config, double[] object) {

        return setComment(config.get(category, name, object), comment).getDoubleList();
    }

    private static float readFloat (String name, String category, String comment, Configuration config, float object) {

        return (float) readDouble(name, category, comment, config, object);
    }

    private static float[] readFloatArray (String name, String category, String comment, Configuration config, float[] object) {

        return CollectionUtils.toFloat(readDoubleArray(name, category, comment, config, CollectionUtils.toDouble(object)));
    }

    private static int readInt (String name, String category, String comment, Configuration config, int object) {

        return setComment(config.get(category, name, object), comment).getInt();
    }

    private static int[] readIntArray (String name, String category, String comment, Configuration config, int[] object) {

        return setComment(config.get(category, name, object), comment).getIntList();
    }

    private static String readString (String name, String category, String comment, Configuration config, String object) {

        return setComment(config.get(category, name, object), comment).getString();
    }

    private static String[] readStringArray (String name, String category, String comment, Configuration config, String[] object) {

        return setComment(config.get(category, name, object), comment).getStringList();
    }

    private static ResourceLocation readIdentifier (String name, String category, String comment, Configuration config, ResourceLocation object) {

        return new ResourceLocation(readString(name, category, comment, config, object.toString()));
    }

    private static ResourceLocation[] readIdentifierArray (String name, String category, String comment, Configuration config, ResourceLocation[] object) {

        return CollectionUtils.toIdentifier(readStringArray(name, category, comment, config, CollectionUtils.toString(object)));
    }

    private static Item readItem (String name, String category, String comment, Configuration config, Item object) {

        return ForgeRegistries.ITEMS.getValue(readIdentifier(name, category, comment, config, object.getRegistryName()));
    }

    private static Item[] readItemArray (String name, String category, String comment, Configuration config, Item[] object) {

        return CollectionUtils.toItem(readIdentifierArray(name, category, comment, config, CollectionUtils.toIdentifier(object)));
    }

    private static Block readBlock (String name, String category, String comment, Configuration config, Block object) {

        return ForgeRegistries.BLOCKS.getValue(readIdentifier(name, category, comment, config, object.getRegistryName()));
    }

    private static Block[] readBlockArray (String name, String category, String comment, Configuration config, Block[] object) {

        return CollectionUtils.toBlock(readIdentifierArray(name, category, comment, config, CollectionUtils.toIdentifier(object)));
    }

    private static ItemStack readItemStack (String name, String category, String comment, Configuration config, ItemStack object) {

        return StackUtils.createStackFromString(readString(name, category, comment, config, StackUtils.writeStackToString(object)));
    }

    private static ItemStack[] readItemStackArray (String name, String category, String comment, Configuration config, ItemStack[] object) {

        return CollectionUtils.toStack(readStringArray(name, category, comment, config, CollectionUtils.toString(object)));
    }

    private static Color readColor (String name, String category, String comment, Configuration config, Color object) {

        return CollectionUtils.toColor(readIntArray(name, category, comment, config, CollectionUtils.toInt(object)));
    }

    private static BlockPos readPos (String name, String category, String comment, Configuration config, BlockPos object) {

        return CollectionUtils.toPos(readIntArray(name, category, comment, config, CollectionUtils.toInt(object)));
    }

    private static Property setComment (Property property, String comment) {

        property.setComment(comment);
        return property;
    }

    // Functional Interfaces

    public static interface Adapter<T extends Object> {

        public T read (String name, String category, String comment, Configuration config, T object);
    }
}