/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
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
import net.darkhax.bookshelf.events.BlockTextureEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.MinecraftForge;

public final class TransformerBlockModelShapes {

    public static final Mapping METHOD_BLOCK_STRENGTH = new Mapping("func_178122_a", "getTexture", "net/minecraft/client/renderer/BlockModelShapes", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private TransformerBlockModelShapes () {

        throw new IllegalAccessError("Utility class");
    }

    public static byte[] transform (String name, String transformedName, byte[] classBytes) {

        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_BLOCK_STRENGTH.getMethodNode(clazz);

        final InsnList i = new InsnList();
        final LabelNode l0 = new LabelNode();
        i.add(l0);
        i.add(new TypeInsnNode(Opcodes.NEW, "net/darkhax/bookshelf/events/BlockTextureEvent"));
        i.add(new InsnNode(Opcodes.DUP));
        i.add(new VarInsnNode(Opcodes.ALOAD, 1));
        i.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/darkhax/bookshelf/events/BlockTextureEvent", "<init>", "(Lnet/minecraft/block/state/IBlockState;)V", false));
        i.add(new VarInsnNode(Opcodes.ASTORE, 2));

        final LabelNode l1 = new LabelNode();
        i.add(l1);
        i.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        i.add(new VarInsnNode(Opcodes.ALOAD, 2));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        i.add(new InsnNode(Opcodes.POP));

        final LabelNode l2 = new LabelNode();
        i.add(l2);
        i.add(new VarInsnNode(Opcodes.ALOAD, 2));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/BlockTextureEvent", "isCanceled", "()Z", false));

        final LabelNode l3 = new LabelNode();
        i.add(new JumpInsnNode(Opcodes.IFEQ, l3));

        final LabelNode l4 = new LabelNode();
        i.add(l4);
        i.add(new VarInsnNode(Opcodes.ALOAD, 2));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/BlockTextureEvent", "getSprite", "()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", false));
        i.add(new InsnNode(Opcodes.ARETURN));
        i.add(l3);

        method.instructions.insertBefore(method.instructions.getFirst(), i);
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }

    public TextureAtlasSprite getTexture (IBlockState state) {

        final BlockTextureEvent event = new BlockTextureEvent(state);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {

            return event.getSprite();
        }

        return null;
    }
}
