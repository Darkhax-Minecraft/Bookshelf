package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.getEfficiencyModifier;
import static net.darkhax.bookshelf.asm.Mappings.getFireAspectModifier;
import static net.darkhax.bookshelf.asm.Mappings.getFortuneModifier;
import static net.darkhax.bookshelf.asm.Mappings.getKnockbackModifier;
import static net.darkhax.bookshelf.asm.Mappings.getLootingModifier;
import static net.darkhax.bookshelf.asm.Mappings.getLuckOfSeaModifier;
import static net.darkhax.bookshelf.asm.Mappings.getLureModifier;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_APPEND;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMUtils;

public class EnchantmentHelperTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode enchantmentHelperClass = ASMUtils.createClassFromByteArray(bytes);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getKnockbackModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "KnockbackEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getFireAspectModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "FireAspectEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getEfficiencyModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "EfficiencyEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getFortuneModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "FortuneEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getLuckOfSeaModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "LuckOfSeaEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getLureModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "LureEvent", false);
        transformGetLootingModifier(ASMUtils.getMethodFromClass(enchantmentHelperClass, getLootingModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"), "LootingEvent", false);
        return ASMUtils.createByteArrayFromClass(enchantmentHelperClass, ClassWriter.COMPUTE_MAXS);
    }
    
    /**
     * Transforms the getLootingModifier method, to trigger the LootingEvent. This allows for
     * new behavior to happen, when looting modifiers are calculated.
     * 
     * @param method: A MethodNode that represents the getLootingModifier method.
     */
    private static void transformGetLootingModifier (MethodNode method, String type, boolean isStupid) {
        
        InsnList instructions = new InsnList();
        
        LabelNode start = new LabelNode();
        instructions.add(start);
        instructions.add(new TypeInsnNode(NEW, "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type));
        instructions.add(new InsnNode(DUP));
        instructions.add(new VarInsnNode(ALOAD, 0));
        instructions.add(new MethodInsnNode(INVOKESPECIAL, "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type, "<init>", "(Lnet/minecraft/entity/EntityLivingBase;)V", false));
        instructions.add(new VarInsnNode(ASTORE, 1));
        
        LabelNode bus = new LabelNode();
        instructions.add(bus);
        instructions.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        instructions.add(new InsnNode(POP));
        
        LabelNode l1 = new LabelNode();
        instructions.add(l1);
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type, "getResult", "()Lnet/minecraftforge/fml/common/eventhandler/Event$Result;", false));
        instructions.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/fml/common/eventhandler/Event$Result", "ALLOW", "Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"));
        
        LabelNode l2 = new LabelNode();
        instructions.add(new JumpInsnNode(IF_ACMPNE, l2));
        
        LabelNode l3 = new LabelNode();
        instructions.add(l3);
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new FieldInsnNode(GETFIELD, "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type, "levels", "I"));
        instructions.add(new InsnNode(IRETURN));
        instructions.add(l2);
        
        instructions.add(new FrameNode(F_APPEND, 1, new Object[] { "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type }, 0, null));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/darkhax/bookshelf/event/EnchantmentLevelEvent$" + type, "getResult", "()Lnet/minecraftforge/fml/common/eventhandler/Event$Result;", false));
        instructions.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/fml/common/eventhandler/Event$Result", "DENY", "Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"));
        
        LabelNode l4 = new LabelNode();
        instructions.add(new JumpInsnNode(IF_ACMPNE, l4));
        
        LabelNode l5 = new LabelNode();
        instructions.add(l5);
        instructions.add(new InsnNode(ICONST_0));
        instructions.add(new InsnNode(IRETURN));
        instructions.add(l4);
        
        method.instructions.insertBefore(method.instructions.getFirst(), instructions);
    }
}