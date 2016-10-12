package net.darkhax.bookshelf.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.asm.Mapping;

public class TransformerEnchantmentHelper {
    
    public static final Mapping METHOD_GET_ENCH_LEVEL = new Mapping("func_185284_a", "getMaxEnchantmentLevel", "net/minecraft/enchantment/EnchantmentHelper", "(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/EntityLivingBase;)I");
    public static final Mapping METHOD_INIT_EVENT = new Mapping("<init>", "net/darkhax/bookshelf/events/EnchantmentModifierEvent", "(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/EntityLivingBase;)V");
    public static final Mapping METHOD_POST = new Mapping("post", "net/minecraftforge/fml/common/eventhandler/EventBus", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z");
    public static final Mapping METHOD_CANCELED = new Mapping("isCanceled", "net/darkhax/bookshelf/events/EnchantmentModifierEvent", "()Z");
    public static final Mapping METHOD_GET_LEVELS = new Mapping("getLevels", "net/darkhax/bookshelf/events/EnchantmentModifierEvent", "()I");
    public static final Mapping FIELD_EVENT_BUS = new Mapping("EVENT_BUS", "net/minecraftforge/common/MinecraftForge", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;");
    
    public static byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_GET_ENCH_LEVEL.getMethodNode(clazz);
        
        final InsnList n1 = new InsnList();
        final LabelNode start = new LabelNode();
        n1.add(start);
        n1.add(new TypeInsnNode(Opcodes.NEW, "net/darkhax/bookshelf/events/EnchantmentModifierEvent"));
        n1.add(new InsnNode(Opcodes.DUP));
        n1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        n1.add(new VarInsnNode(Opcodes.ALOAD, 1));
        n1.add(METHOD_INIT_EVENT.getMethodInsn(Opcodes.INVOKESPECIAL, false));
        n1.add(new VarInsnNode(Opcodes.ASTORE, 2));
        final LabelNode l1 = new LabelNode();
        n1.add(l1);
        n1.add(FIELD_EVENT_BUS.getFieldNode(Opcodes.GETSTATIC));
        n1.add(new VarInsnNode(Opcodes.ALOAD, 2));
        n1.add(METHOD_POST.getMethodInsn(Opcodes.INVOKEVIRTUAL, false));
        n1.add(new InsnNode(Opcodes.POP));
        final LabelNode l2 = new LabelNode();
        n1.add(l2);
        n1.add(new VarInsnNode(Opcodes.ALOAD, 2));
        n1.add(METHOD_CANCELED.getMethodInsn(Opcodes.INVOKEVIRTUAL, false));
        final LabelNode vanillaLogic = new LabelNode();
        n1.add(new JumpInsnNode(Opcodes.IFEQ, vanillaLogic));
        final LabelNode l4 = new LabelNode();
        n1.add(l4);
        n1.add(new VarInsnNode(Opcodes.ALOAD, 2));
        n1.add(METHOD_GET_LEVELS.getMethodInsn(Opcodes.INVOKEVIRTUAL, false));
        n1.add(new InsnNode(Opcodes.IRETURN));
        n1.add(vanillaLogic);
        method.instructions.insertBefore(method.instructions.getFirst(), n1);
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }
}
