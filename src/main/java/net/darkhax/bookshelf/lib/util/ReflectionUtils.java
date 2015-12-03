package net.darkhax.bookshelf.lib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;

public final class ReflectionUtils {
    
    /**
     * A map of cached Methods, used to make reflection more efficient. The key is the class
     * name, followed by the SRG method name and an underscore in between.
     */
    private static Map<String, Method> cachedMethods = Maps.newHashMap();
    
    /**
     * A map of cached Fields, used to make reflection more efficient. Tke key is the class
     * name, followed byt he SRG field name and an underscore in between.
     */
    private static Map<String, Field> cachedFields = Maps.newHashMap();
    
    /**
     * A simple check to see if a class exists.
     * 
     * @param className: The name of the class you are looking for.
     * @return boolean: Whether or not the specified class exists in the current environment.
     */
    public static boolean doesClassExist (String className) {
        
        try {
            
            Class.forName(className, false, null);
            return true;
        }
        
        catch (ClassNotFoundException exception) {
            
            return false;
        }
    }
    
    /**
     * Retrieves a Class by its name.
     * 
     * @param className: The name of the class you are trying to get.
     * @return <T> Class<T>: The Class that was retrieved.
     */
    public static <T> Class<T> getClass (String className) {
        
        try {
            
            return getCasted(Class.forName(className));
        }
        
        catch (Exception exception) {
            
            return null;
        }
    }
    
    /**
     * Sets the value of a cached field.
     * 
     * @param classToAccess: The Class that contains the Field being set.
     * @param instance: An instance of the Class that contains the Field.
     * @param mcpName: The MCP mapping for the field name.
     * @param srgName: The SRG mapping for the field name.
     * @param value: The new value being set for the Field.
     */
    public static <T, E> void setCachedFieldValue (Class<? super E> classToAccess, E instance, String mcpName, String srgName, T value) {
        
        Field field = getCachedField(classToAccess, mcpName, srgName);
        
        try {
            
            field.set(instance, value);
        }
        
        catch (Throwable exception) {
            
            throw new UnableToSetFieldException(exception);
        }
    }
    
    /**
     * Retrieves the value of a cached Field.
     * 
     * @param classToAccess: The Class that contains the Field being used.
     * @param instance: An instance of the Class that contains the Field.
     * @param mcpName: The MCP mapping for the field name.
     * @param srgName: The SRG mapping for the field name.
     * @return <T, E> T: The value that the Field was set to.
     */
    public static <T, E> T getCachedFieldValue (Class<? super E> classToAccess, E instance, String mcpName, String srgName) {
        
        Field field = getCachedField(classToAccess, mcpName, srgName);
        
        try {
            
            return (T) field.get(instance);
        }
        
        catch (Exception exception) {
            
            throw new UnableToGetFieldException(exception);
        }
    }
    
    /**
     * Invokes a Method that has been cached.
     * 
     * @param classToAccess: The Class that contains the method being accessed.
     * @param instance: An instance of the Class that contains the method.
     * @param mcpName: The MCP mapping for the method name.
     * @param srgName: The SRG mapping for the method name.
     * @param parameterTypes: The parameters used by this Method.
     * @param parameterValues: The parameter values to pass to the method.
     * @return <T, E> T: The data returned by the method.
     */
    public static <T, E> T invokeCachedMethod (Class<? super E> classToAccess, E instance, String mcpName, String srgName, Class<?>[] parameterTypes, Object[] parameterValues) {
        
        Method method = getCachedMethod(classToAccess, mcpName, srgName, parameterTypes);
        
        try {
            
            return (T) method.invoke(instance, parameterValues);
        }
        
        catch (Exception exception) {
            
            throw new UnableToInvokeMethodException(exception);
        }
    }
    
    /**
     * Retrieves a cached Field from the Field cache. This is used to re-access fields accessed
     * through reflection.
     * 
     * @param classToAccess: The Class that contains the field being accessed.
     * @param mcpName: The MCP mapping for the field name.
     * @param srgName: The SRG mapping for the field name.
     * @return Field: A Field object which represents the field being accessed.
     */
    public static Field getCachedField (Class<?> classToAccess, String mcpName, String srgName) {
        
        String key = classToAccess.getCanonicalName() + '_' + srgName;
        
        if (cachedFields.containsKey(key))
            return cachedFields.get(key);
            
        return cacheAccessedField(classToAccess, mcpName, srgName);
    }
    
