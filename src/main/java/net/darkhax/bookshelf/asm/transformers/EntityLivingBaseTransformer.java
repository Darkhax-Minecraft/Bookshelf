package net.darkhax.bookshelf.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;

public class EntityLivingBaseTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
        
        if (ASMHelper.hasClassMethodName(itemClass, "curePotionEffects"))
            transformcurePotionEffects(ASMHelper.getMethodFromClass(itemClass, "curePotionEffects", "(Lnet/minecraft/item/ItemStack;)V"));
            
        if (ASMHelper.hasClassMethodName(itemClass, "onNewPotionEffect"))
            transform(ASMHelper.getMethodFromClass(itemClass, "onNewPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V"), "onNewPotionEffect");
            
        if (ASMHelper.hasClassMethodName(itemClass, "onChangedPotionEffect"))
            transform(ASMHelper.getMethodFromClass(itemClass, "onChangedPotionEffect", "(Lnet/minecraft/potion/PotionEffect;Z)V"), "onChangedPotionEffect");
            
        if (ASMHelper.hasClassMethodName(itemClass, "onFinishedPotionEffect"))
            transform(ASMHelper.getMethodFromClass(itemClass, "onFinishedPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V"), "onFinishedPotionEffect");
            
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
    
    /**
     * Transforms the potion event methods in EntityLivingBase to trigger the potion status
     * events.
     *
     * @param method: A MethodNode which represents the curePotionEffects method.
     * @param hook: The hook method in the BookshelfHooks class.
     */
    private static void transform (MethodNode method, String hook) {
        
        InsnList newInsns = new InsnList();
        
        newInsns.add(new VarInsnNode(ALOAD, 1));
        newInsns.add(new VarInsnNode(ALOAD, 0));
        newInsns.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", hook, "(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/entity/EntityLivingBase;)V", false));
        
        method.instructions.insertBefore(method.instructions.getFirst(), newInsns);
    }
}