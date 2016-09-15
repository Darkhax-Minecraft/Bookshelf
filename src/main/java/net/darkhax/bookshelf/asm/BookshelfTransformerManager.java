package net.darkhax.bookshelf.asm;

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

import net.darkhax.bookshelf.asm.mapping.Mapping;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    public static final Mapping METHOD_RENDER_ITEM = new Mapping("func_180454_a", "renderItem", "net/minecraft/client/renderer/RenderItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
    public static final Mapping METHOD_BOOKSHELF_RENDER_ITEM = new Mapping("renderItem", "net/darkhax/bookshelf/handler/BookshelfHooks", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)Z");
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.client.renderer.RenderItem")) {
            
            final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
            final MethodNode node = METHOD_RENDER_ITEM.getMethodNode(clazz);
            this.insertRenderHook(node);
            return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }
        return classBytes;
    }
    
    private void insertRenderHook (MethodNode method) {
        
        final InsnList i = new InsnList();
        final LabelNode start = new LabelNode();
        i.add(start);
        i.add(new VarInsnNode(Opcodes.ALOAD, 0));
        i.add(new VarInsnNode(Opcodes.ALOAD, 1));
        i.add(new VarInsnNode(Opcodes.ALOAD, 2));
        i.add(METHOD_BOOKSHELF_RENDER_ITEM.getMethodInsn(Opcodes.INVOKESTATIC, false));
        final LabelNode check = new LabelNode();
        i.add(check);
        final LabelNode vanilla = new LabelNode();
        final LabelNode exit = new LabelNode();
        i.add(new JumpInsnNode(Opcodes.IFEQ, vanilla));
        i.add(exit);
        i.add(new InsnNode(Opcodes.RETURN));
        i.add(vanilla);
        i.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        
        method.instructions.insertBefore(method.instructions.getFirst(), i);
    }
}