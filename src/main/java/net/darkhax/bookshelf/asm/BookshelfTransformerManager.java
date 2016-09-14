package net.darkhax.bookshelf.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.darkhax.bookshelf.asm.mapping.FieldMapping;
import net.darkhax.bookshelf.asm.mapping.Mapping;
import net.minecraft.launchwrapper.IClassTransformer;

public class BookshelfTransformerManager implements IClassTransformer {
    
    private String CLASS_TILE_ENTITY_ITEM_STACK_RENDERER;
    private Mapping METHOD_RENDER_ITEM;
    private FieldMapping FIELD_INSTANCE;
    
    boolean loading = false;
    boolean loaded = false;
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] classBytes) {
        
        if (!this.loading) {
            this.loading = true;
            this.CLASS_TILE_ENTITY_ITEM_STACK_RENDERER = "net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer";
            this.METHOD_RENDER_ITEM = new Mapping("func_180454_a", "renderItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
            this.FIELD_INSTANCE = new FieldMapping(this.CLASS_TILE_ENTITY_ITEM_STACK_RENDERER, "field_72777_q", "instance", "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;");
            this.loaded = true;
        }
        if (this.loaded)
            if (transformedName.equals("net.minecraft.client.renderer.RenderItem")) {
                final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
                this.transformRenderItem(this.METHOD_RENDER_ITEM.getMethodNode(clazz));
                return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS);
            }
            
        return classBytes;
    }
    
    private void transformRenderItem (MethodNode method) {
        
        {
            final InsnList needle = new InsnList();
            
            needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer", "instance", "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;"));
            needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
            needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer", "renderByItem", "(Lnet/minecraft/item/ItemStack;)V", false));
            needle.add(new JumpInsnNode(Opcodes.GOTO, new LabelNode()));
            
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            
            final AbstractInsnNode pointer = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);
            final InsnList newInstr = new InsnList();
            
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
            newInstr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/darkhax/bookshelf/common/BookshelfRegistry", "renderPreModel", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false));
            newInstr.add(new LabelNode());
            
            method.instructions.insert(pointer, newInstr);
        }
        
        {
            final InsnList needle = new InsnList();
            needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
            needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
            needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
            needle.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", false));
            
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            
            final AbstractInsnNode pointer = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);
            final InsnList newInstr = new InsnList();
            
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
            newInstr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/darkhax/bookshelf/common/BookshelfRegistry", "renderPostModel", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false));
            newInstr.add(new LabelNode());
            
            method.instructions.insert(pointer, newInstr);
        }
        
        {
            final InsnList needle = new InsnList();
            needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
            needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
            needle.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderEffect", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false));
            
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            
            final AbstractInsnNode pointer = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);
            final InsnList newInstr = new InsnList();
            
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
            newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
            newInstr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/darkhax/bookshelf/common/BookshelfRegistry", "renderPostEffect", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false));
            newInstr.add(new LabelNode());
            
            method.instructions.insert(pointer, newInstr);
        }
    }
}