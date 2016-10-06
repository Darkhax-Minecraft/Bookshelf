package net.darkhax.bookshelf.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.asm.Mapping;

public class TransformItemRenderer {
    
    public static final Mapping METHOD_RENDER_ITEM = new Mapping("func_180454_a", "renderItem", "net/minecraft/client/renderer/RenderItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
    public static final Mapping METHOD_BOOKSHELF_RENDER_ITEM = new Mapping("renderItem", "net/darkhax/bookshelf/handler/BookshelfHooks", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)Z");
    
    public static byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_RENDER_ITEM.getMethodNode(clazz);
        
        final InsnList newInsns = new InsnList();
        final LabelNode start = new LabelNode();
        newInsns.add(start);
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInsns.add(METHOD_BOOKSHELF_RENDER_ITEM.getMethodInsn(Opcodes.INVOKESTATIC, false));
        final LabelNode check = new LabelNode();
        newInsns.add(check);
        final LabelNode vanilla = new LabelNode();
        final LabelNode exit = new LabelNode();
        newInsns.add(new JumpInsnNode(Opcodes.IFEQ, vanilla));
        newInsns.add(exit);
        newInsns.add(new InsnNode(Opcodes.RETURN));
        newInsns.add(vanilla);
        newInsns.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        method.instructions.insertBefore(method.instructions.getFirst(), newInsns);
        
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }
}
