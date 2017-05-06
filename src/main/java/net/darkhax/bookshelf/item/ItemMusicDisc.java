/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMusicDisc extends ItemRecord {

    /**
     * Constructor for a basic music disc. This opens up the constructor of ItemRecord, and
     * will automatically add the record to the ore dictionary.
     *
     * @param name The name of the record's song. Used in unlocalized name.
     * @param sound The sound to play when the record is put in a juke box.
     */
    public ItemMusicDisc (String name, SoundEvent sound) {

        super(name, sound);
        OreDictionary.registerOre(OreDictUtils.RECORD, this);
    }
}