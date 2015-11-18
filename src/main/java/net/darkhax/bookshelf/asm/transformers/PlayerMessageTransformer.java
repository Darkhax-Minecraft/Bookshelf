package net.darkhax.bookshelf.asm.transformers;

import static net.darkhax.bookshelf.asm.Mappings.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import net.minecraftforge.common.MinecraftForge;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.event.PlayerMessageEvent;

public class PlayerMessageTransformer {
    
    public static byte[] transform (String name, String transformedName, byte[] bytes) {
        
        ClassNode itemClass = ASMHelper.createClassFromByteArray(bytes);
        
        if (ASMHelper.hasClassMethodName(itemClass, getColorFromItemStack))
            onDisconnect(ASMHelper.getMethodFromClass(itemClass, getColorFromItemStack, "(Lnet/minecraft/item/ItemStack;I)I"));
            
        return ASMHelper.createByteArrayFromClass(itemClass, ClassWriter.COMPUTE_MAXS);
    }
    
    private static void onDisconnect (MethodNode method) {
        
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 2));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/ChatComponentTranslation", "getChatStyle", "()Lnet/minecraft/util/ChatStyle;", false));
        needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/util/EnumChatFormatting", "YELLOW", "Lnet/minecraft/util/EnumChatFormatting;"));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/ChatStyle", "setColor", "(Lnet/minecraft/util/EnumChatFormatting;)Lnet/minecraft/util/ChatStyle;", false));
        needle.add(new InsnNode(POP));
    }
    
    public EntityPlayerMP playerEntity;
    
    public void stuff () {
        
        ChatComponentTranslation chatcomponenttranslation = null;
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        
    }
}
