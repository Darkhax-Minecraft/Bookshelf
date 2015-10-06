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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class InstructionComparator {
    
    // TODO: Add documentation
    public static InsnList getImportantList (InsnList list) {
        
        if (list.size() == 0)
            return list;
            
        HashMap<LabelNode, LabelNode> labels = new HashMap<LabelNode, LabelNode>();
        
        for (AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext())
            if (insn instanceof LabelNode)
                labels.put((LabelNode) insn, (LabelNode) insn);
                
        InsnList importantNodeList = new InsnList();
        
        for (AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext()) {
            
            if (insn instanceof LabelNode || insn instanceof LineNumberNode)
                continue;
                
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
        
        if (node1.getType() != node2.getType())
            return false;
            
        else if (node1.getOpcode() != node2.getOpcode())
            return false;
            
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
        
        LinkedList<AbstractInsnNode> callNodes = new LinkedList<AbstractInsnNode>();
        
        for (int callPoint : insnListFind(haystack, needle))
            callNodes.add(haystack.get(callPoint));
            
        return callNodes;
    }
    
    // TODO: Add documentation
    public static List<Integer> insnListFind (InsnList haystack, InsnList needle) {
        
        LinkedList<Integer> list = new LinkedList<Integer>();
        
        for (int start = 0; start <= haystack.size() - needle.size(); start++)
            if (insnListMatches(haystack, needle, start))
                list.add(start);
                
        return list;
    }
    
    // TODO: Add documentation
    public static List<AbstractInsnNode> insnListFindEnd (InsnList haystack, InsnList needle) {
        
        LinkedList<AbstractInsnNode> callNodes = new LinkedList<AbstractInsnNode>();
        
        for (int callPoint : insnListFind(haystack, needle))
            callNodes.add(haystack.get(callPoint + needle.size() - 1));
            
        return callNodes;
    }
    
    // TODO: Add documentation
    public static List<InsnListSection> insnListFindL (InsnList haystack, InsnList needle) {
        
        HashSet<LabelNode> controlFlowLabels = new HashSet<LabelNode>();
        
        for (AbstractInsnNode insn = haystack.getFirst(); insn != null; insn = insn.getNext()) {
            
            switch (insn.getType()) {
                
                case 8:
                case 15:
                    break;
                    
                case 7:
                    JumpInsnNode jinsn = (JumpInsnNode) insn;
                    controlFlowLabels.add(jinsn.label);
                    break;
                    
                case 11:
                    TableSwitchInsnNode tsinsn = (TableSwitchInsnNode) insn;
                    for (LabelNode label : tsinsn.labels)
                        controlFlowLabels.add(label);
                    break;
                    
                case 12:
                    LookupSwitchInsnNode lsinsn = (LookupSwitchInsnNode) insn;
                    for (LabelNode label : lsinsn.labels)
                        controlFlowLabels.add(label);
                    break;
            }
        }
        
        LinkedList<InsnListSection> list = new LinkedList<InsnListSection>();
        
        nextsection : for (int start = 0; start <= haystack.size() - needle.size(); start++) {
            InsnListSection section = insnListMatchesL(haystack, needle, start, controlFlowLabels);
            
            if (section != null) {
                
                for (InsnListSection asection : list)
                    if (asection.last == section.last)
                        continue nextsection;
                        
                list.add(section);
            }
        }
        
        return list;
    }
    
    // TODO: Add documentation
    public static boolean insnListMatches (InsnList haystack, InsnList needle, int start) {
        
        if (haystack.size() - start < needle.size())
            return false;
            
        for (int i = 0; i < needle.size(); i++)
            if (!insnEqual(haystack.get(i + start), needle.get(i)))
                return false;
                
        return true;
    }
    
    // TODO: Add documentation
    private static InsnListSection insnListMatchesL (InsnList haystack, InsnList needle, int start, HashSet<LabelNode> controlFlowLabels) {
        
        int h = start, n = 0;
        
        for (; h < haystack.size() && n < needle.size(); h++) {
            
            AbstractInsnNode insn = haystack.get(h);
            
            if (insn.getType() == 15)
                continue;
                
            if (insn.getType() == 8 && !controlFlowLabels.contains(insn))
                continue;
                
            if (!insnEqual(haystack.get(h), needle.get(n)))
                return null;
                
            n++;
        }
        
        if (n != needle.size())
            return null;
            
        return new InsnListSection(haystack, start, h - 1);
    }
    
    /**
     * compares two integer instructions, to see if they match or not.
     * 
     * @param node1: The first integer instruction.
     * @param node2: The second integer instruction.
     * @return boolean: True if the instructions match, false if the do not.
     */
    public static boolean intInsnEqual (IntInsnNode node1, IntInsnNode node2) {
        
        return node1.operand == -1 || node2.operand == -1 || node1.operand == node2.operand;
        
    }
    
    /**
     * Compares two load constant instructions, to see if they match or not.
     * 
     * @param insn1: The first load constant instruction.
     * @param insn2: The second load constant instruction.
     * @return boolean: True if the instructions match, false if they do not.
     */
    public static boolean ldcInsnEqual (LdcInsnNode insn1, LdcInsnNode insn2) {
        
        return insn1.cst.equals("~") || insn2.cst.equals("~") || insn1.cst.equals(insn2.cst);
        
    }
    
    /**
     * Compares two method instructions, to see if they match or not.
     * 
     * @param insn1: The first method instruction.
     * @param insn2: The second method instruction.
     * @return boolean: True if the instructions match, false if the do not.
     */
    public static boolean methodInsnEqual (MethodInsnNode insn1, MethodInsnNode insn2) {
        
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }
    
    /**
     * Compares two type instructions, to see if they match or not.
     * 
     * @param insn1: The first type instruction.
     * @param insn2: The second type instruction.
     * @return boolean: True if the instructions match, false if they do not.
     */
    public static boolean typeInsnEqual (TypeInsnNode insn1, TypeInsnNode insn2) {
        
        return insn1.desc.equals("~") || insn2.desc.equals("~") || insn1.desc.equals(insn2.desc);
        
    }
    
    /**
     * Compares two variable instructions, to see if they match or not.
     * 
     * @param insn1: The first variable instruction.
     * @param insn2: The second variable instruction.
     * @return boolean: True if the instructions match, false if the do not.
     */
    public static boolean varInsnEqual (VarInsnNode insn1, VarInsnNode insn2) {
        
        return insn1.var == -1 || insn2.var == -1 || insn1.var == insn2.var;
        
    }
    
    /**
     * Compares two incremental integer instructions, to see if they match or not.
     * 
     * @param node1: The first incremental integer instruction.
     * @param node2: The second incremental integer instruction.
     * @return boolean: True if the instructions match, false if the don't.
     */
    public static boolean iincInsnEqual (IincInsnNode node1, IincInsnNode node2) {
        
        return node1.var == node2.var && node1.incr == node2.incr;
    }
    
    /**
     * Compares if two field instructions are equal.
     * 
     * @param insn1: The first instruction.
     * @param insn2: The second instruction.
     * @return boolean: True if the two instructions match, false if the do not.
     */
    public static boolean fieldInsnEqual (FieldInsnNode insn1, FieldInsnNode insn2) {
        
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }
    
    // TODO: Needs documentation
    public static class InsnListSection {
        
        public AbstractInsnNode first;
        public AbstractInsnNode last;
        
        public InsnListSection(InsnList haystack, int start, int end) {
            
            this.first = haystack.get(start);
            this.last = haystack.get(end);
        }
    }
}