package net.darkhax.bookshelf.lib.util;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

import org.lwjgl.opengl.Display;

import cpw.mods.fml.common.FMLCommonHandler;

import net.darkhax.bookshelf.lib.Constants;

public final class MinecraftUtils {
    
    /**
     * Sets the title for the application window.
     * 
     * @param title: The new title for the application window.
     */
    public static void setAppTitle (String title) {
        
        Display.setTitle(title);
    }
    
    /**
     * Shuts down the Minecraft application.
     */
    public static void shutdownApp () {
        
        System.out.println();
        Constants.LOG.info("---=== Shutting down Minecraft! ===---");
        FMLCommonHandler.instance().exitJava(0, false);
    }
    
    /**
     * Restarts the Minecraft application. This will cause the game to completely reload
     * everything including textures, configurations and mods.
     */
    public static void restartApp () {
        
        try {
            
            String java = System.getProperty("java.home") + "/bin/javaw";
            List<String> vmArguments = new ArrayList<String>(ManagementFactory.getRuntimeMXBean().getInputArguments());
            Iterator<String> args = vmArguments.iterator();
            
            while (args.hasNext())
                if (args.next().contains("-agentlib"))
                    args.remove();
                    
            final List<String> cmd = new ArrayList<String>();
            cmd.add('"' + java + '"');
            String[] mainCommand = System.getProperty("sun.java.command").split(" ");
            
            if (mainCommand[0].endsWith(".jar")) {
                
                cmd.add("-jar");
                cmd.add(new File(mainCommand[0]).getPath());
            }
            
            else {
                
                cmd.add("-cp");
                cmd.add('"' + System.getProperty("java.class.path") + '"');
                cmd.add(mainCommand[0]);
            }
            
            cmd.addAll(Arrays.asList(mainCommand).subList(1, mainCommand.length));
            cmd.addAll(vmArguments);
            
            OutputStream os = System.out;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                
                @Override
                public void run () {
                    
                    try {
                        
                        ProcessBuilder builder = new ProcessBuilder(cmd);
                        builder.inheritIO();
                        builder.start();
                    }
                    
                    catch (IOException exception) {
                        
                        exception.printStackTrace();
                    }
                }
            });
            
            System.out.println();
            Constants.LOG.info("---=== Restarting Minecraft! ===---");
            FMLCommonHandler.instance().exitJava(0, false);
        }
        catch (Throwable exception) {
            
            throw new RejectedExecutionException("Error while trying to restart the application", exception);
        }
    }
}