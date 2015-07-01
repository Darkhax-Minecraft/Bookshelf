/*******************************************************************************************************************
 * Copyright: SanAndreasP
 * 
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *            http://creativecommons.org/licenses/by-nc-sa/4.0/
 *            
 * Changes: -Reformat to project specifics
 *          -Updated mappings to latest forge
 *          -Added java documentation
 *          -Changed mappings of new methods and classes
 *          -Opcodes is now a static import
 *******************************************************************************************************************/
package net.darkhax.bookshelf.asm;

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
    public byte[] transform (String name, String transformedName, byte[] bytes) {
    
        if (transformedName.equals("net.minecraft.entity.passive.EntityHorse"))
            return transformHorse(bytes);
        
        return bytes;
    }
    
    private static byte[] transformHorse (byte[] bytes) {
    
        ClassNode horseClass = ASMHelper.createClassFromByteArray(bytes);
        
        injectGetCustomBookshelfArmor(horseClass);
        injectSetCustomBoookhelfArmor(horseClass);
        transformGetTotalArmorValue(ASMHelper.getMethodFromClass(horseClass, getTotalArmorValue, "()I"));
        transformOnInventoryChanged(ASMHelper.getMethodFromClass(horseClass, onInventoryChanged, "(Lnet/minecraft/inventory/InventoryBasic;)V"));
        transformUpdateHorseSlots(ASMHelper.getMethodFromClass(horseClass, updateHorseSlots, "()V"));
        transformEntityInit(ASMHelper.getMethodFromClass(horseClass, entityInit, "()V"));
        transformIsValidArmor(ASMHelper.getMethodFromClass(horseClass, isArmorItem, "(Lnet/minecraft/item/Item;)Z"));
        //transformInteract(ASMHelper.getMethodFromClass(horseClass, interact, "(Lnet/minecraft/entity/player/EntityPlayer;)Z"));
        
        try {
            
            transformSetHorseTexturePaths(ASMHelper.getMethodFromClass(horseClass, setHorseTexturePaths, "()V"));
        }
        
        catch (ASMHelper.MethodNotFoundException e) {
            
        }
        
        return ASMHelper.createByteArrayFromClass(horseClass, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    }
    
    /**
     * Injects a new method into the EntityHorse class. This new method allows for the retrieval of a custom armor item that is stored on bit 23 of the DataWatcher.
     * @param horseClass: A ClassNode representation of the EntityHorse class. 
     */
    private static void injectGetCustomBookshelfArmor (ClassNode horseClass) {
    
        MethodNode newMethod = new MethodNode(Opcodes.ACC_PRIVATE, "getCustomBookshelfArmor", "()Lnet/minecraft/item/ItemStack;", null, null);
        newMethod.visitCode();
        
        Label l0 = new Label();
        newMethod.visitLabel(l0);
        newMethod.visitVarInsn(Opcodes.ALOAD, 0);
        newMethod.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        newMethod.visitIntInsn(Opcodes.BIPUSH, 23);
        newMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", getWatchableObjectItemStack, "(I)Lnet/minecraft/item/ItemStack;", false);
        newMethod.visitInsn(Opcodes.ARETURN);
        
        Label l1 = new Label();
        newMethod.visitLabel(l1);
        newMethod.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l1, 0);
        newMethod.visitMaxs(2, 1);
        newMethod.visitEnd();
        
        horseClass.methods.add(newMethod);
    }
    
    /**
     * Injects a new method into the EntityHorse class. This new method allows for the setting of a custom armor item which is stored on bit 23 of the DataWatcher. 
     * @param horseClass: A ClassNode representation of the EntityHorse class. 
     */
    private static void injectSetCustomBoookhelfArmor (ClassNode horseClass) {
    
        MethodNode newMethod = new MethodNode(Opcodes.ACC_PRIVATE, "setCustomBookshelfArmor", "(Lnet/minecraft/item/ItemStack;)V", null, null);
        newMethod.visitCode();
        
        Label l0 = new Label();
        newMethod.visitLabel(l0);
        newMethod.visitVarInsn(Opcodes.ALOAD, 1);
        
        Label l1 = new Label();
        newMethod.visitJumpInsn(Opcodes.IFNONNULL, l1);
        newMethod.visitTypeInsn(Opcodes.NEW, "net/minecraft/item/ItemStack");
        newMethod.visitInsn(Opcodes.DUP);
        newMethod.visitFieldInsn(Opcodes.GETSTATIC, "net/minecraft/init/Items", stick, "Lnet/minecraft/item/Item;");
        newMethod.visitInsn(Opcodes.ICONST_0);
        newMethod.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V", false);
        newMethod.visitVarInsn(Opcodes.ASTORE, 1);
        newMethod.visitLabel(l1);
        newMethod.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        newMethod.visitVarInsn(Opcodes.ALOAD, 0);
        newMethod.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        newMethod.visitIntInsn(Opcodes.BIPUSH, 23);
        newMethod.visitVarInsn(Opcodes.ALOAD, 1);
        newMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", updateObject, "(ILjava/lang/Object;)V", false);
        
        Label l3 = new Label();
        newMethod.visitLabel(l3);
        newMethod.visitInsn(Opcodes.RETURN);
        
        Label l4 = new Label();
        newMethod.visitLabel(l4);
        newMethod.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l3, 0);
        newMethod.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l3, 1);
        newMethod.visitMaxs(5, 2);
        newMethod.visitEnd();
        
        horseClass.methods.add(newMethod);
    }
    
    /**
     * Transforms the getTotalArmorValue method to take custom armor items into consideration. This is done by checking if the item stored within he 23rd bit of the data watcher is an instance of Bookshelf's implementation of ItemHorseArmor, if it is, the value returned by the item's getArmorValue method will be returned by this method. 
     * @param method: Instance of a MethodNode representation of the getTotalArmorValue method. 
     */
    private static void transformGetTotalArmorValue (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/entity/passive/EntityHorse", armorValues, "[I"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", getHorseArmorIndexSynced, "()I", false));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 1));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        
        LabelNode l2 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l2));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/darkhax/bookshelf/items/ItemHorseArmor"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/darkhax/bookshelf/items/ItemHorseArmor", "getArmorValue", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I", false));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l2);
        
        method.instructions.insertBefore(pointer, newInstr);
    }
    
    /**
     * Transforms the onInventoryChanged method to take custom armor items into consideration when updating the inventory. 
     * @param method: Instance of a MethodNode representation of the onInventoryChanged method. 
     */
    private static void transformOnInventoryChanged (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", isHorseSaddled, "()Z", false));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 3));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));
        
        method.instructions.insert(pointer, newInstr);
        
        needle = new InsnList();
        needle.add(new LdcInsnNode("mob.horse.armor"));
        needle.add(new LdcInsnNode(new Float("0.5")));
        needle.add(new InsnNode(Opcodes.FCONST_1));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", playSound, "(Ljava/lang/String;FF)V", false));
        needle.add(new LabelNode());
        
        pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", stick, "Lnet/minecraft/item/Item;"));
        
        LabelNode l9 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l9));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", isItemEqual, "(Lnet/minecraft/item/ItemStack;)Z", false));
        newInstr.add(new JumpInsnNode(Opcodes.IFNE, l9));
       
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new LdcInsnNode("mob.horse.armor"));
        newInstr.add(new LdcInsnNode(new Float("0.5")));
        newInstr.add(new InsnNode(Opcodes.FCONST_1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", playSound, "(Ljava/lang/String;FF)V", false));
        newInstr.add(l9);
        
        method.instructions.insert(pointer, newInstr);
    }
    
    /**
     * Transforms the setHorseTexturePaths method to take our custom armor item into consideration. If the horse has a custom armor item in it's inventory and that item is an instance of our ItemHorseArmor implementation, the item's getArmorTexture method will me used for the armor texture path. 
     * @param method: Instance of a MethodNode representation of the setHorseTexturePaths method. 
     */
    private static void transformSetHorseTexturePaths (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", getHorseArmorIndexSynced, "()I", false));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 3));
        
        AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        LabelNode l17 = new LabelNode();
        newInstr.add(l17);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "getCustomBookshelfArmor", "()Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));
        
        LabelNode l18 = new LabelNode();
        newInstr.add(l18);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
       
        LabelNode l19 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l19));
        
        LabelNode l20 = new LabelNode();
        newInstr.add(l20);
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", horseTextureArray, "[Ljava/lang/String;"));
        newInstr.add(new InsnNode(Opcodes.ICONST_2));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
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
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", texturePrefix, "Ljava/lang/String;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false));
        newInstr.add(new LdcInsnNode("cst-"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getUnlocalizedName, "()Ljava/lang/String;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false));
        newInstr.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/passive/EntityHorse", texturePrefix, "Ljava/lang/String;"));
        newInstr.add(new InsnNode(Opcodes.RETURN));
        newInstr.add(l19);
        
        method.instructions.insert(node, newInstr);
    }
    
    /**
     * Transforms the updateHorseSlots method to take custom armor items into consideration. When an item is set to the armor slot, it will also be stored under our data bit in the data watcher.
     * @param method: Instance of a MethodNode representation of the updateHorseSlots method. 
     */
    private static void transformUpdateHorseSlots (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", horseChest, "Lnet/minecraft/inventory/AnimalChest;"));
        needle.add(new InsnNode(Opcodes.ICONST_1));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", horseChest, "Lnet/minecraft/inventory/AnimalChest;"));
        newInstr.add(new InsnNode(Opcodes.ICONST_1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/inventory/AnimalChest", getStackInSlot, "(I)Lnet/minecraft/item/ItemStack;", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "setCustomBookshelfArmor", "(Lnet/minecraft/item/ItemStack;)V", false));
        newInstr.add(new LabelNode());
        
        method.instructions.insertBefore(pointer, newInstr);
    }
    
    /**
     * Transforms the entityInit method to designate the 23rd bit within the DataWatcher to store an ItemStack. This storage is used by our custom methods. 
     * @param method: Instance of a MethodNode representation of the entityInit method. 
     */
    private static void transformEntityInit (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        needle.add(new IntInsnNode(Opcodes.BIPUSH, 22));
        needle.add(new InsnNode(Opcodes.ICONST_0));
        needle.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", addObject, "(ILjava/lang/Object;)V", false));
        
        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        newInstr.add(new IntInsnNode(Opcodes.BIPUSH, 23));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/item/ItemStack"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", stick, "Lnet/minecraft/item/Item;"));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", addObject, "(ILjava/lang/Object;)V", false));
        
        method.instructions.insert(pointer, newInstr);
    }
    
    /**
     * Transforms the isValidArmor method to accept items which use our implementation of ItemHorseArmor as a valid horse armor item.
     * @param method: Instance of a MethodNode representation of the isValidArmor method. 
     */
    private static void transformIsValidArmor (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", iron_horse_armor, "Lnet/minecraft/item/Item;"));
        needle.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, new LabelNode()));
        
        AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        
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
        
        method.instructions.insertBefore(node, newInstr);
    }
    
    /**
     * 
     * @param method
     */
    private static void transformInteract (MethodNode method) {
    
        InsnList needle = new InsnList();
        needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", diamond_horse_armor, "Lnet/minecraft/item/Item;"));
        needle.add(new JumpInsnNode(Opcodes.IF_ACMPNE, new LabelNode()));
        needle.add(new LabelNode());
        
        needle.add(new LineNumberNode(801, new LabelNode()));
        needle.add(new InsnNode(Opcodes.ICONST_3));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 4));
        
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(804, new LabelNode()));
        needle.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        
        AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/darkhax/bookshelf/items/ItemHorseArmor"));
       
        LabelNode l8 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l8));
        newInstr.add(new InsnNode(Opcodes.ICONST_4));
        newInstr.add(new VarInsnNode(Opcodes.ISTORE, 4));
        newInstr.add(l8);
       
        newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        
        method.instructions.insert(node, newInstr);
    }
    
    // Fields
    private static String dataWatcher = ASMHelper.getAppropriateMapping("dataWatcher", "field_70180_af");
    private static String stick = ASMHelper.getAppropriateMapping("stick", "field_151055_y");
    private static String armorValues = ASMHelper.getAppropriateMapping("armorValues", "field_110272_by");
    private static String isHorseSaddled = ASMHelper.getAppropriateMapping("isHorseSaddled", "func_110257_ck");
    private static String horseTextureArray = ASMHelper.getAppropriateMapping("field_110280_bR", "field_110280_bR");
    private static String texturePrefix = ASMHelper.getAppropriateMapping("field_110286_bQ", "field_110286_bQ");
    private static String iron_horse_armor = ASMHelper.getAppropriateMapping("iron_horse_armor", "field_151138_bX");
    private static String diamond_horse_armor = ASMHelper.getAppropriateMapping("diamond_horse_armor", "field_151125_bZ");
    private static String horseChest = ASMHelper.getAppropriateMapping("horseChest", "field_110296_bG");
    
    // Methods
    private static String interact = ASMHelper.getAppropriateMapping("interact", "func_70085_c");
    private static String isArmorItem = ASMHelper.getAppropriateMapping("func_146085_a", "func_146085_a");
    private static String entityInit = ASMHelper.getAppropriateMapping("entityInit", "func_70088_a");
    private static String updateHorseSlots = ASMHelper.getAppropriateMapping("func_110232_cE", "func_110232_cE");
    private static String onInventoryChanged = ASMHelper.getAppropriateMapping("onInventoryChanged", "func_76316_a");
    private static String getTotalArmorValue = ASMHelper.getAppropriateMapping("getTotalArmorValue", "func_70658_aO");
    private static String setHorseTexturePaths = ASMHelper.getAppropriateMapping("setHorseTexturePaths", "func_110247_cG");
    private static String getWatchableObjectItemStack = ASMHelper.getAppropriateMapping("getWatchableObjectItemStack", "func_82710_f");
    private static String updateObject = ASMHelper.getAppropriateMapping("updateObject", "func_75692_b");
    private static String getHorseArmorIndexSynced = ASMHelper.getAppropriateMapping("func_110241_cb", "func_110241_cb");
    private static String getItem = ASMHelper.getAppropriateMapping("getItem", "func_77973_b");
    private static String playSound = ASMHelper.getAppropriateMapping("playSound", "func_85030_a");
    private static String isItemEqual = ASMHelper.getAppropriateMapping("isItemEqual", "func_77969_a");
    private static String addObject = ASMHelper.getAppropriateMapping("addObject", "func_75682_a");
    private static String getStackInSlot = ASMHelper.getAppropriateMapping("getStackInSlot", "func_70301_a");
    private static String getUnlocalizedName = ASMHelper.getAppropriateMapping("getUnlocalizedName", "func_77977_a");
}