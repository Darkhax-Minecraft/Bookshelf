/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraftforge.fml.ModList;

public final class ModUtils {
    
    /**
     * Checks if a mod is present in the mod list.
     * 
     * @param modid The mod id to look for.
     * @return Whether or not the mod id is present in the current mod list.
     */
    public static boolean isInModList (String modid) {
        
        return ModList.get().isLoaded(modid);
    }
    
    /**
     * Invokes a callable function if a mod is present in the mod list.
     * 
     * @param <T> The type to be returned by the callable function.
     * @param modid The mod id to look for.
     * @param toRun The callable function to invoke if the mod is present.
     * @return If the mod was present the result of the callable function will be returned.
     *         Otherwise this will be null.
     */
    @Nullable
    public static <T> T callIfPresent (String modid, Supplier<Callable<T>> toRun) {
        
        return callIfPresent(modid, toRun, null);
    }
    
    /**
     * Invokes a callable function if a mod is present in the mod list. If that mod is not
     * present a second optional callable function will be invoked.
     * 
     * @param <T> The type to be returned by the callable function. Must be the same for both
     *        present and not present functions.
     * @param modid The mod id to look for.
     * @param ifPresent The callable function to invoke if the mod is present.
     * @param notPresent The callable function to invoke if the mod is not present. May be
     *        null.
     * @return If the mod was present the result of the present callable function will be
     *         returned. If the mod is not present and the notPresent callable function is not
     *         null it's result will be returned. If neither callable function can be called
     *         the result will be null.
     */
    @Nullable
    public static <T> T callIfPresent (String modid, Supplier<Callable<T>> ifPresent, @Nullable Supplier<Callable<T>> notPresent) {
        
        try {
            
            if (isInModList(modid)) {
                
                return ifPresent.get().call();
            }
            
            else if (notPresent != null) {
                
                return notPresent.get().call();
            }
        }
        
        catch (final Exception e) {
            
            throw new RuntimeException(e);
        }
        
        return null;
    }
    
    /**
     * Runs a function if a mod is present in the mod list.
     * 
     * @param modid The mod id to look for.
     * @param ifPresent The runnable function to run if the mod is present.
     */
    public static void runIfPresent (String modid, Supplier<Runnable> ifPresent) {
        
        runIfPresent(modid, ifPresent, null);
    }
    
    /**
     * Runs a function if a mod is present in the mod list. If the mod is not present a second
     * optional function will be run.
     * 
     * @param modid The mod id to look for.
     * @param ifPresent The runnable function to run if the mod is present.
     * @param notPresent An optional runnable function to run if the mod is not present.
     */
    public static void runIfPresent (String modid, Supplier<Runnable> ifPresent, @Nullable Supplier<Runnable> notPresent) {
        
        if (isInModList(modid)) {
            
            ifPresent.get().run();
        }
        
        else if (notPresent != null) {
            
            notPresent.get().run();
        }
    }
    
    /**
     * Invokes a supplier if a mod is present in the mod list. If the mod is not present a
     * second optional supplier will be invoked.
     * 
     * @param <T> The type for the suppliers to return. This must be the same for both.
     * @param modid The mod id to look for.
     * @param ifPresent The supplier to invoke if the mod is present.
     * @return If the mod is present the result of the ifPresent supplier will be returned. If
     *         the mod is not present the result will be null.
     */
    public static <T> T supplyIfPresent (String modid, Supplier<Supplier<T>> ifPresent) {
        
        return supplyIfPresent(modid, ifPresent, null);
    }
    
    /**
     * Invokes a supplier if a mod is present in the mod list. If the mod is not present a
     * second optional supplier will be invoked.
     * 
     * @param <T> The type for the suppliers to return. This must be the same for both.
     * @param modid The mod id to look for.
     * @param ifPresent The supplier to invoke if the mod is present.
     * @param notPresent An optional supplier to invoke if the mod is not present.
     * @return If the mod is present the result of the ifPresent supplier will be returned. If
     *         the mod is not present and the notPresent supplier is not null it's result will
     *         be returned. Otherwise null will be returned as a fallback.
     */
    @Nullable
    public static <T> T supplyIfPresent (String modid, Supplier<Supplier<T>> ifPresent, @Nullable Supplier<Supplier<T>> notPresent) {
        
        if (isInModList(modid)) {
            
            return ifPresent.get().get();
        }
        
        else if (notPresent != null) {
            
            return notPresent.get().get();
        }
        
        return null;
    }
}