package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.*;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class ItemTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] bytes) {
        
        if (transformedName.equals("net.minecraft.item.Item")) {
            
            ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
            
            if (ASMHelper.hasClassMethodName(itemClass, getColorFromItemStack))
                transformGetColorFromItemStack(ASMHelper.getMethodFromClass(itemClass, getColorFromItemStack, "(Lnet/minecraft/item/ItemStack;I)I"));
                
            return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
        }
        
        return bytes;
    }
    
    /**
     * Transforms the getColorFromItemStack method within the Item class. This change adds in a
     * simple check to see if the Item has a color nbt tag. If it does, the color will be taken
     * from NBT instead.
     * 
     * @param method: Instance of the getColorFromItemStack MethodNode.
     */
    private static void transformGetColorFromItemStack (MethodNode method) {
        
        InsnList insns = new InsnList();
        
        LabelNode l0 = new LabelNode();
        insns.add(l0);
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", hasTagCompound, "()Z", false));
        
        LabelNode l1 = new LabelNode();
        insns.add(new JumpInsnNode(IFEQ, l1));
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getTagCompound, "()Lnet/minecraft/nbt/NBTTagCompound;", false));
        insns.add(new LdcInsnNode("bookshelfColor"));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", hasKey, "(Ljava/lang/String;)Z", false));
        insns.add(new JumpInsnNode(IFEQ, l1));
        
        LabelNode l2 = new LabelNode();
        insns.add(l2);
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getTagCompound, "()Lnet/minecraft/nbt/NBTTagCompound;", false));
        insns.add(new LdcInsnNode("bookshelfColor"));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", getInteger, "(Ljava/lang/String;)I", false));
        insns.add(new InsnNode(IRETURN));
        insns.add(l1);
        
        method.instructions.insert(method.instructions.get(0), insns);
    }
}