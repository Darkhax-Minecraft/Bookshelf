/*******************************************************************************************************************
 * Copyright: SanAndreasP
 *
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *            http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Changes: -Removal of deprecated methods
 *          -Reformat to project specifics
 *          -Recreation of all documentation
 *          -Renamed to ASMUtils
 *******************************************************************************************************************/

package net.darkhax.bookshelf.asm;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.lib.Constants;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

public final class ASMUtils {

    /**
     * Whether or not the game is running with srg mappings.
     */
    public static boolean isSrg = true;

    /**
     * Whether or not ASM has been enabled.
     */
    public static boolean isASMEnabled = false;

    /**
     * Converts a ClassNode into a byte array which can then be returned by your transformer.
     *
     * @param classNode: An instance of the ClassNode you wish to convert into a byte array.
     * @param flags: The flags to use when converting the ClassNode. These are generally
     *        COMPUTE_FRAMES and COMPUTE_MAXS.
     * @return byte[]: A byte array representation of the ClassNode.
     */
    public static byte[] createByteArrayFromClass (ClassNode classNode, int flags) {

        final ClassWriter classWriter = new ClassWriter(flags);
        classNode.accept(classWriter);

        return classWriter.toByteArray();
    }

    /**
     * Converts a byte array into a ClassNode which can then easily be worked with and
     * manipulated.
     *
     * @param classBytes: The byte array representation of the class.
     * @return ClassNode: A ClassNode representation of the class, built from the byte array.
     */
    public static ClassNode createClassFromByteArray (byte[] classBytes) {

        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return classNode;
    }

    /**
     * Checks if a ClassNode has an instance of the target method. This does not take
     * descriptors into account.
     *
     * @param classNode: The instance of ClassNode to look through.
     * @param methodName: The name of the method you are looking for.
     * @return boolean: True if the method is found, false if it is not.
     */
    public static boolean hasClassMethodName (ClassNode classNode, String methodName) {

        for (final MethodNode method : classNode.methods)
            if (methodName.equals(method.name))
                return true;

        return false;
    }

    /**
     * Finds the first instruction node after the the provided instruction list, within a
     * larger list of instructions.
     *
     * @param haystack: A complete list of instructions which is being searched through.
     * @param needle: A small list of instructions which represents a very specific part of the
     *        larger instruction list.
     * @return AbstractInsnNode: The first instruction node from the specified list of
     *         instructions. (the needle)
     */
    public static AbstractInsnNode findFirstNodeFromNeedle (InsnList haystack, InsnList needle) {

        final List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);

        if (ret.size() != 1)
            Constants.LOG.warn(new InvalidNeedleException(ret.size()));

        return ret.get(0);
    }

    /**
     * Finds the last instruction node after the provided instruction list, within a larger
     * list of instructions.
     *
     * @param haystack: A large list of instructions which is being searched through.
     * @param needle: A small list of instructions which represents a very specific part of the
     *        larger instruction list.
     * @return AbstractInsnNode: The last instruction node from the specified list of
     *         instructions. (the needle)
     */
    public static AbstractInsnNode findLastNodeFromNeedle (InsnList haystack, InsnList needle) {

        final List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(haystack, needle);

        if (ret.size() != 1)
            Constants.LOG.warn(new InvalidNeedleException(ret.size()));

        return ret.get(0);
    }

    /**
     * Removes a specific set of instructions (the needle) from a much larger set of
     * instructions (the hay stack). Be cautious when using this method, as it is almost never
     * a good idea to remove instructions.
     *
     * @param haystack: A large list of instructions which is being searched through.
     * @param needle: A specific list of instructions which are to be removed from the larger
     *        instruction list.
     */
    public static void removeNeedleFromHaystack (InsnList haystack, InsnList needle) {

        final int firstInd = haystack.indexOf(findFirstNodeFromNeedle(haystack, needle));
        final int lastInd = haystack.indexOf(findLastNodeFromNeedle(haystack, needle));
        final List<AbstractInsnNode> realNeedle = new ArrayList<AbstractInsnNode>();

        for (int i = firstInd; i <= lastInd; i++)
            realNeedle.add(haystack.get(i));

        for (final AbstractInsnNode node : realNeedle)
            haystack.remove(node);
    }

    /**
     * Checks if an instruction can be ignored. While this typically isn't needed, there may be
     * some cases where you want to ignore LabelNodes and LineNumberNodes. This method will
     * help with that.
     *
     * @param insn: The AbstractInsnNode to check against.
     * @return boolean: True if it okay to ignore this instruction, false if it not okay.
     */
    public static boolean canIgnoreInstruction (AbstractInsnNode insn) {

        return insn instanceof LabelNode || insn instanceof LineNumberNode;
    }

    public static class InvalidNeedleException extends RuntimeException {

        /**
         * An exception which is thrown when there is an issue working with a needle. This
         * could be due to the needle not being found within a hay stack, multiple versions of
         * the same needle being found, or another anomaly.
         *
         * @param count: The amount of the specified needle which was found.
         */
        public InvalidNeedleException(int count) {

            super(count > 1 ? "More than one instance of the needle have been found!" : count < 1 ? "The needle was not found" : "There is a glitch in the matrix");
        }
    }
}