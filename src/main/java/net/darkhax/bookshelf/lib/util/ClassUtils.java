package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.lib.Constants;

public final class ClassUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private ClassUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * A basic check to see if two classes are the same. For the classes to be the same,
     * neither can be null, and they must share the same name.
     *
     * @param class1: The first class to compare.
     * @param class2: The second class to compare.
     * @return boolean: True if neither class is null, and both share the same name.
     */
    public static boolean compareClasses (Class<?> class1, Class<?> class2) {

        return class1 != null && class2 != null && class1.getName().equalsIgnoreCase(class2.getName());
    }

    /**
     * Compares the class of an Object with another class. Useful for comparing a TileEntity or
     * Item.
     *
     * @param obj: The Object to compare.
     * @param clazz: The class to compare the Object to.
     * @return boolean: True if the Object is of the same class as the one provided.
     */
    public static boolean compareObjectToClass (Object obj, Class<?> clazz) {

        return compareClasses(obj.getClass(), clazz);
    }

    /**
     * Provides a safe way to get a class by its name. This is essentially the same as
     * Class.forName however it will handle any ClassNotFoundException automatically.
     *
     * @param name: The name of the class you are trying to get. Example: java.lang.String
     * @return Class: If a class could be found, it will be returned. Otherwise, null.
     */
    public static Class<?> getClassFromString (String name) {

        try {

            return Class.forName(name);
        }

        catch (final ClassNotFoundException e) {

            Constants.LOG.warn(e);
            return null;
        }
    }
}