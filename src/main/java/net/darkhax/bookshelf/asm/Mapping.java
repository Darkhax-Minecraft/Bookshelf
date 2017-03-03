package net.darkhax.bookshelf.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.darkhax.bookshelf.lib.Constants;

public class Mapping {

    /**
     * The name of the mapping.
     */
    private final String name;

    /**
     * The path to the class which holds the mapping.
     */
    private final String classPath;

    /**
     * The descriptor for the mapping. Only applicable to methods. Will be null for fields.
     */
    private final String descriptor;

    /**
     * Creates a mapping which can be used in ASM byte code manipulation or reflection.
     *
     * @param srgName The name of mapping in a srg environment.
     * @param mcpName The name of the mapping in a mappend mcp environment.
     * @param descriptor The descriptor.
     */
    public Mapping (String srgName, String mcpName, String path, String descriptor) {

        this.name = ASMUtils.isSrg ? srgName : mcpName;
        this.descriptor = descriptor;
        this.classPath = path;
    }

    /**
     * Creates a mapping which can be used in ASM byte code manipulation or reflection.
     *
     * @param name The name of the mapping. This should be one you are 100% certain of.
     * @param descriptor The descriptor.
     */
    public Mapping (String name, String path, String descriptor) {

        this.name = name;
        this.descriptor = descriptor;
        this.classPath = path;
    }

    /**
     * Gets the name of the mapping. This will always be the correct mapping unless created
     * wrongly.
     *
     * @return The name of the mapping.
     */
    public String getName () {

        return this.name;
    }

    /**
     * Gets the descriptor of the mapping.
     *
     * @return The descriptor of the mapping.
     */
    public String getDescriptor () {

        return this.descriptor;
    }

    public MethodNode getMethodNode (ClassNode classNode) {

        for (final MethodNode mnode : classNode.methods)
            if (this.getName().equals(mnode.name) && this.descriptor.equals(mnode.desc))
                return mnode;

        Constants.LOG.warn(new RuntimeException(String.format("The method %s with descriptor %s could not be found in %s", this.getName(), this.descriptor, classNode.name)));
        return null;
    }

    public MethodInsnNode getMethodInsn (int opcode, boolean isInterface) {

        return new MethodInsnNode(opcode, this.classPath, this.name, this.descriptor, isInterface);
    }

    public FieldInsnNode getFieldNode (int opCode) {

        return new FieldInsnNode(opCode, this.classPath, this.getName(), this.getDescriptor());
    }
}