    /**
     * Caches a field that was accessed through reflections. This is used to make re-accessing
     * that field more efficient. This is for private use only.
     * 
     * @param classToAccess: The Class that contains the field being accessed.
     * @param mcpName: The MCP mapping for the field name.
     * @param srgName: The SRG mapping for the field name.
     * @return Field: A Field object which represents the field being accessed.
     */
    private static Field cacheAccessedField (Class<?> classToAccess, String mcpName, String srgName) {
        
        Field method;
        String key = classToAccess.getCanonicalName() + '_' + srgName;
        
        try {
            
            method = classToAccess.getDeclaredField(srgName);
            method.setAccessible(true);
            cachedFields.put(key, method);
            return method;
        }
        
        catch (Throwable ex1) {
            
            try {
                
                method = classToAccess.getDeclaredField(mcpName);
                method.setAccessible(true);
                cachedFields.put(key, method);
                return method;
            }
            
            catch (Throwable ex2) {
                
                throw new UnableToFindFieldException(ex2);
            }
        }
    }
    
    /**
     * Retrieves a cached Method from the method cache. This is used to re-access methods
     * accessed via reflection.
     * 
     * @param classToAccess: The Class that contains the method you want.
     * @param mcpName: The MCP mapping for the method name.
     * @param srgName: The SRG mapping for the method name.
     * @param parameterTypes: The parameters that are used by the method.
     * @return Method: A Method object which represents the method being found.
     */
    public static Method getCachedMethod (Class<?> classToAccess, String mcpName, String srgName, Class<?>... parameterTypes) {
        
        String key = classToAccess.getCanonicalName() + '_' + srgName;
        
        if (cachedMethods.containsKey(key))
            return cachedMethods.get(key);
            
        return cacheAccessedMethod(classToAccess, mcpName, srgName, parameterTypes);
    }
    
    /**
     * Caches a Method accessed through reflection to a cache. This makes it easier and more
     * efficient to access the method more than once. For internal use only.
     * 
     * @param classToAccess: The Class that contains the method being accessed.
     * @param mcpName: The MCP mapping for the method name.
     * @param srgName: The SRG mapping for the method name.
     * @param parameterTypes: The parameters that are used by this method.
     * @return Method: A Method object which represents the newly cached method.
     */
    private static Method cacheAccessedMethod (Class<?> classToAccess, String mcpName, String srgName, Class<?>... parameterTypes) {
        
        Method method;
        String key = classToAccess.getCanonicalName() + '_' + srgName;
        
        try {
            
            method = classToAccess.getDeclaredMethod(srgName, parameterTypes);
            method.setAccessible(true);
            cachedMethods.put(key, method);
            return method;
        }
        
        catch (Throwable ex1) {
            
            try {
                
                method = classToAccess.getDeclaredMethod(mcpName, parameterTypes);
                method.setAccessible(true);
                cachedMethods.put(key, method);
                return method;
            }
            
            catch (Throwable ex2) {
                
                throw new UnableToFindMethodException(ex2);
            }
        }
    }
    
    /**
     * Casts an Object to a generic Type.
     * 
     * @param obj: The Object to cast.
     * @return <T> T: The Object as a generic Type.
     */
    public static <T> T getCasted (Object obj) {
        
        return (T) obj;
    }
    
    public static class UnableToSetFieldException extends RuntimeException {
        
        /**
         * An exception thrown when an attempt is made to set a field via reflection, however
         * the field could not be set.
         * 
         * @param exception: An instance of the exception being thrown.
         */
        public UnableToSetFieldException(Throwable exception) {
            
            super(exception);
        }
    }
    
    public static class UnableToGetFieldException extends RuntimeException {
        
        /**
         * An exception thrown when an attempt is made to retrieve a field via reflection.
         * 
         * @param exception: An instance of the exception being thrown.
         */
        public UnableToGetFieldException(Throwable exception) {
            
            super(exception);
        }
    }
    
    public static class UnableToInvokeMethodException extends RuntimeException {
        
        /**
         * An exception thrown when an attempt to invoke a method via reflection has failed.
         * 
         * @param exception: An instance of the exception being thrown.
         */
        public UnableToInvokeMethodException(Throwable exception) {
            
            super(exception);
        }
    }
    
    public static class UnableToFindMethodException extends RuntimeException {
        
        /**
         * An exception thrown when an attempt to look up a method is made, but it could not be
         * found.
         * 
         * @param exception: An instance of the exception being thrown.
         */
        public UnableToFindMethodException(Throwable exception) {
            
            super(exception);
        }
    }
    
    public static class UnableToFindFieldException extends RuntimeException {
        
        /**
         * An exception thrown when an attempt to look up a field is made, but it could not be
         * found.
         * 
         * @param exception: An instance of the exception being thrown.
         */
        public UnableToFindFieldException(Throwable exception) {
            
            super(exception);
        }
    }
}