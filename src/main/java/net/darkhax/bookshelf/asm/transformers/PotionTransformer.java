package net.darkhax.bookshelf.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.asm.Mappings;

public class PotionTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode potionClass = ASMHelper.createClassFromByteArray(bytes);
        transformConstructor(ASMHelper.getMethodFromClass(potionClass, "<init>", "(IZI)V"));
        return ASMHelper.createByteArrayFromClass(potionClass, ClassWriter.COMPUTE_MAXS);
    }
    
    /**
     * Transforms the Potion class constructor, to do a check to ensure that Potion IDs are not
     * being overlapped.
     * 
     * @param method: A MethodNode that represents the Potion constructor.
     */
    private static void transformConstructor (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new VarInsnNode(ILOAD, 1));
        needle.add(new FieldInsnNode(PUTFIELD, "net/minecraft/potion/Potion", Mappings.potionID, "I"));
        
        InsnList instructions = new InsnList();
        LabelNode start = new LabelNode();
        LabelNode end = new LabelNode();
        instructions.add(start);
        instructions.add(new VarInsnNode(ALOAD, 0));
        instructions.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onPotionConstructed", "(Lnet/minecraft/potion/Potion;)V", false));
        instructions.add(end);
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        method.instructions.insert(pointer, instructions);
    }
}