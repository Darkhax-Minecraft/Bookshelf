package net.darkhax.bookshelf.asm;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityHorseTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform (String name, String transformedName, byte[] basicClass) {
    
        if (transformedName.equals("net.minecraft.entity.passive.EntityHorse")) {
            
            ClassNode horseClass = ASMHelper.createClassFromBytes(basicClass);
            
            injectGetCustomHorseArmor(horseClass);
            injectSetCustomHorseArmor(horseClass);
            
            transformEntityInit(ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("entityInit", "func_70088_a"), "()V"));
            transformIsValidArmor(ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("func_146085_a", "func_146085_a"), "(Lnet/minecraft/item/Item;)Z"));
            transformGetTotalArmorValue(ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("getTotalArmorValue", "func_70658_aO"), "()I"));
            transformUpdateHorseSlots(ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("func_110232_cE", "func_110232_cE"), "()V"));
            transformOnInvChanged(ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("onInventoryChanged", "func_76316_a"), "(Lnet/minecraft/inventory/InventoryBasic;)V"));
            
            MethodNode node = ASMHelper.getMethodFromClass(horseClass, ASMHelper.getCorrectMapping("setHorseTexturePaths", "func_110247_cG"), "()V");
            if (node != null)
                transformSetHorseTexturePaths(node);
            return ASMHelper.createBytesFromClass(horseClass, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }
    
    /**
     * Injects the getCustomBookshelfHorseArmor method into EntityHorse. This method can be
     * used to retrieve the custom armor ItemStack from any EntityHorse instance. Custom horse
     * armor is stored using the 23rd bit in the DataWatcher.
     * 
     * @param horseClass: A ClassNode representation of the EntityHorse class.
     */
    private void injectGetCustomHorseArmor (ClassNode horseClass) {
    
        MethodNode newMethod = new MethodNode(ACC_PRIVATE, "getCustomBookshelfHorseArmor", "()Lnet/minecraft/item/ItemStack;", null, null);
        newMethod.visitCode();
        
        Label l0 = new Label();
        newMethod.visitLabel(l0);
        newMethod.visitVarInsn(ALOAD, 0);
        newMethod.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("dataWatcher", "field_70180_af"), "Lnet/minecraft/entity/DataWatcher;");
        newMethod.visitIntInsn(BIPUSH, 23);
        newMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMHelper.getCorrectMapping("getWatchableObjectItemStack", "func_82710_f"), "(I)Lnet/minecraft/item/ItemStack;", false);
        newMethod.visitInsn(ARETURN);
        
        Label l1 = new Label();
        newMethod.visitLabel(l1);
        newMethod.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l1, 0);
        newMethod.visitMaxs(2, 1);
        newMethod.visitEnd();
        
        horseClass.methods.add(newMethod);
        
    }
    
    /**
     * Injects the setCustomBookshelfHorseArmor method into EntityHorse. This method takes one
     * instance of ItemStack as a parameter. The provided ItemStack will be set to the
     * EntityHorse's armor slot, and written to the DataWatcher under the 23rd bit.
     * 
     * @param horseClass: A ClassNode representation of the EntityHorse class.
     */
    private void injectSetCustomHorseArmor (ClassNode horseClass) {
    
        MethodNode newMethod = new MethodNode(ACC_PRIVATE, "setCustomBookshelfHorseArmor", "(Lnet/minecraft/item/ItemStack;)V", null, null);
        newMethod.visitCode();
        
        Label l0 = new Label();
        newMethod.visitLabel(l0);
        newMethod.visitVarInsn(ALOAD, 1);
        
        Label l1 = new Label();
        newMethod.visitJumpInsn(IFNONNULL, l1);
        newMethod.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
        newMethod.visitInsn(DUP);
        newMethod.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", ASMHelper.getCorrectMapping("stick", "field_151055_y"), "Lnet/minecraft/item/Item;");
        newMethod.visitInsn(ICONST_0);
        newMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V", false);
        newMethod.visitVarInsn(ASTORE, 1);
        newMethod.visitLabel(l1);
        newMethod.visitFrame(F_SAME, 0, null, 0, null);
        newMethod.visitVarInsn(ALOAD, 0);
        newMethod.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("dataWatcher", "field_70180_af"), "Lnet/minecraft/entity/DataWatcher;");
        newMethod.visitIntInsn(BIPUSH, 23);
        newMethod.visitVarInsn(ALOAD, 1);
        newMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMHelper.getCorrectMapping("updateObject", "func_75692_b"), "(ILjava/lang/Object;)V", false);
        
        Label l2 = new Label();
        newMethod.visitLabel(l2);
        newMethod.visitInsn(RETURN);
        
        Label l3 = new Label();
        newMethod.visitLabel(l3);
        newMethod.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l2, 0);
        newMethod.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l2, 1);
        newMethod.visitMaxs(5, 2);
        newMethod.visitEnd();
        
        horseClass.methods.add(newMethod);
    }
    
    /**
     * Transforms the isValidArmor method to take our custom horse armor into consideration.
     * This is done by checking if the ItemStack being used is an instance of our custom armor
     * item.
     * 
     * @param methodNode: A MethodNode representation of the isValidArmor method.
     */
    private void transformIsValidArmor (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", ASMHelper.getCorrectMapping("iron_horse_armor", "field_151138_bX"), "Lnet/minecraft/item/Item;"));
        needle.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, new LabelNode()));
        
        AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        LabelNode ln = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, ln));
        newInstr.add(new LabelNode());
        newInstr.add(new InsnNode(Opcodes.ICONST_1));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(ln);
        newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        
        methodNode.instructions.insertBefore(node, newInstr);
    }
    
    /**
     * Transforms the entityInit method of EntityHorse to also handle the 23rd bit within the
     * DataWatcher. This allows us to store and retrieve custom armor items from here. It also
     * allows for custom armor items to be synced across client and server.
     * 
     * @param methodNode: A MethodNode representation of the entityInit method.
     */
    private void transformEntityInit (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("dataWatcher", "field_70180_af"), "Lnet/minecraft/entity/DataWatcher;"));
        needle.add(new IntInsnNode(BIPUSH, 22));
        needle.add(new InsnNode(ICONST_0));
        needle.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMHelper.getCorrectMapping("addObject", "func_75682_a"), "(ILjava/lang/Object;)V", false));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("dataWatcher", "field_70180_af"), "Lnet/minecraft/entity/DataWatcher;"));
        newInstr.add(new IntInsnNode(BIPUSH, 23));
        newInstr.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
        newInstr.add(new InsnNode(DUP));
        newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Items", ASMHelper.getCorrectMapping("stick", "field_151055_y"), "Lnet/minecraft/item/Item;"));
        newInstr.add(new InsnNode(ICONST_0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V", false));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMHelper.getCorrectMapping("addObject", "func_75682_a"), "(ILjava/lang/Object;)V", false));
        
        methodNode.instructions.insert(pointer, newInstr);
    }
    
    /**
     * Transforms the getTotalArmorValue method to also take our custom horse armor into
     * consideration. This is done by checking if the horse is wearing our custom armor, if so
     * we return the value provided by the getArmorValue from our custom horse armor item.
     * 
     * @param methodNode: A MethodNode representation of getTotalArmorValue.
     */
    private void transformGetTotalArmorValue (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("armorValues", "field_110272_by"), "[I"));
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("func_110241_cb", "func_110241_cb"), "()I", false));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstructions = new InsnList();
        
        newInstructions.add(new LabelNode());
        newInstructions.add(new VarInsnNode(ALOAD, 0));
        newInstructions.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfHorseArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstructions.add(new VarInsnNode(ASTORE, 1));
        
        LabelNode l2 = new LabelNode();
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new JumpInsnNode(IFNULL, l2));
        
        newInstructions.add(new LabelNode());
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false));
        newInstructions.add(new TypeInsnNode(INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        newInstructions.add(new JumpInsnNode(IFEQ, l2));
        
        newInstructions.add(new LabelNode());
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false));
        newInstructions.add(new TypeInsnNode(CHECKCAST, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        newInstructions.add(new VarInsnNode(ALOAD, 0));
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, "net/darkhax/bookshelf/items/ItemHorseArmor", "getArmorValue", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I", false));
        newInstructions.add(new InsnNode(IRETURN));
        
        newInstructions.add(l2);
        
        methodNode.instructions.insertBefore(pointer, newInstructions);
    }
    
    /**
     * Transforms the updateHorseSlots method to take custom horse data slot into
     * consideration. If the slot is being updated, the item will be set to our custom data
     * slot.
     * 
     * @param methodNode: A MethodNode representation of the updateHorseSlots method.
     */
    private void transformUpdateHorseSlots (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("horseChest", "field_110296_bG"), "Lnet/minecraft/inventory/AnimalChest;"));
        needle.add(new InsnNode(Opcodes.ICONST_1));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("horseChest", "field_110296_bG"), "Lnet/minecraft/inventory/AnimalChest;"));
        newInstr.add(new InsnNode(Opcodes.ICONST_1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/inventory/AnimalChest", ASMHelper.getCorrectMapping("getStackInSlot", "func_70301_a"), "(I)Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "setCustomBookshelfHorseArmor", "(Lnet/minecraft/item/ItemStack;)V", false));
        newInstr.add(new LabelNode());
        
        methodNode.instructions.insertBefore(pointer, newInstr);
    }
    
    /**
     * Transforms the onInvChanged method to take our custom horse armor into consideration
     * when handling updates of the horses's inventory.
     * 
     * @param methodNode: A MethodNode representation of the onInvChanged method.
     */
    private void transformOnInvChanged (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("isHorseSaddled", "func_110257_ck"), "()Z", false));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 3));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfHorseArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));
        
        methodNode.instructions.insert(pointer, newInstr);
        
        needle = new InsnList();
        needle.add(new LdcInsnNode("mob.horse.armor"));
        needle.add(new LdcInsnNode(new Float("0.5")));
        needle.add(new InsnNode(Opcodes.FCONST_1));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("playSound", "func_85030_a"), "(Ljava/lang/String;FF)V", false));
        needle.add(new LabelNode());
        
        pointer = ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle);
        
        newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", ASMHelper.getCorrectMapping("stick", "field_151055_y"), "Lnet/minecraft/item/Item;"));
        LabelNode l9 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l9));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfHorseArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("isItemEqual", "func_77969_a"), "(Lnet/minecraft/item/ItemStack;)Z", false));
        newInstr.add(new JumpInsnNode(Opcodes.IFNE, l9));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new LdcInsnNode("mob.horse.armor"));
        newInstr.add(new LdcInsnNode(new Float("0.5")));
        newInstr.add(new InsnNode(Opcodes.FCONST_1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("playSound", "func_85030_a"), "(Ljava/lang/String;FF)V", false));
        newInstr.add(l9);
        
        methodNode.instructions.insert(pointer, newInstr);
    }
    
    /**
     * Transforms the setHorseTexturePaths method to take our custom armor into consideration.
     * This is done by checking if the armor item is an instance of our custom armor, if it is,
     * the getArmorTexture method from the item is returned.
     * 
     * @param methodNode: A MethodNode representation of the setHorseTexturePaths method.
     */
    private void transformSetHorseTexturePaths (MethodNode methodNode) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("func_110241_cb", "func_110241_cb"), "()I", false));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 3));
        
        AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle);
        
        InsnList newInstr = new InsnList();
        LabelNode l17 = new LabelNode();
        newInstr.add(l17);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfHorseArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));
        LabelNode l18 = new LabelNode();
        newInstr.add(l18);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        LabelNode l19 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l19));
        LabelNode l20 = new LabelNode();
        newInstr.add(l20);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("field_110280_bR", "field_110280_bR"), "[Ljava/lang/String;"));
        newInstr.add(new InsnNode(Opcodes.ICONST_2));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/items/ItemHorseArmor", "getArmorTexture", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", false));
        newInstr.add(new InsnNode(Opcodes.AASTORE));
        LabelNode l21 = new LabelNode();
        newInstr.add(l21);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "java/lang/StringBuilder"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("texturePrefix", "field_110286_bQ"), "Ljava/lang/String;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false));
        newInstr.add(new LdcInsnNode("cst-"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMHelper.getCorrectMapping("getUnlocalizedName", "func_77977_a"), "()Ljava/lang/String;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false));
        newInstr.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/passive/EntityHorse", ASMHelper.getCorrectMapping("texturePrefix", "field_110286_bQ"), "Ljava/lang/String;"));
        newInstr.add(new InsnNode(Opcodes.RETURN));
        newInstr.add(l19);
        
        methodNode.instructions.insert(node, newInstr);
    }
}
