package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.updateSpawner;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;

public class MobSpawnerBasicLogicTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
        transformMobSpawnerBaseLogic(ASMHelper.getMethodFromClass(itemClass, updateSpawner, "()V"));
        return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
    }
    
    // TODO Docs
    private static void transformMobSpawnerBaseLogic (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 11));
        needle.add(new JumpInsnNode(IFNULL, new LabelNode()));
        needle.add(new VarInsnNode(ALOAD, 11));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/EntityLiving", "getCanSpawnHere", "()Z", false));
        needle.add(new JumpInsnNode(IFEQ, new LabelNode()));
        
        LabelNode jump = new LabelNode();
        InsnList newInstr = new InsnList();
        newInstr.add(new JumpInsnNode(GOTO, jump));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        method.instructions.insertBefore(pointer, newInstr);
        
        AbstractInsnNode pointerE = pointer;
        for (int i = 0; i < needle.size() - 1; i++) {
            pointerE = pointerE.getNext();
        }
        
        JumpInsnNode ifeq = (JumpInsnNode) pointerE;
        newInstr.clear();
        newInstr.add(jump);
        newInstr.add(new VarInsnNode(ALOAD, 5));
        newInstr.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "doEntitySpawnSpawnerCheck", "(Lnet/minecraft/entity/Entity;)Z", false));
        newInstr.add(new JumpInsnNode(IFEQ, ifeq.label));
        
        method.instructions.insert(pointerE, newInstr);
    }
}