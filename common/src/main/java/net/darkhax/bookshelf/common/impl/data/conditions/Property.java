package net.darkhax.bookshelf.common.impl.data.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.conditions.ConditionType;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class Property implements ILoadCondition {

    public static final Identifier TYPE_ID = BookshelfMod.id("property");
    public static final CachedSupplier<ConditionType> TYPE = CachedSupplier.cache(() -> LoadConditions.getType(TYPE_ID));
    public static final MapCodec<Property> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(MapCodecs.RESOURCE_LOCATION.get("property", p -> p.property)).apply(instance, Property::new));

    private static final Map<Identifier, BooleanSupplier> PROPERTIES = new HashMap<>();

    private final Identifier property;

    private Property(Identifier property) {
        this.property = property;
    }

    @Override
    public boolean allowLoading() {
        final BooleanSupplier supplier = PROPERTIES.get(this.property);
        return supplier != null && supplier.getAsBoolean();
    }

    @Override
    public ConditionType getType() {
        return TYPE.get();
    }

    public static void define(Identifier property, BooleanSupplier supplier) {
        PROPERTIES.put(property, supplier);
    }
}