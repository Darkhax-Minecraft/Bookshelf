package net.darkhax.bookshelf.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotion extends Potion {

    public ModPotion (MobEffectInstance... effects) {

        super(null, effects);
    }

    @Override
    public String getName (String prefix) {

        // Vanilla just appends the path, but for mod compatibility including the namespace is
        // better.
        return prefix + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath();
    }
}