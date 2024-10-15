package net.darkhax.bookshelf.common.mixin.patch.registries;

import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterPacket;
import net.darkhax.bookshelf.common.api.registry.register.RegisterParticleTypes;
import net.darkhax.bookshelf.common.api.registry.register.RegisterRecipeType;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.recipe.RecipeTypeImpl;
import net.darkhax.bookshelf.common.mixin.access.particles.AccessSimpleParticleType;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class MixinBuiltInRegistries {

    @Inject(method = "bootStrap()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/registries/BuiltInRegistries;freeze()V", ordinal = 0))
    private static void bootstrap(CallbackInfo callback) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerMobEffects(new Register<>(owner, (id, effect) -> Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id, effect)));
            provider.registerBlocks(new Register<>(owner, (id, block) -> Blocks.register(ResourceKey.create(BuiltInRegistries.BLOCK.key(), id), block)));
            provider.registerEntities(new Register<>(owner, (id, builder) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id.toString()))));
            provider.registerItems(new Register<>(owner, Items::registerItem));
            provider.registerBlockEntities(new Register<>(owner, (id, builder) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, id.toString())))));
            provider.registerMenus(new MenuRegister(owner));
            provider.registerRecipeTypes(new RegisterRecipeType(owner, id -> Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeTypeImpl<>(id))));
            provider.registerRecipeSerializers(new Register<>(owner, (id, serializer) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer)));
            provider.registerAttributes(new Register<>(owner, (id, attribute) -> Registry.register(BuiltInRegistries.ATTRIBUTE, id, attribute)));
            provider.registerLootConditions(new Register<>(owner, (id, codec) -> Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, id, new LootItemConditionType(codec))));
            provider.registerLootFunctions(new Register<>(owner, (id, codec) -> Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, id, new LootItemFunctionType<>(codec))));
            provider.registerLoadConditions(new Register<>(owner, LoadConditions::register));
            provider.registerPackets(new RegisterPacket(owner, Services.NETWORK::register));
            provider.registerParticleTypes(new RegisterParticleTypes(owner, (id, overrideLimit) -> {
                final SimpleParticleType particleType = AccessSimpleParticleType.init(overrideLimit);
                Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particleType);
                return () -> particleType;
            }));
        });
    }
}