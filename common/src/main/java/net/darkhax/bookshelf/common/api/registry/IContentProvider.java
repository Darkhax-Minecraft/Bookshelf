package net.darkhax.bookshelf.common.api.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.registry.register.ArgumentRegister;
import net.darkhax.bookshelf.common.api.registry.register.ItemComponentRegister;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterPacket;
import net.darkhax.bookshelf.common.api.registry.register.RegisterPotPatterns;
import net.darkhax.bookshelf.common.api.registry.register.RegisterRecipeType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface IContentProvider {

    String contentNamespace();

    default void registerMobEffects(Register<MobEffect> registry) {
    }

    default void registerBlocks(Register<Block> registry) {
    }

    default void registerEntities(Register<EntityType.Builder<?>> registry) {
    }

    default void registerItems(Register<Item> registry) {
    }

    default void registerBlockEntities(Register<BlockEntityType.Builder<?>> registry) {
    }

    default void registerRecipeTypes(RegisterRecipeType registry) {
    }

    default void registerRecipeSerializers(Register<RecipeSerializer<?>> registry) {
    }

    default void registerAttributes(Register<Attribute> registry) {
    }

    default void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
    }

    default void registerCommandArguments(ArgumentRegister registry) {
    }

    default void registerItemComponents(ItemComponentRegister registry) {
    }

    default void registerLoadConditions(Register<MapCodec<? extends ILoadCondition>> registry) {
    }

    default void registerPotPatterns(RegisterPotPatterns registry) {
    }

    default void registerMenus(MenuRegister registry) {
    }

    default void registerLootConditions(Register<MapCodec<? extends LootItemCondition>> registry) {
    }

    default void registerLootFunctions(Register<MapCodec<? extends LootItemFunction>> registry) {
    }

    default void registerPackets(RegisterPacket registry) {
    }
}