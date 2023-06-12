package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.commands.BookshelfCommands;
import net.darkhax.bookshelf.impl.commands.args.FontArgument;
import net.darkhax.bookshelf.impl.commands.args.HandArgument;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionAnd;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionModLoaded;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionNot;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionOr;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionPlatform;
import net.darkhax.bookshelf.impl.data.conditions.LoadConditionRegistry;
import net.darkhax.bookshelf.impl.data.recipes.crafting.ShapedDurabilityRecipe;
import net.darkhax.bookshelf.impl.data.recipes.crafting.ShapelessDurabilityRecipe;
import net.minecraft.resources.ResourceLocation;

public class BookshelfContentProvider extends RegistryDataProvider {

    public BookshelfContentProvider() {

        super(Constants.MOD_ID);

        // Command Argument Types
        this.commandArguments.add(FontArgument.class, () -> FontArgument.FONT_SERIALIZER, "font");
        this.commandArguments.add(HandArgument.class, () -> HandArgument.SERIALIZER, "item_output");

        // JSON Load Conditions
        this.registerLoadCondition("and", new LoadConditionAnd());
        this.registerLoadCondition("or", new LoadConditionOr());
        this.registerLoadCondition("not", new LoadConditionNot());
        this.registerLoadCondition("on_platform", new LoadConditionPlatform());
        this.registerLoadCondition("mod_loaded", new LoadConditionModLoaded());
        this.registerLoadCondition("block_exists", LoadConditionRegistry.BLOCK);
        this.registerLoadCondition("item_exists", LoadConditionRegistry.ITEM);
        this.registerLoadCondition("enchantment_exists", LoadConditionRegistry.ENCHANTMENT);
        this.registerLoadCondition("painting_exists", LoadConditionRegistry.PAINTING);
        this.registerLoadCondition("mob_effect_exists", LoadConditionRegistry.MOB_EFFECT);
        this.registerLoadCondition("potion_exists", LoadConditionRegistry.POTION);
        this.registerLoadCondition("attribute_exists", LoadConditionRegistry.ATTRIBUTE);
        this.registerLoadCondition("entity_exists", LoadConditionRegistry.ENTITY_TYPE);
        this.registerLoadCondition("block_entity_exists", LoadConditionRegistry.BLOCK_ENTITY_TYPE);

        // Command Builders
        this.commands.add(BookshelfCommands::new, "commands");

        // Recipe Serializers
        this.recipeSerializers.add(() -> ShapedDurabilityRecipe.SERIALIZER, "shaped_durability");
        this.recipeSerializers.add(() -> ShapelessDurabilityRecipe.SERIALIZER, "shapeless_durability");
    }

    private void registerLoadCondition(String id, ILoadConditionSerializer<?> serializer) {

        LoadConditions.register(new ResourceLocation(Constants.MOD_ID, id), serializer);
    }
}