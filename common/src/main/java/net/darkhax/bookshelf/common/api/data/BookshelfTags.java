package net.darkhax.bookshelf.common.api.data;

import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BookshelfTags {
    public static TagKey<DamageType> FAKE_PLAYER_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fake_player"));
}