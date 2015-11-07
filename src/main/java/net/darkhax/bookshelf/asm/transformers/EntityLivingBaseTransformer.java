package net.darkhax.bookshelf.asm.transformers;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;

public class EntityLivingBaseTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
        
        if (ASMHelper.hasClassMethodName(itemClass, "curePotionEffects"))
            transformcurePotionEffects(ASMHelper.getMethodFromClass(itemClass, "curePotionEffects", "(Lnet/minecraft/item/ItemStack;)V"));
            
        return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
    }
    
    /**
     * Transforms the curePotionEffects method in EntityLivingBase to trigger the
     * PotionCuredEvent.
     * 
     * @param method: A MethodNode which represents the curePotionEffects method.
     */
    private static void transformcurePotionEffects (MethodNode method) {
        
        InsnList newInsns = new InsnList();
        
        LabelNode start = new LabelNode();
        newInsns.add(start);
        newInsns.add(new VarInsnNode(ALOAD, 0));
        newInsns.add(new VarInsnNode(ALOAD, 1));
        newInsns.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onPotionsCured", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Z", false));
        
        LabelNode procede = new LabelNode();
        newInsns.add(new JumpInsnNode(IFEQ, procede));
        
        LabelNode exit = new LabelNode();
        newInsns.add(exit);
        newInsns.add(new InsnNode(RETURN));
        newInsns.add(procede);
        
        method.instructions.insertBefore(method.instructions.getFirst(), newInsns);
    }
}