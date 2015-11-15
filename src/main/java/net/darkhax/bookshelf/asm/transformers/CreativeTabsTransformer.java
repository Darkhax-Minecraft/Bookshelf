package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.darkhax.bookshelf.asm.ASMHelper;

public class CreativeTabsTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode creativeTabsClass = ASMHelper.createClassFromByteArray(bytes);
        
        if (ASMHelper.hasClassMethodName(creativeTabsClass, displayAllReleventItems)) {
            
            transformDisplayAllReleventItems(ASMHelper.getMethodFromClass(creativeTabsClass, displayAllReleventItems, "(Ljava/util/List;)V"));
            return ASMHelper.createByteArrayFromClass(creativeTabsClass, ClassWriter.COMPUTE_MAXS);
        }
        
        return bytes;
    }
    
    /**
     * Adds in two new hook calls, before and after the relevant items are generated.
     * 
     * @param method: A MethodNode which reflects the displayAllReleventItems method.
     */
    private static void transformDisplayAllReleventItems (MethodNode method) {
        
        InsnList preInsns = new InsnList();
        LabelNode preStart = new LabelNode();
        preInsns.add(preStart);
        preInsns.add(new VarInsnNode(ALOAD, 0));
        preInsns.add(new VarInsnNode(ALOAD, 1));
        preInsns.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onCreativeTabDisplayPre", "(Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)Z", false));
        LabelNode l1 = new LabelNode();
        preInsns.add(new JumpInsnNode(IFEQ, l1));
        LabelNode l2 = new LabelNode();
        preInsns.add(l2);
        preInsns.add(new InsnNode(RETURN));
        preInsns.add(l1);
        preInsns.add(new FrameNode(F_SAME, 0, null, 0, null));
        
        method.instructions.insertBefore(method.instructions.getFirst(), preInsns);
        
        InsnList postNeedle = new InsnList();
        LabelNode l14 = new LabelNode();
        postNeedle.add(new VarInsnNode(ALOAD, 0));
        postNeedle.add(new VarInsnNode(ALOAD, 1));
        postNeedle.add(new VarInsnNode(ALOAD, 0));
        postNeedle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/creativetab/CreativeTabs", getRelevantEnchantmentTypes, "()[Lnet/minecraft/enchantment/EnumEnchantmentType;", false));
        postNeedle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/creativetab/CreativeTabs", addEnchantmentBooksToList, "(Ljava/util/List;[Lnet/minecraft/enchantment/EnumEnchantmentType;)V", false));
        postNeedle.add(l14);
        
        InsnList postInsns = new InsnList();
        LabelNode post = new LabelNode();
        postInsns.add(post);
        postInsns.add(new VarInsnNode(ALOAD, 0));
        postInsns.add(new VarInsnNode(ALOAD, 1));
        postInsns.add(new MethodInsnNode(INVOKESTATIC, "net/darkhax/bookshelf/handler/BookshelfHooks", "onCreativeTabDisplayPost", "(Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", false));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, postNeedle);
        method.instructions.insert(pointer, postInsns);
    }
}