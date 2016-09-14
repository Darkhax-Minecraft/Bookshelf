package net.darkhax.bookshelf.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.mapping.Mapping;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    public static final Mapping METHOD_RENDER_ITEM = new Mapping("func_178099_a", "renderItem", "net/minecraft/client/renderer/RenderItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
    public static final Mapping METHOD_IS_BUILT_IN_RENDERER = new Mapping("func_188618_c", "isBuiltInRenderer", "net/minecraft/client/renderer/block/model/IBakedModel", "()Z");
    public static final Mapping METHOD_ON_RENDER_ITEM = new Mapping("onRenderItem", "net/darkhax/bookshelf/handler/BookshelfHooks", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (transformedName.equals("net.minecraft.client.renderer.RenderItem")) {
            
            final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
            this.transformRenderItem(METHOD_RENDER_ITEM.getMethodNode(clazz));
            return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS);
        }
        return classBytes;
    }
    
    private void transformRenderItem (MethodNode method) {
        
        InsnList needle = new InsnList();
        LabelNode l4 = new LabelNode();
        needle.add(l4);
        needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
        needle.add(METHOD_IS_BUILT_IN_RENDERER.getMethodInsn(Opcodes.INVOKEINTERFACE, true));
        LabelNode l5 = new LabelNode();
        needle.add(new JumpInsnNode(Opcodes.IFEQ, l5));
        
        InsnList insertion = new InsnList();
        LabelNode start = new LabelNode();
        insertion.add(start);
        insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insertion.add(new VarInsnNode(Opcodes.ALOAD, 2));
        insertion.add(METHOD_ON_RENDER_ITEM.getMethodInsn(Opcodes.INVOKESTATIC, false));
        
        final AbstractInsnNode pointer = ASMUtils.findFirstNodeFromNeedle(method.instructions, needle);
        
        method.instructions.insertBefore(pointer, insertion);
    }
}