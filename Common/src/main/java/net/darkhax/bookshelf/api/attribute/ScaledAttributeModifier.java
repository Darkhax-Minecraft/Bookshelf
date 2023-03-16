package net.darkhax.bookshelf.api.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class ScaledAttributeModifier {

    private final UUID id;
    private final String name;
    private final Function<Integer, Float> scaleFunc;
    private final AttributeModifier.Operation operation;
    private final Map<Integer, AttributeModifier> scaledModifiers = new HashMap<>();

    public ScaledAttributeModifier(String id, String name, AttributeModifier.Operation operation, Function<Integer, Float> scaleFunc) {

        this(UUID.fromString(id), name, operation, scaleFunc);
    }

    public ScaledAttributeModifier(UUID id, String name, AttributeModifier.Operation operation, Function<Integer, Float> scaleFunc) {

        this.id = id;
        this.name = name;
        this.scaleFunc = scaleFunc;
        this.operation = operation;
    }

    public AttributeModifier get(int level) {

        return scaledModifiers.computeIfAbsent(level, lvl -> new AttributeModifier(id, name, scaleFunc.apply(lvl), operation));
    }
}