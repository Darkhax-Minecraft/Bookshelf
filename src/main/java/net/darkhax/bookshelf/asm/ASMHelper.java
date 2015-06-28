package net.darkhax.bookshelf.asm;

import java.util.List;

import net.darkhax.bookshelf.util.Constants;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class ASMHelper {
    
    public static boolean isMCP = true;
    
    /**
     * Allows for easily using the correct mapping for your environment. If you are in a
     * developer environment, the deobf value will be used. If you are not in the development
     * environment the obf value will be used.
     * 
     * @param obf: The obfuscated variant of the mapping.
     * @param deobf: The deobfuscated variant of the mapping.
     * @return String: An appropriate mapping to use for this environment.
     */
    public static String getCorrectMapping (String mcp, String srg) {
    
        return isMCP ? mcp : srg;
    }
    
    /**
     * Creates a byte array representation of a ClassNode.
     * 
     * @param classNode: An instance of the ClassNode which you are wanting to retrieve a byte
     *            array of.
     * @param flags: The flags to be used with the ClassWriter. Generally COMPUTE_MAXS and
     *            COMPUTE_FRAMES.
     * @return byte[]: A byte array representation of the ClassNode.
     */
    public static byte[] createBytesFromClass (ClassNode classNode, int flags) {
    
        ClassWriter classWriter = new ClassWriter(flags);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
    
    // TODO rewrite description.
    /**
     * Converts an array of bytes into a ClassNode.
     * 
     * @param bytes: An array of bytes which represents the ClassNode.
     * @return ClassNode: A ClassNode which represents the suplied byte aray.
     */
    public static ClassNode createClassFromBytes (byte[] bytes) {
    
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return classNode;
    }
    
    /**
     * Checks if a ClassNode has a desired method.
     * 
     * @param classNode: Instance of the ClassNode which represents the class being looked at.
     * @param methodName: The name of the method that is being looked for.
     * @return boolean: True if the method is found, false if it isn't.
     */
    public static boolean hasMethod (ClassNode classNode, String methodName) {
    
        for (MethodNode method : classNode.methods)
            if (methodName.equals(method.name))
                return true;
        
        return false;
    }
    
    /**
     * Atempts to grab a desired MethodNode from a ClassNode.
     * 
     * @param classNode: The ClassNode to search through.
     * @param name: The name of the desired method.
     * @param descriptor: The descriptor for the method.
     * @return MethodNode: The desired MethodNode (hopefully). Can possibly be null.
     */
    public static MethodNode getMethodFromClass (ClassNode classNode, String name, String descriptor) {
    
        for (MethodNode methodNode : classNode.methods)
            if (name.equals(methodNode.name) && descriptor.equals(methodNode.desc))
                return methodNode;
        
        Constants.LOG.info("Attempted to find Method: " + name + " but it could not be found. Expect a crash!");
        return null;
    }
    
    // TODO: Throw a custom exception
    /**
     * Finds the first instance of a specified instruction within an instruction list.
     * 
     * @param haystack: A list of all instructions to search through.
     * @param needle: A precise list of instructions which you are looking for.
     * @return AbstractInsnNode: The first instance of the specified instruction list.
     */
    public static AbstractInsnNode findFirstNodeFromNeedle (InsnList haystack, InsnList needle) {
    
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);
        
        if (ret.size() != 1)
            Constants.LOG.info("Attempt to find an instruction failed. Expect a crash!" + System.getProperty("line.separator") + needle.toString());
        
        return ret.get(0);
    }
    
    // TODO: Throw a custom exception
    /**
     * Finds the last instance of a specified instruction within an instruction list.
     * 
     * @param haystack: A list of all instructions to search through.
     * @param needle: A precise list of instructions which you are looking for.
     * @return AbstractInsnNode: The last instance of the specified instruction list.
     */
    public static AbstractInsnNode findLastNodeFromNeedle (InsnList haystack, InsnList needle) {
    
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(haystack, needle);
        
        if (ret.size() != 1)
            Constants.LOG.info("Attempt to find an instruction failed. Expect a crash!" + System.getProperty("line.separator") + needle.toString());
        
        return ret.get(0);
    }
    
}