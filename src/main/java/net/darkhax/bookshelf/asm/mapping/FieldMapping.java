package net.darkhax.bookshelf.asm.mapping;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldMapping extends Mapping {

    private final String classPath;

    public FieldMapping(String classMapping, String srgName, String mcpName, String descriptor) {

        super(srgName, mcpName, descriptor);
        this.classPath = classMapping.replace(".", "/");
    }

    public FieldInsnNode getFieldNode(int opCode) {

        return new FieldInsnNode(opCode, this.classPath, this.getName(), this.getDescriptor());
    }
}