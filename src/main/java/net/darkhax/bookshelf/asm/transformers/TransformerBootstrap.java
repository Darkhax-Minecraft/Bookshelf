/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.asm.Mapping;

public class TransformerBootstrap {
    
    public static final Mapping REGISTER = new Mapping("func_151354_b", "register", "net/minecraft/init/Bootstrap", "()V");
    
    public static byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = REGISTER.getMethodNode(clazz);
        final InsnList n1 = new InsnList();
        
        final LabelNode node = new LabelNode();
        n1.add(node);
        n1.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/darkhax/bookshelf/BookshelfHooks", "onPrePreInit", "()V", false));
        
        final InsnList needle = new InsnList();
        needle.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraftforge/fml/common/registry/GameData", "vanillaSnapshot", "()V", false));
        
        final AbstractInsnNode inb4 = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);
        method.instructions.insert(inb4, n1);
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }
}