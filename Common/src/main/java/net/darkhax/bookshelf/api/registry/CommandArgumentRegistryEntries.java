package net.darkhax.bookshelf.api.registry;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class CommandArgumentRegistryEntries extends RegistryEntries<CommandArgumentEntry<?, ?, ?>> {

    public CommandArgumentRegistryEntries(Supplier<String> idProvider, ResourceKey<?> registryKey) {

        super(idProvider, registryKey);
    }

    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> void add(Class<A> typeClass, Supplier<I> typeSerializer, String id) {

        this.add(() -> new CommandArgumentEntry<>(typeSerializer, typeClass), id);
    }
}