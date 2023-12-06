package net.darkhax.bookshelf.api.data.conditions.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;

public class OnPlatform implements ILoadCondition {

    public static final Codec<OnPlatform> CODEC = RecordCodecBuilder.create(instance -> instance.group(BookshelfCodecs.STRING.get("platform", OnPlatform::getRequiredPlatform)).apply(instance, OnPlatform::new));
    private final String requiredPlatform;

    private OnPlatform(String requiredPlatform) {

        this.requiredPlatform = requiredPlatform;
    }

    @Override
    public boolean allowLoading() {

        return Services.PLATFORM.getName().equalsIgnoreCase(this.requiredPlatform);
    }

    public String getRequiredPlatform() {

        return this.requiredPlatform;
    }

    @Override
    public LoadConditions.ConditionType getType() {

        return LoadConditions.ON_PLATFORM;
    }
}
