package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.getLootingModifier;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;

public class EnchantmentHelperTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode enchantmentHelperClass = ASMHelper.createClassFromByteArray(bytes);
        transformGetLootingModifier(ASMHelper.getMethodFromClass(enchantmentHelperClass, getLootingModifier, "(Lnet/minecraft/entity/EntityLivingBase;)I"));
        return ASMHelper.createByteArrayFromClass(enchantmentHelperClass, ClassWriter.COMPUTE_MAXS);
    }
    
    /**
     * Transforms the getLootingModifier method, to trigger the LootingEvent. This allows for
     * new behavior to happen, when looting modifiers are calculated.
     * 
     * @param method: A MethodNode that represents the getLootingModifier method.
     */
    private static void transformGetLootingModifier (MethodNode method) {
        
        InsnList instructions = new InsnList();
        
        LabelNode start = new LabelNode();
        instructions.add(start);
        instructions.add(new TypeInsnNode(NEW, "net/darkhax/bookshelf/event/LootingEvent"));
        instructions.add(new InsnNode(DUP));
        instructions.add(new VarInsnNode(ALOAD, 0));
        instructions.add(new MethodInsnNode(INVOKESPECIAL, "net/darkhax/bookshelf/event/LootingEvent", "<init>", "(Lnet/minecraft/entity/EntityLivingBase;)V", false));
        instructions.add(new VarInsnNode(ASTORE, 1));
        
        LabelNode bus = new LabelNode();
        instructions.add(bus);
        instructions.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false));
        instructions.add(new InsnNode(POP));
        
        LabelNode l1 = new LabelNode();
        instructions.add(l1);
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/darkhax/bookshelf/event/LootingEvent", "getResult", "()Lcpw/mods/fml/common/eventhandler/Event$Result;", false));
        instructions.add(new FieldInsnNode(GETSTATIC, "cpw/mods/fml/common/eventhandler/Event$Result", "ALLOW", "Lcpw/mods/fml/common/eventhandler/Event$Result;"));
        
        LabelNode l2 = new LabelNode();
        instructions.add(new JumpInsnNode(IF_ACMPNE, l2));
        
        LabelNode l3 = new LabelNode();
        instructions.add(l3);
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new FieldInsnNode(GETFIELD, "net/darkhax/bookshelf/event/LootingEvent", "lootingModifier", "I"));
        instructions.add(new InsnNode(IRETURN));
        instructions.add(l2);
        
        instructions.add(new FrameNode(F_APPEND, 1, new Object[] { "net/darkhax/bookshelf/event/LootingEvent" }, 0, null));
        instructions.add(new VarInsnNode(ALOAD, 1));
        instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/darkhax/bookshelf/event/LootingEvent", "getResult", "()Lcpw/mods/fml/common/eventhandler/Event$Result;", false));
        instructions.add(new FieldInsnNode(GETSTATIC, "cpw/mods/fml/common/eventhandler/Event$Result", "DENY", "Lcpw/mods/fml/common/eventhandler/Event$Result;"));
        
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