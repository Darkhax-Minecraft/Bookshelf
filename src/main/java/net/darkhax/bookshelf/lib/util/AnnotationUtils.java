package net.darkhax.bookshelf.lib.util;

import java.util.ArrayList;
import java.util.List;

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
    public static <T> List<T> getAnnotations (ASMDataTable asmDataTable, Class<?> annotation, Class<T> instance) {
        
        List<T> instances = new ArrayList<T>();
        
        for (ASMDataTable.ASMData asmData : asmDataTable.getAll(annotation.getCanonicalName())) {
            
            try {
                
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instance);
                instances.add(asmInstanceClass.newInstance());
            }
            
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                
                Constants.LOG.warn("Could not load " + asmData.getClassName(), e);
            }
        }
        return instances;
    }
}