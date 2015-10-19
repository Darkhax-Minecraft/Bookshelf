package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.updatePistonState;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class BlockPistonTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] bytes) {
        
        if (transformedName.equals("net.minecraft.block.BlockPistonBase")) {
            
            ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
            transformUpdatePistonStateExtend(ASMHelper.getMethodFromClass(itemClass, updatePistonState, "(Lnet/minecraft/world/World;III)V"));
            transformUpdatePistonStateRetract(ASMHelper.getMethodFromClass(itemClass, updatePistonState, "(Lnet/minecraft/world/World;III)V"));
            return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
        }
        
        return bytes;
    }
    
    private void transformUpdatePistonStateExtend (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ILOAD, 7));
        needle.add(new JumpInsnNode(IFEQ, new LabelNode()));
        needle.add(new VarInsnNode(ILOAD, 5));
        needle.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/block/BlockPistonBase", "isExtended", "(I)Z", false));
        needle.add(new JumpInsnNode(IFNE, new LabelNode()));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        LabelNode labelNode = ((JumpInsnNode) pointer).label;
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(ILOAD, 2));
        newInstr.add(new VarInsnNode(ILOAD, 3));
        newInstr.add(new VarInsnNode(ILOAD, 4));
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new VarInsnNode(ILOAD, 5));
        newInstr.add(new VarInsnNode(ILOAD, 6));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/block/BlockPistonBase", "isSticky", "Z"));
        newInstr.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onPistonExtend", "(IIILnet/minecraft/world/World;Lnet/minecraft/block/Block;IIZ)Z", false));
        newInstr.add(new JumpInsnNode(IFEQ, labelNode));
        
        method.instructions.insert(pointer, newInstr);
    }
    
    private void transformUpdatePistonStateRetract (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ILOAD, 7));
        needle.add(new JumpInsnNode(IFNE, new LabelNode()));
        needle.add(new VarInsnNode(ILOAD, 5));
        needle.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/block/BlockPistonBase", "isExtended", "(I)Z", false));
        needle.add(new JumpInsnNode(IFEQ, new LabelNode()));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        LabelNode labelNode = ((JumpInsnNode) pointer).label;
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(ILOAD, 2));
        newInstr.add(new VarInsnNode(ILOAD, 3));
        newInstr.add(new VarInsnNode(ILOAD, 4));
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new VarInsnNode(ILOAD, 5));
        newInstr.add(new VarInsnNode(ILOAD, 6));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/block/BlockPistonBase", "isSticky", "Z"));
        newInstr.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onPistonRetract", "(IIILnet/minecraft/world/World;Lnet/minecraft/block/Block;IIZ)Z", false));
        newInstr.add(new JumpInsnNode(IFEQ, labelNode));
        
        method.instructions.insert(pointer, newInstr);
    }
    
    // TODO Make this work
    private void transformCanPushBlock (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", "obsidian", "Lnet/minecraft/block/Block;"));
        needle.add(new JumpInsnNode(IF_ACMPNE, new LabelNode()));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        LabelNode labelNode = ((JumpInsnNode) pointer.getNext().getNext()).label;
        
        InsnList newInstr = new InsnList();
        newInstr.insert(new VarInsnNode(ALOAD, 0));
        newInstr.insert(new VarInsnNode(ALOAD, 1));
        newInstr.insert(new VarInsnNode(ILOAD, 2));
        newInstr.insert(new VarInsnNode(ILOAD, 3));
        newInstr.insert(new VarInsnNode(ILOAD, 4));
        newInstr.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "canPushBlock", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;III)Z", false));
        newInstr.add(new JumpInsnNode(IFEQ, labelNode));
        newInstr.add(new LabelNode());
        method.instructions.insertBefore(pointer, newInstr);
    }
}