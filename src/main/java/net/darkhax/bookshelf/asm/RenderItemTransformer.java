package net.darkhax.bookshelf.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FCONST_1;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FMUL;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class RenderItemTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] bytes) {
        
        if (transformedName.equals("net.minecraft.client.renderer.entity.RenderItem"))
            return transformItem(bytes);
            
        return bytes;
    }
    
    private static byte[] transformItem (byte[] bytes) {
        
        ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
        transformRenderDroppedItem(ASMHelper.getMethodFromClass(itemClass, "renderDroppedItem", "(Lnet/minecraft/entity/item/EntityItem;Lnet/minecraft/util/IIcon;IFFFFI)V"));
        return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
    }
    
    private static void transformRenderDroppedItem (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new LdcInsnNode(new Float("0.5")));
        needle.add(new VarInsnNode(FLOAD, 23));
        needle.add(new InsnNode(FMUL));
        needle.add(new LdcInsnNode(new Float("0.25")));
        needle.add(new VarInsnNode(FLOAD, 23));
        needle.add(new InsnNode(FMUL));
        needle.add(new LdcInsnNode(new Float("0.8")));
        needle.add(new VarInsnNode(FLOAD, 23));
        needle.add(new InsnNode(FMUL));
        needle.add(new InsnNode(FCONST_1));
        needle.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V", false));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        InsnList i = new InsnList();
        LabelNode start = new LabelNode();
        LabelNode exit = new LabelNode();
        i.add(start);
        i.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", hasTagCompound, "()Z", false));
        i.add(new JumpInsnNode(IFEQ, exit));
        i.add(new VarInsnNode(ALOAD, 19));
        i.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getTagCompound, "()Lnet/minecraft/nbt/NBTTagCompound;", false));
        i.add(new LdcInsnNode("bookshelfEnchColor"));
        i.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", hasKey, "(Ljava/lang/String;)Z", false));
        i.add(new JumpInsnNode(IFEQ, exit));
        
        LabelNode l0 = new LabelNode();
        i.add(l0);
        i.add(new VarInsnNode(ALOAD, 19));
        i.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getTagCompound, "()Lnet/minecraft/nbt/NBTTagCompound;", false));
        i.add(new LdcInsnNode("bookshelfEnchColor"));
        i.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", getInteger, "(Ljava/lang/String;)I", false));
        i.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/util/Utilities", "renderColor", "(I)V", false));
        i.add(exit);
        
        // method.instructions.insert(pointer, i);
    }
    
    private static String getInteger = ASMHelper.getAppropriateMapping("getInteger", "func_74762_e");
    private static String hasKey = ASMHelper.getAppropriateMapping("hasKey", "func_74764_b");
    private static String getTagCompound = ASMHelper.getAppropriateMapping("getTagCompound", "func_77978_p");
    private static String hasTagCompound = ASMHelper.getAppropriateMapping("hasTagCompound", "func_77942_o");
}