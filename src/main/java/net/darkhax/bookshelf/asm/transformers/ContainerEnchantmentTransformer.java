package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.buildEnchantmentList;
import static net.darkhax.bookshelf.asm.Mappings.enchantItem;
import static net.darkhax.bookshelf.asm.Mappings.enchantLevels;
import static net.darkhax.bookshelf.asm.Mappings.rand;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class ContainerEnchantmentTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] bytes) {
        
        if (transformedName.equals("net.minecraft.inventory.ContainerEnchantment")) {
            
            ClassNode itemStackClass = ASMHelper.createClassFromByteArray(bytes);
            transformEnchantItem(ASMHelper.getMethodFromClass(itemStackClass, enchantItem, "(Lnet/minecraft/entity/player/EntityPlayer;I)Z"));
            return ASMHelper.createByteArrayFromClass(itemStackClass, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }
        
        return bytes;
    }
    
    private static void transformEnchantItem (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/inventory/ContainerEnchantment", rand, "Ljava/util/Random;"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 3));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/inventory/ContainerEnchantment", enchantLevels, "[I"));
        needle.add(new VarInsnNode(Opcodes.ILOAD, 2));
        needle.add(new InsnNode(Opcodes.IALOAD));
        needle.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/enchantment/EnchantmentHelper", buildEnchantmentList, "(Ljava/util/Random;Lnet/minecraft/item/ItemStack;I)Ljava/util/List;", false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 4));
        
        InsnList newInsns = new InsnList();
        LabelNode start = new LabelNode();
        LabelNode exit = new LabelNode();
        
        newInsns.add(start);
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 3));
        newInsns.add(new VarInsnNode(Opcodes.ILOAD, 2));
        newInsns.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInsns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/darkhax/bookshelf/util/Utilities", "onItemEnchanted", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;ILjava/util/List;)Ljava/util/List;", false));
        newInsns.add(new VarInsnNode(Opcodes.ASTORE, 4));
        newInsns.add(exit);
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        if (pointer != null)
            method.instructions.insert(pointer, newInsns);
    }
}