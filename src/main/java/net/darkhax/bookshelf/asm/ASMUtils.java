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
import java.util.ListIterator;

import org.apache.commons.lang3.SystemUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.Launch;

public final class ASMUtils {
    
    /**
     * Whether or not the game is running with srg mappings.
     */
    public static boolean isSrg = !(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    
    /**
     * An array of instruction type names by order of ID.
     */
    public static final String[] INSN_TYPES = new String[] { "insn", "int", "var", "type", "field", "method", "invoke_dynamic", "jump", "label", "ldc", "iinc", "table_switch", "lookup_switch", "multianewarry", "frame", "line" };
    
    /**
     * An array of opcode names by order of their ID.
     */
    public static final String[] OPCODES = new String[] { "NOP", "ACONST_NULL", "ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3", "ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1", "FCONST_0", "FCONST_1", "FCONST_2", "DCONST_0", "DCONST_1", "BIPUSH", "SIPUSH", "LDC", "LDC_W", "LDC2_W", "ILOAD", "LLOAD", "FLOAD", "DLOAD", "ALOAD", "ILOAD_0", "ILOAD_1", "ILOAD_2", "ILOAD_3", "LLOAD_0", "LLOAD_1", "LLOAD_2", "LLOAD_3", "FLOAD_0", "FLOAD_1", "FLOAD_2", "FLOAD_3", "DLOAD_0", "DLOAD_1", "DLOAD_2", "DLOAD_3", "ALOAD_0", "ALOAD_1", "ALOAD_2", "ALOAD_3", "IALOAD", "LALOAD", "FALOAD", "DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "ISTORE", "LSTORE", "FSTORE", "DSTORE", "ASTORE", "ISTORE_0", "ISTORE_1", "ISTORE_2", "ISTORE_3", "LSTORE_0", "LSTORE_1", "LSTORE_2", "LSTORE_3", "FSTORE_0", "FSTORE_1", "FSTORE_2", "FSTORE_3", "DSTORE_0", "DSTORE_1", "DSTORE_2", "DSTORE_3", "ASTORE_0", "ASTORE_1", "ASTORE_2", "ASTORE_3", "IASTORE", "LASTORE", "FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE", "POP", "POP2", "DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "SWAP", "IADD", "LADD", "FADD", "DADD", "ISUB", "LSUB", "FSUB", "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV", "LDIV", "FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", "INEG", "LNEG", "FNEG", "DNEG", "ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR", "IAND", "LAND", "IOR", "LOR", "IXOR", "LXOR", "IINC", "I2L", "I2F", "I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L", "D2F", "I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL", "DCMPG", "IFEQ", "IFNE", "IFLT", "IFGE", "IFGT", "IFLE", "IF_ICMPEQ", "IF_ICMPNE", "IF_ICMPLT", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE", "IF_ACMPEQ", "IF_ACMPNE", "GOTO", "JSR", "RET", "TABLESWITCH", "LOOKUPSWITCH", "IRETURN", "LRETURN", "FRETURN", "DRETURN", "ARETURN", "RETURN", "GETSTATIC", "PUTSTATIC", "GETFIELD", "PUTFIELD", "INVOKEVIRTUAL", "INVOKESPECIAL", "INVOKESTATIC", "INVOKEINTERFACE", "INVOKEDYNAMIC", "NEW", "NEWARRAY", "ANEWARRAY", "ARRAYLENGTH", "ATHROW", "CHECKCAST", "INSTANCEOF", "MONITORENTER", "MONITOREXIT", "WIDE", "MULTIANEWARRAY", "IFNULL", "IFNONNULL", "GOTO_W", "JSR_W" };
    
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
            throw new InvalidNeedleException(ret.size(), needle, haystack);
        
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
            throw new InvalidNeedleException(ret.size(), needle, haystack);
        
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
        final List<AbstractInsnNode> realNeedle = new ArrayList<>();
        
        for (int i = firstInd; i <= lastInd; i++)
            realNeedle.add(haystack.get(i));
        
        for (final AbstractInsnNode node : realNeedle)
            haystack.remove(node);
    }
    
    /**
     * Creates a string representation of an AbstractInsnNode.
     * 
     * @param node The node to look at.
     * @return The resulting string.
     */
    public static String getInstructionString (AbstractInsnNode node) {
        
        final String type = node.getType() < 0 || node.getType() > INSN_TYPES.length ? "Invalid" : INSN_TYPES[node.getType()];
        final String opcode = node.getOpcode() < 0 || node.getOpcode() > OPCODES.length ? "N/A" : OPCODES[node.getOpcode()];
        return "Type: " + type + " Opcode: " + opcode;
    }
    
    private static String infod (InsnList needle, InsnList hayStack) {
        
        final StringBuilder builder = new StringBuilder();
        builder.append(SystemUtils.LINE_SEPARATOR);
        builder.append("Printing Needle" + SystemUtils.LINE_SEPARATOR);
        
        ListIterator<AbstractInsnNode> i = needle.iterator();
        
        while (i.hasNext())
            builder.append(getInstructionString(i.next()) + SystemUtils.LINE_SEPARATOR);
        
        builder.append("Printing Haystack" + SystemUtils.LINE_SEPARATOR);
        i = hayStack.iterator();
        
        while (i.hasNext())
            builder.append(getInstructionString(i.next()) + SystemUtils.LINE_SEPARATOR);
        
        builder.append("fuffff");
        return builder.toString();
    }
    
    public static class InvalidNeedleException extends RuntimeException {
        
        /**
         * An exception which is thrown when there is an issue working with a needle. This
         * could be due to the needle not being found within a hay stack, multiple versions of
         * the same needle being found, or another anomaly.
         *
         * @param count: The amount of the specified needle which was found.
         */
        public InvalidNeedleException(int count, InsnList needle, InsnList hayStack) {
            
            super((count > 1 ? "More than one instance of the needle have been found!" : count < 1 ? "The needle was not found" : "There is a glitch in the matrix") + infod(needle, hayStack));
        }
    }
}