package net.darkhax.bookshelf.potion;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;

public class ModPotion extends Potion {

    public ModPotion (EffectInstance... effects) {

        super(null, effects);
    }

    @Override
    public String getName (String prefix) {

        // Vanilla just appends the path, but for mod compatibility including the namespace is
        // better.
        return prefix + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath();
    }
}