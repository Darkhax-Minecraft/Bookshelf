package net.darkhax.bookshelf.asm;

import net.darkhax.bookshelf.asm.transformers.TransformItemRenderer;
import net.darkhax.bookshelf.asm.transformers.TransformRenderLiving;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.client.renderer.RenderItem"))
            return TransformItemRenderer.transform(name, transformedName, classBytes);
        
        else if (transformedName.equals("net.minecraft.client.renderer.entity.RenderLivingBase"))
            return TransformRenderLiving.transform(name, transformedName, classBytes);
        
        return classBytes;
    }
}