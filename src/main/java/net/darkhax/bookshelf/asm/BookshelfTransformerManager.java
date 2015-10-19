package net.darkhax.bookshelf.asm;

import net.darkhax.bookshelf.asm.transformers.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.entity.passive.EntityHorse"))
            return EntityHorseTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.block.BlockPistonBase"))
            return BlockPistonTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.inventory.ContainerEnchantment"))
            return ContainerEnchantmentTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.item.Item"))
            return ItemTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.tileentity.MobSpawnerBaseLogic"))
            return MobSpawnerBasicLogicTransformer.transform(name, transformedName, classBytes);
            
        return classBytes;
    }
}
