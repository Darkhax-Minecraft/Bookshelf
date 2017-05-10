/*******************************************************************************************************************
 * Copyright: SanAndreasP
 *
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *            http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Changes: -The addition of new java documentation.
 *          -Reformat to project specifics
 *******************************************************************************************************************/
package net.darkhax.bookshelf.asm;

import static org.objectweb.asm.tree.AbstractInsnNode.FIELD_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.IINC_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.INT_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.LDC_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.METHOD_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.TYPE_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.VAR_INSN;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class InstructionComparator {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private InstructionComparator () {

        throw new IllegalAccessError("Utility class");
    }

    // TODO: Add documentation
    public static InsnList getImportantList (InsnList list) {

        if (list.size() == 0) {
            return list;
        }

        final HashMap<LabelNode, LabelNode> labels = new HashMap<>();

        for (AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext()) {
            if (insn instanceof LabelNode) {
                labels.put((LabelNode) insn, (LabelNode) insn);
            }
        }

        final InsnList importantNodeList = new InsnList();

        for (AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext()) {

            if (insn instanceof LabelNode || insn instanceof LineNumberNode) {
                continue;
            }

            importantNodeList.add(insn.clone(labels));
        }

        return importantNodeList;
    }

    /**
     * Compares whether or not two instructions are equal.
     *
     * @param node1: The first instruction.
     * @param node2: The second instruction.
     * @return boolean: True if they are the same, false if they are not.
     */
    public static boolean insnEqual (AbstractInsnNode node1, AbstractInsnNode node2) {

        if (node1.getType() != node2.getType() || node1.getOpcode() != node2.getOpcode()) {
            return false;
        }

        switch (node2.getType()) {

            case VAR_INSN:
                return varInsnEqual((VarInsnNode) node1, (VarInsnNode) node2);

            case TYPE_INSN:
                return typeInsnEqual((TypeInsnNode) node1, (TypeInsnNode) node2);

            case FIELD_INSN:
                return fieldInsnEqual((FieldInsnNode) node1, (FieldInsnNode) node2);

            case METHOD_INSN:
                return methodInsnEqual((MethodInsnNode) node1, (MethodInsnNode) node2);

            case LDC_INSN:
                return ldcInsnEqual((LdcInsnNode) node1, (LdcInsnNode) node2);

            case IINC_INSN:
                return iincInsnEqual((IincInsnNode) node1, (IincInsnNode) node2);

            case INT_INSN:
                return intInsnEqual((IntInsnNode) node1, (IntInsnNode) node2);

            default:
                return true;
        }
    }

    // TODO: Add documentation
    public static List<AbstractInsnNode> insnListFindStart (InsnList haystack, InsnList needle) {

        final LinkedList<AbstractInsnNode> callNodes = new LinkedList<>();

        for (final int callPoint : insnListFind(haystack, needle)) {
            callNodes.add(haystack.get(callPoint));
        }

        return callNodes;
    }

    // TODO: Add documentation
    public static List<Integer> insnListFind (InsnList haystack, InsnList needle) {

        final LinkedList<Integer> list = new LinkedList<>();

        for (int start = 0; start <= haystack.size() - needle.size(); start++) {
            if (insnListMatches(haystack, needle, start)) {
                list.add(start);
            }
        }

        return list;
    }

    // TODO: Add documentation
    public static List<AbstractInsnNode> insnListFindEnd (InsnList haystack, InsnList needle) {

        final LinkedList<AbstractInsnNode> callNodes = new LinkedList<>();

        for (final int callPoint : insnListFind(haystack, needle)) {
            callNodes.add(haystack.get(callPoint + needle.size() - 1));
        }

        return callNodes;
    }

    // TODO: Add documentation
    public static boolean insnListMatches (InsnList haystack, InsnList needle, int start) {

        if (haystack.size() - start < needle.size()) {
            return false;
        }

        for (int i = 0; i < needle.size(); i++) {
            if (!insnEqual(haystack.get(i + start), needle.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if two IntInsnNodes are the same. For them to be the same, the operand for the
     * instructions must be the same. They will also be considered the same, if either operand
     * is -1.
     *
     * @param node1 The first IntInsnNode to compare.
     * @param node2 The second IntInsnNode to compare.
     * @return boolean: True if the instructions share the same operand, or if either operand
     *         is -1.
     */
    public static boolean intInsnEqual (IntInsnNode node1, IntInsnNode node2) {

        return node1.operand == -1 || node2.operand == -1 || node1.operand == node2.operand;

    }

    /**
     * Checks if two LdcInsnNodes are the same. For them to be the same, the constant that is
     * to be loaded onto the stack must be the same. They will also be considered the same, if
     * either is loading a String object of ~.
     *
     * @param insn1 The first LdcInsnNode to compare.
     * @param insn2 The second LdcInsnNode to compare.
     * @return boolean: True if the instructions are loading the same constant, or if either is
     *         loading "~".
     */
    public static boolean ldcInsnEqual (LdcInsnNode insn1, LdcInsnNode insn2) {

        return "~".equals(insn1.cst) || "~".equals(insn2.cst) || insn1.cst.equals(insn2.cst);

    }

    /**
     * Checks if two MethodInsnNodes are the same. For them to be considered the same, both
     * instructions must share the same description, owner, and name..
     *
     * @param insn1 The first MethodInsNode to compare.
     * @param insn2 The second MethodInsnNode to compare.
     * @return boolean True if the instructions share the same owner, the same name, and the
     *         same description.
     */
    public static boolean methodInsnEqual (MethodInsnNode insn1, MethodInsnNode insn2) {

        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }

    /**
     * Checks if two TypeInsnNodes are the same. For them to be considered the same, both
     * instructions must share the same description. They will also be considered to be the
     * same, if either has a description of ~.
     *
     * @param insn1 The first TypeInsnNode to compare.
     * @param insn2 The second TypeInsnNode to compare.
     * @return boolean: True if the instructions share the same description, or if either
     *         description is ~.
     */
    public static boolean typeInsnEqual (TypeInsnNode insn1, TypeInsnNode insn2) {

        return "~".equals(insn1.desc) || "~".equals(insn2.desc) || insn1.desc.equals(insn2.desc);

    }

    /**
     * Checks two VarInsnNodes to see if they are the same. For them to be the same, their
     * variable index must be the same. They will also be considered the same if either one has
     * an index of -1.
     *
     * @param insn1 The first VarInsnNode to compare.
     * @param insn2 The second VarInsnNode to compare.
     * @return boolean True if the instructions share the same variable index, or if either
     *         instruction has an index of -1.
     */
    public static boolean varInsnEqual (VarInsnNode insn1, VarInsnNode insn2) {

        return insn1.var == -1 || insn2.var == -1 || insn1.var == insn2.var;

    }

    /**
     * Checks if two IincInsnNodes are the same. For them to be the same, they must share the
     * same local variable index, and increment by the same amount.
     *
     * @param node1 The first IincInsnNode to compare.
     * @param node2 The second IincInsnNode to compare.
     * @return boolean True if the instructions share the same local variable index, and
     *         increment by the same amount.
     */
    public static boolean iincInsnEqual (IincInsnNode node1, IincInsnNode node2) {

        return node1.var == node2.var && node1.incr == node2.incr;
    }

    /**
     * Checks if two FieldInsnNodes are the same. For them to be the same, the owner, name and
     * description must be the same.
     *
     * @param insn1 The first instruction to compare.
     * @param insn2 The second instruction to compare.
     * @return boolean True if the instructions share the same owner, name and description.
     */
    public static boolean fieldInsnEqual (FieldInsnNode insn1, FieldInsnNode insn2) {

        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }
}