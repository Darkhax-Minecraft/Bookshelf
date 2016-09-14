package net.darkhax.bookshelf.asm.mapping;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.darkhax.bookshelf.asm.ASMUtils;
import net.darkhax.bookshelf.lib.Constants;

public class Mapping {
    
    private final String name;
    
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
    public Mapping(String srgName, String mcpName, String descriptor) {
        
        this.name = ASMUtils.isSrg ? srgName : mcpName;
        this.descriptor = descriptor;
    }
    
    /**
     * Creates a mapping which can be used in ASM byte code manipulation or reflection.
     *
     * @param name The name of the mapping. This should be one you are 100% certain of.
     * @param descriptor The descriptor.
     */
    public Mapping(String name, String descriptor) {
        
        this.name = name;
        this.descriptor = descriptor;
    }
    
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
}