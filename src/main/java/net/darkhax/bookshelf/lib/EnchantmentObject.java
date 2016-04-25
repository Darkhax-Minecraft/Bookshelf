package net.darkhax.bookshelf.lib;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class EnchantmentObject {
    
    private final Enchantment enchantment;
    private int level;
    
    public EnchantmentObject(Enchantment enchantment, int level) {
        
        this.enchantment = enchantment;
        this.level = level;
    }
    
    public EnchantmentObject(ByteBuf buf) {
        
        this.enchantment = Enchantment.getEnchantmentByLocation(ByteBufUtils.readUTF8String(buf));
        this.level = buf.readInt();
    }
    
    public Enchantment getEnchantment () {
        
        return this.enchantment;
    }
    
    public int getLevel () {
        
        return this.level;
    }
    
    public void setLevel (int level) {
        
        this.level = level;
    }
    
    public void writeToBuffer (ByteBuf buf) {
        
        ByteBufUtils.writeUTF8String(buf, this.enchantment.getRegistryName().toString());
        buf.writeInt(this.level);
    }
}