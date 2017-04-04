package net.darkhax.bookshelf.util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public final class AnnotationUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private AnnotationUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Finds all classes annotated with the annotation class. These classes are then
     * instantiated, added to a list, and given to you.
     *
     * @param asmDataTable The ASMDataTable created by Forge. You can get this from most of the
     *        main mod loading stage events.
     * @param annotation The class of the annotation you're using to search for.
     * @param instance The class of the thing you're trying to construct. This should be a
     *        shared interface, or parent class.
     * @return A list of all classes annotated with the annotation, as instances.
     */
    public static <T, A extends Annotation> Map<T, A> getAnnotations (ASMDataTable asmDataTable, Class<A> annotation, Class<T> instance) {

        final Map<T, A> map = new HashMap<>();

        for (final ASMDataTable.ASMData asmData : asmDataTable.getAll(annotation.getCanonicalName())) {

            try {

                final Class<?> asmClass = Class.forName(asmData.getClassName());
                final Class<? extends T> asmInstanceClass = asmClass.asSubclass(instance);
                map.put(asmInstanceClass.newInstance(), asmInstanceClass.getAnnotation(annotation));
            }

            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

                Constants.LOG.warn("Could not load " + asmData.getClassName(), e);
            }
        }

        return map;
    }

}