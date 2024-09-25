package net.darkhax.bookshelf.common.api.registry.register;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public interface ArgumentRegister {
    <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void accept(String id, Class<? extends A> argumentClass, ArgumentTypeInfo<A, T> info);
}