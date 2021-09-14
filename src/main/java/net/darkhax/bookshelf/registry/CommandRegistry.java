package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommandRegistry {
    
    /**
     * A logger instance used for outputting info and debug info.
     */
    private final Logger logger;
    
    private final List<LiteralArgumentBuilder<CommandSource>> commands;
    private final Map<String, Tuple<Class, IArgumentSerializer>> commandArguments;
    
    public CommandRegistry(Logger logger) {
        
        this.logger = logger;
        this.commands = NonNullList.create();
        this.commandArguments = new HashMap<>();
    }
    
    public void initialize (IEventBus bus) {
        
        if (!this.commands.isEmpty()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        }
        
        if (!this.commandArguments.isEmpty()) {
            
            bus.addListener(this::registerCommandArguments);
        }
    }
    
    /**
     * @deprecated The static instantiation of commands seems to make some mixin mods very
     *             unhappy. As a result this approach to registering commands has been
     *             deprecated and will be removed in future updates.
     */
    @Deprecated
    public LiteralArgumentBuilder<CommandSource> registerCommand (LiteralArgumentBuilder<CommandSource> command) {
        
        this.logger.error("Registered a command using a deprecated method. This will cause errors! {]", command);
        this.commands.add(command);
        return command;
    }
    
    private void registerCommands (RegisterCommandsEvent event) {
        
        if (!this.commands.isEmpty()) {
            
            this.logger.info("Registering {} commands. This mod is using a deprecated system!", this.commands.size());
            final CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
            
            for (final LiteralArgumentBuilder<CommandSource> command : this.commands) {
                
                dispatcher.register(command);
            }
        }
    }
    
    public List<LiteralArgumentBuilder<CommandSource>> getCommands () {
        
        return ImmutableList.copyOf(this.commands);
    }
    
    public <T extends ArgumentType<?>> void registerCommandArgument (String name, Class<T> clazz, IArgumentSerializer<T> serializer) {
        
        this.commandArguments.put(name, new Tuple<>(clazz, serializer));
    }
    
    private void registerCommandArguments (FMLCommonSetupEvent event) {
        
        this.logger.info("Registering {} command argument types.", this.commandArguments.size());
        
        for (final Entry<String, Tuple<Class, IArgumentSerializer>> entry : this.commandArguments.entrySet()) {
            
            ArgumentTypes.register(entry.getKey(), entry.getValue().getA(), entry.getValue().getB());
        }
    }
}