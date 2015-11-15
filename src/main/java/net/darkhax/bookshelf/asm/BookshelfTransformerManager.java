package net.darkhax.bookshelf.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import net.darkhax.bookshelf.asm.transformers.*;

public class BookshelfTransformerManager implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.entity.passive.EntityHorse"))
            return EntityHorseTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.inventory.ContainerEnchantment"))
            return ContainerEnchantmentTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.item.Item") || transformedName.equals("net.minecraft.item.ItemArmor"))
            return ItemColorTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.enchantment.EnchantmentHelper"))
            return EnchantmentHelperTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.creativetab.CreativeTabs"))
            return CreativeTabsTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.entity.EntityLivingBase"))
            return EntityLivingBaseTransformer.transform(name, transformedName, classBytes);
            
        if (transformedName.equals("net.minecraft.potion.Potion"))
            return PotionTransformer.transform(name, transformedName, classBytes);
            
        return classBytes;
    }
}
