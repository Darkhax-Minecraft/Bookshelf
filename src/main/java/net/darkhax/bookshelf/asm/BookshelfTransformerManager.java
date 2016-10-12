package net.darkhax.bookshelf.asm;

import net.darkhax.bookshelf.asm.transformers.TransformItemRenderer;
import net.darkhax.bookshelf.asm.transformers.TransformerEnchantmentHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.client.renderer.RenderItem"))
            return TransformItemRenderer.transform(name, transformedName, classBytes);
        
        if (transformedName.equals("net.minecraft.enchantment.EnchantmentHelper"))
            return TransformerEnchantmentHelper.transform(name, transformedName, classBytes);
        
        return classBytes;
    }
}