/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.asm;

import net.darkhax.bookshelf.asm.transformers.TransformerBootstrap;
import net.darkhax.bookshelf.asm.transformers.TransformerCrashReport;
import net.darkhax.bookshelf.asm.transformers.TransformerEnchantmentHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if ("net.minecraft.enchantment.EnchantmentHelper".equals(name))
            return TransformerEnchantmentHelper.transform(name, transformedName, classBytes);
        
        else if ("net.minecraft.crash.CrashReport".equals(name))
            return TransformerCrashReport.transform(name, transformedName, classBytes);
        
        else if ("net.minecraft.init.Bootstrap".equals(name))
            return TransformerBootstrap.transform(name, transformedName, classBytes);
        
        return classBytes;
    }
}