package net.darkhax.bookshelf.api.registry;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import java.util.function.Supplier;

public class CommandArgumentEntry <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> {

    private final Supplier<I> serializer;
    private final Class<A> argClass;

    public CommandArgumentEntry(Supplier<I> serializer, Class<A> argType) {

        this.serializer = serializer;
        this.argClass = argType;
    }

    public Supplier<I> getSerializer() {

        return this.serializer;
    }

    public Class<A> getType() {

        return this.argClass;
    }
}