/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.asm.transformers;

import javax.annotation.Nonnull;

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
import net.darkhax.bookshelf.events.BlockStrengthEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public final class TransformerForgeHooks {

    public static final Mapping METHOD_BLOCK_STRENGTH = new Mapping("blockStrength", "net/minecraftforge/common/ForeHooks", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private TransformerForgeHooks () {

        throw new IllegalAccessError("Utility class");
    }

    public static byte[] transform (String name, String transformedName, byte[] classBytes) {

        final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
        final MethodNode method = METHOD_BLOCK_STRENGTH.getMethodNode(clazz);

        final InsnList i = new InsnList();
        final LabelNode l0 = new LabelNode();
        i.add(l0);
        i.add(new TypeInsnNode(Opcodes.NEW, "net/darkhax/bookshelf/events/BlockStrengthEvent"));
        i.add(new InsnNode(Opcodes.DUP));
        i.add(new VarInsnNode(Opcodes.ALOAD, 0));
        i.add(new VarInsnNode(Opcodes.ALOAD, 1));
        i.add(new VarInsnNode(Opcodes.ALOAD, 2));
        i.add(new VarInsnNode(Opcodes.ALOAD, 3));
        i.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/darkhax/bookshelf/events/BlockStrengthEvent", "<init>", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", false));
        i.add(new VarInsnNode(Opcodes.ASTORE, 4));

        final LabelNode l1 = new LabelNode();
        i.add(l1);
        i.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        i.add(new VarInsnNode(Opcodes.ALOAD, 4));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        i.add(new InsnNode(Opcodes.POP));

        final LabelNode l2 = new LabelNode();
        i.add(l2);
        i.add(new VarInsnNode(Opcodes.ALOAD, 4));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/BlockStrengthEvent", "isCanceled", "()Z", false));

        final LabelNode l3 = new LabelNode();
        i.add(new JumpInsnNode(Opcodes.IFEQ, l3));

        final LabelNode l4 = new LabelNode();
        i.add(l4);
        i.add(new VarInsnNode(Opcodes.ALOAD, 4));
        i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/events/BlockStrengthEvent", "getStrength", "()F", false));
        i.add(new InsnNode(Opcodes.FRETURN));
        i.add(l3);

        method.instructions.insertBefore(method.instructions.getFirst(), i);
        return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }

    // @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull
    // BlockPos pos
    public static float blockStrength (@Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos) {

        final BlockStrengthEvent event = new BlockStrengthEvent(state, player, world, pos);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {

            return event.getStrength();
        }

        final float f = 32423f;
        return f;
    }
}
