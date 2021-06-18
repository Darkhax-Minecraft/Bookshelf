package net.darkhax.bookshelf.registry;

import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.utils.ItemHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

/**
 * The registry helper handles registration of various game objects.
 */
public class RegistryHelper {
    
    public final String ownerId;
    private boolean initialized = false;
    
    public final BasicRegistry<Block> blocks;
    public final BasicRegistryItem items;
    public final BasicRegistry<BlockEntityType<?>> blockEntities;
    public final BasicRegistry<RecipeType<?>> recipeTypes;
    public final BasicRegistry<RecipeSerializer<?>> recipeSerializers;
    public final BasicRegistry<EntityType<?>> entityTypes;
    public final BasicRegistry<PaintingMotive> paintings;
    public final BasicRegistry<Potion> potions;
    public final BasicRegistry<Enchantment> enchantments;
    
    public RegistryHelper(String ownerId, Logger logger) {
        
        this.ownerId = ownerId;
        
        this.blocks = new BasicRegistry<>(logger, ownerId, Registry.BLOCK);
        this.items = new BasicRegistryItem(logger, ownerId);
        this.blockEntities = new BasicRegistry<>(logger, ownerId, Registry.BLOCK_ENTITY_TYPE);
        this.recipeTypes = new BasicRegistry<>(logger, ownerId, Registry.RECIPE_TYPE);
        this.recipeSerializers = new BasicRegistry<>(logger, ownerId, Registry.RECIPE_SERIALIZER);
        this.entityTypes = new BasicRegistry<>(logger, ownerId, Registry.ENTITY_TYPE);
        this.paintings = new BasicRegistry<>(logger, ownerId, Registry.PAINTING_MOTIVE);
        this.potions = new BasicRegistry<>(logger, ownerId, Registry.POTION);
        this.enchantments = new BasicRegistry<>(logger, ownerId, Registry.ENCHANTMENT);
    }
    
    public RegistryHelper withItemGroup (Supplier<Item> icon) {
        
        final Lazy<ItemStack> iconGetter = new Lazy<>( () -> new ItemStack(icon.get()));
        return this.withItemGroup(FabricItemGroupBuilder.build(new Identifier(this.ownerId, "tab"), iconGetter::get));
    }
    
    public RegistryHelper withItemGroup (ItemGroup group) {
        
        this.items.addRegisterListener( (reg, item) -> ItemHelper.setItemGroup(item, group));
        return this;
    }
    
    public void init () {
        
        if (!this.initialized) {
            
            this.blocks.apply();
            this.items.apply();
            this.blockEntities.apply();
            this.recipeTypes.apply();
            this.recipeSerializers.apply();
            this.entityTypes.apply();
            this.paintings.apply();
            this.potions.apply();
            this.enchantments.apply();
            
            this.initialized = true;
        }
        
        else {
            
            throw new IllegalStateException("RegistryHelper for mod " + this.ownerId + " has already been initialized.");
        }
    }
}