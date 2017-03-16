package net.darkhax.bookshelf.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.asm.Mapping;

public final class TransformerCrashReport {

    public static final Mapping METHOD_GET_WITTY_COMMENT = new Mapping("func_71503_h", "getWittyComment", "net/minecraft/crash/CrashReport", "()Ljava/lang/String;");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private TransformerCrashReport () {

        throw new IllegalAccessError("Utility class");
    }

    // Hopefully this doesn't crash anyone lol If it does, *shrugs*
    public static byte[] transform (String name, String transformedName, byte[] classBytes) {

        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_GET_WITTY_COMMENT.getMethodNode(clazz);
        final InsnList n1 = new InsnList();

        // Creates the event object
        final LabelNode start = new LabelNode();
        n1.add(start);
        n1.add(new TypeInsnNode(Opcodes.NEW, "net/darkhax/bookshelf/events/CrashReportEvent"));
        n1.add(new InsnNode(Opcodes.DUP));
        n1.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/darkhax/bookshelf/events/CrashReportEvent", "<init>", "()V", false));
        n1.add(new VarInsnNode(Opcodes.ASTORE, 0));

        // Posts the forge event
        final LabelNode forge = new LabelNode();
        n1.add(forge);
        n1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        n1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        n1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));

        // Checks if the event message is not empty
        final LabelNode l4 = new LabelNode();
        n1.add(l4);
        n1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        n1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/CrashReportEvent", "getMessage", "()Ljava/lang/String;", false));
        n1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false));

        // The not part of "is not empty"
        final LabelNode l5 = new LabelNode();
        n1.add(new JumpInsnNode(Opcodes.IFNE, l5));

        // Handles returning the event string if one exists
        final LabelNode l6 = new LabelNode();
        n1.add(l6);
        n1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        n1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/CrashReportEvent", "getMessage", "()Ljava/lang/String;", false));
        n1.add(new InsnNode(Opcodes.ARETURN));
        n1.add(l5);

        method.instructions.insertBefore(method.instructions.getFirst(), n1);
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }
}
