package net.darkhax.bookshelf.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.asm.Mapping;

public class TransformRenderLiving {
    
    public static final Mapping METHOD_RENDER_LIVING_AT = new Mapping("func_77039_a", "renderLivingAt", "net/minecraft/client/renderer/entity/RenderLivingBase", "(Lnet/minecraft/entity/EntityLivingBase;DDD)V");
    public static final Mapping METHOD_BOOKSHELF_RENDER_LIVING = new Mapping("modifyEntityRender", "net/darkhax/bookshelf/handler/BookshelfHooks", "(Lnet/minecraft/client/renderer/entity/RenderLivingBase;Lnet/minecraft/entity/EntityLivingBase;)V");
    
    public static byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_RENDER_LIVING_AT.getMethodNode(clazz);
        
        final InsnList newInsns = new InsnList();
        final LabelNode start = new LabelNode();
        newInsns.add(start);
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInsns.add(METHOD_BOOKSHELF_RENDER_LIVING.getMethodInsn(Opcodes.INVOKESTATIC, false));
        final LabelNode end = new LabelNode();
        newInsns.add(end);
        
        method.instructions.insertBefore(method.instructions.getFirst(), newInsns);
        
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }
}
